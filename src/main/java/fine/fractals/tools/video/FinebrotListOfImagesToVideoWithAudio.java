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

        FFmpegFrameRecorder recorder = null;
        try {
            recorder = new FFmpegFrameRecorder(VIDEO_NAME_MUTE, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);

            recorder.setVideoCodec(AV_CODEC_ID_MPEG4);
            recorder.setFrameRate(25);
            recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
            recorder.setFormat("mp4");

            recorder.start();

            final Java2DFrameConverter converter = new Java2DFrameConverter();

            for (URL url : urls) {
                /*
                 * Make the video, 25 images /s
                 */
                recorder.record(converter.getFrame(ImageIO.read(url)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
        }
    }

    public void mergeAudioAndVideo() throws Exception {
        log.info("mergeAudioAndVideo()");

        FrameGrabber videoGrabber = null;
        FrameGrabber audioGrabber = null;
        FrameRecorder recorder = null;
        try {
            videoGrabber = new FFmpegFrameGrabber(VIDEO_NAME_MUTE);
            audioGrabber = new FFmpegFrameGrabber(AUDIO_FILE);

            videoGrabber.start();
            audioGrabber.start();

            recorder = new FFmpegFrameRecorder(VIDEO_NAME,
                    videoGrabber.getImageWidth(),
                    videoGrabber.getImageHeight(),
                    audioGrabber.getAudioChannels());

            recorder.setFrameRate(videoGrabber.getFrameRate());
            recorder.setSampleRate(audioGrabber.getSampleRate());
            recorder.setVideoCodec(AV_CODEC_ID_MPEG4);
            recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
            recorder.setFormat("mp4");

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (recorder != null) {
                    recorder.release();
                }
                if (videoGrabber != null) {
                    videoGrabber.release();
                }
                if (audioGrabber != null) {
                    audioGrabber.release();
                }
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }
    }
}
