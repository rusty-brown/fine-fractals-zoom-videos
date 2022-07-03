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

    private static final FinebrotListOfImagesToVideoWithAudio VideoMaker;

    static {
        log.info("init");
        VideoMaker = new FinebrotListOfImagesToVideoWithAudio();
    }

    private FinebrotListOfImagesToVideoWithAudio() {
    }

    public static void main(String[] args) throws Exception {
        VideoMaker.listOfFinebrotImages();
        VideoMaker.createVideoWithAudio();
    }


    private void listOfFinebrotImages() throws IOException {
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


    private void createVideoWithAudio() throws IOException {
        log.info("createVideo()");

        FFmpegFrameRecorder recorder = null;
        FrameGrabber audioGrabber = null;
        try {
            audioGrabber = new FFmpegFrameGrabber(AUDIO_FILE);
            audioGrabber.start();

            recorder = new FFmpegFrameRecorder(VIDEO_NAME, RESOLUTION_WIDTH, RESOLUTION_HEIGHT, audioGrabber.getAudioChannels());
            recorder.setVideoCodec(AV_CODEC_ID_MPEG4);
            recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
            recorder.setFrameRate(25);
            recorder.setSampleRate(audioGrabber.getSampleRate());
            recorder.setFormat("mp4");
            recorder.start();

            final Java2DFrameConverter converter = new Java2DFrameConverter();
            Frame audioFrame;
            // final JavaFXFrameConverter converter = new JavaFXFrameConverter();

            /*
             * zoom in
             */
            for (URL url : urls) {
                recorder.record(converter.getFrame(ImageIO.read(url)));
            }

            /*
             * 2s wait
             */
            final BufferedImage last = ImageIO.read(urls.get(urls.size() - 1));
            for (int i = 0; i < 50; i++) {
                recorder.record(converter.getFrame(last));
            }

            /*
             * zoom out
             */
            Collections.reverse(urls);
            for (URL url : urls) {
                recorder.record(converter.getFrame(ImageIO.read(url)));
            }

            /*
             * 2s wait
             */
            final BufferedImage first = ImageIO.read(urls.get(urls.size() - 1));
            for (int i = 0; i < 50; i++) {
                recorder.record(converter.getFrame(first));
            }

            /*
             * Add soundtrack
             */
            while ((audioFrame = audioGrabber.grabFrame()) != null) {
                recorder.record(audioFrame);
            }

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
}
