package fine.fractals.tools.video;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bytedeco.javacv.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_MPEG4;
import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_YUV420P;

public class FinebrotListOfImagesToVideoWithAudio {

    private static final Logger log = LogManager.getLogger(FinebrotListOfImagesToVideoWithAudio.class);

    private static final String VIDEO_NAME = "Finebrot_ff_1.avi";
    private static final String VIDEO_NAME_MUTE = new StringBuilder(VIDEO_NAME).insert(VIDEO_NAME.indexOf("."), "_mute").toString();
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
        VideoMaker.createVideo();
        VideoMaker.mergeAudioAndVideo();
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


    private void createVideo() throws IOException {
        log.info("createVideo()");

        try (
                final FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(VIDEO_NAME_MUTE, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
                final Java2DFrameConverter converter = new Java2DFrameConverter()
        ) {
            recorder.setVideoCodec(AV_CODEC_ID_MPEG4);
            recorder.setFrameRate(25);
            recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
            recorder.setFormat("mp4");
            recorder.start();

            for (URL url : urls) {
                /*
                 * Make the video, 25 images /s
                 */
                recorder.record(converter.getFrame(ImageIO.read(url)));
            }

            recorder.stop();
            recorder.release();
        }
    }

    public void mergeAudioAndVideo() throws Exception {
        log.info("mergeAudioAndVideo()");

        try (
                final FrameGrabber videoGrabber = new FFmpegFrameGrabber(VIDEO_NAME_MUTE);
                final FrameGrabber audioGrabber = new FFmpegFrameGrabber(AUDIO_FILE);
                final FrameRecorder recorder = new FFmpegFrameRecorder(
                        VIDEO_NAME,
                        videoGrabber.getImageWidth(),
                        videoGrabber.getImageHeight(),
                        audioGrabber.getAudioChannels()
                )
        ) {
            videoGrabber.start();
            audioGrabber.start();
            recorder.setFormat("mp4");
            recorder.setFrameRate(videoGrabber.getFrameRate());
            recorder.setSampleRate(audioGrabber.getSampleRate());
            recorder.start();

            Frame videoFrame;
            Frame audioFrame;
            while ((videoFrame = videoGrabber.grabFrame()) != null) {
                recorder.record(videoFrame);
            }
            while ((audioFrame = audioGrabber.grabFrame()) != null) {
                recorder.record(audioFrame);
            }

            videoGrabber.stop();
            audioGrabber.stop();
            recorder.stop();

            recorder.release();
            videoGrabber.release();
            audioGrabber.release();
        }
    }
}
