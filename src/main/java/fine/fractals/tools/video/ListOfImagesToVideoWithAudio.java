package fine.fractals.tools.video;

import fine.fractals.data.annotation.EditMe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
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

import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264;
import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_YUV420P;

public class ListOfImagesToVideoWithAudio {

    private static final Logger log = LogManager.getLogger(ListOfImagesToVideoWithAudio.class);

    @EditMe
    private static final String VIDEO_NAME = "Finebrot_1.avi";
    @EditMe
    private static final String AUDIO_FILE = "/home/lukas/Downloads/Arcadia.mp3";
    @EditMe
    private static final String FINEBROT_IMAGE_LOCATION = "/home/lukas/Fractals/";
    @EditMe
    private static final int RESOLUTION_WIDTH = 1280;
    @EditMe
    private static final int RESOLUTION_HEIGHT = 720;

    private static final ListOfImagesToVideoWithAudio VideoMaker = new ListOfImagesToVideoWithAudio();
    private final List<URL> urls = new ArrayList<>();
    private FFmpegFrameRecorder recorder;
    private Java2DFrameConverter converter;
    private FrameGrabber audioGrabber;

    private ListOfImagesToVideoWithAudio() {
    }

    public static void main(String[] args) throws IOException {
        VideoMaker.makeListOfFinebrotImages();
        VideoMaker.makeVideoWithAudio();
    }

    private void makeListOfFinebrotImages() throws IOException {
        log.debug("makeListOfFinebrotImages()");
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
        log.info("makeVideoWithAudio()");

        try {
            log.info("audioGrabber");
            audioGrabber = new FFmpegFrameGrabber(AUDIO_FILE);
            audioGrabber.start();

            log.info("recorder");
            recorder = new FFmpegFrameRecorder(VIDEO_NAME, RESOLUTION_WIDTH, RESOLUTION_HEIGHT, audioGrabber.getAudioChannels());
            recorder.setVideoCodec(AV_CODEC_ID_H264);
            recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
            recorder.setFrameRate(25);
            recorder.setVideoQuality(9.5);
            recorder.setSampleRate(audioGrabber.getSampleRate());
            recorder.setFormat("mp4");
            recorder.start();

            log.info("converter");
            converter = new Java2DFrameConverter();

            log.info("RESOLUTION_WIDTH = " + RESOLUTION_WIDTH);
            log.info("RESOLUTION_HEIGHT = " + RESOLUTION_HEIGHT);

            log.info("zoom in");
            renderListOfImages();

            log.info("wait");
            final BufferedImage last = ImageIO.read(urls.get(urls.size() - 1));
            renderImage(last, 3 * 25);

            log.info("zoom out");
            Collections.reverse(urls);
            renderListOfImages();

            log.info("video end");
            final BufferedImage first = ImageIO.read(urls.get(urls.size() - 1));
            renderImage(first, 3 * 25);

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
            log.info(url);
            recorder.record(converter.getFrame(ImageIO.read(url)));
        }
    }

    private void renderImage(BufferedImage image, int frames) throws IOException {
        for (int i = 0; i < frames; i++) {
            recorder.record(converter.getFrame(image));
        }
    }

    @SuppressWarnings(value = "unused")
    public static BufferedImage rotateClockwise(BufferedImage src) {
        final int w = src.getWidth();
        final int h = src.getHeight();
        final BufferedImage dest = new BufferedImage(h, w, src.getType());
        final Graphics2D graphics2D = dest.createGraphics();
        /* sets new graphic origin */
        graphics2D.translate((h - w) / 2, (h - w) / 2);
        graphics2D.rotate(Math.PI / 2, h / 2.0, w / 2.0);
        graphics2D.drawRenderedImage(src, null);
        return dest;
    }
}
