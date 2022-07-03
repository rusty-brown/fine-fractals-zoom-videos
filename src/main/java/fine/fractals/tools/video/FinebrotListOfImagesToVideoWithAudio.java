package fine.fractals.tools.video;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_MPEG4;
import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_YUV420P;

public class FinebrotListOfImagesToVideoWithAudio {

    private static final Logger log = LogManager.getLogger(FinebrotListOfImagesToVideoWithAudio.class);

    private static final String VIDEO_NAME = "Finebrot_ff_1.avi";
    private static final String AUDIO_FILE = "/home/lukas/Downloads/Arcadia.mp3";
    private static final String FINEBROT_IMAGE_LOCATION = "/home/lukas/Fractals/del 01/";

    private final List<URL> urls = new ArrayList<>();

    private FFmpegFrameRecorder recorder;
    private Java2DFrameConverter converter;

    private FrameGrabber audioGrabber;
    private static final FinebrotListOfImagesToVideoWithAudio VideoMaker;


    static {
        log.info("init");
        VideoMaker = new FinebrotListOfImagesToVideoWithAudio();
    }

    private FinebrotListOfImagesToVideoWithAudio() {
    }

    public static void main(String[] args) throws Exception {
        VideoMaker.makeListOfFinebrotImages();
        VideoMaker.makeVideoWithAudio();
    }


    private void makeListOfFinebrotImages() throws IOException {
        log.info("listOfFinebrotImages()");

        try (Stream<Path> paths = Files.walk(Paths.get(FINEBROT_IMAGE_LOCATION))) {
            paths.sorted().filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            urls.add(path.toUri().toURL());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }


    private void makeVideoWithAudio() throws IOException {
        log.info("createVideo()");

        try {
            log.info("audioGrabber");
            audioGrabber = new FFmpegFrameGrabber(AUDIO_FILE);
            audioGrabber.start();

            log.info("recorder");
            recorder = new FFmpegFrameRecorder(VIDEO_NAME, RESOLUTION_WIDTH, RESOLUTION_HEIGHT, audioGrabber.getAudioChannels());
            recorder.setVideoCodec(AV_CODEC_ID_MPEG4);
            recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
            recorder.setFrameRate(25);
            recorder.setSampleRate(audioGrabber.getSampleRate());
            recorder.setFormat("mp4");
            recorder.start();

            log.info("converter");
            converter = new Java2DFrameConverter();
            // converter = new JavaFXFrameConverter();

            log.info("zoom in");
            renderListOfImages();

            log.info("2s wait");
            final BufferedImage last = ImageIO.read(urls.get(urls.size() - 1));
            renderImage(last, 3);

            log.info("zoom out");
            Collections.reverse(urls);
            renderListOfImages();

            log.info("2s wait");
            final BufferedImage first = ImageIO.read(urls.get(urls.size() - 1));
            renderImage(first, 2);

            log.info("Add soundtrack");
            Frame audioFrame;
            while ((audioFrame = audioGrabber.grabFrame()) != null) {
                recorder.record(audioFrame);
            }

            log.info("Finished.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
            if (audioGrabber != null) {
                audioGrabber.stop();
                audioGrabber.release();
            }
        }
    }

    private void renderListOfImages() throws IOException {
        for (URL url : urls) {
            recorder.record(converter.getFrame(ImageIO.read(url)));
        }
    }

    private void renderImage(BufferedImage image, int seconds) throws IOException {
        for (int i = 0; i < seconds * 25; i++) {
            recorder.record(converter.getFrame(image));
        }
    }
}
