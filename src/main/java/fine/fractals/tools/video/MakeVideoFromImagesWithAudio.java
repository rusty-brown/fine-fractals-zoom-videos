package fine.fractals.tools.video;

import fine.fractals.Main;
import io.humble.video.*;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;
import io.humble.video.javaxsound.AudioFrame;
import io.humble.video.javaxsound.MediaAudioConverter;
import io.humble.video.javaxsound.MediaAudioConverterFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.humble.video.Coder.Flag.FLAG_GLOBAL_HEADER;
import static io.humble.video.ContainerFormat.Flag.GLOBAL_HEADER;
import static io.humble.video.MediaDescriptor.Type.MEDIA_AUDIO;
import static io.humble.video.PixelFormat.Type.PIX_FMT_YUV420P;
import static io.humble.video.javaxsound.MediaAudioConverterFactory.DEFAULT_JAVA_AUDIO;

public class MakeVideoFromImagesWithAudio {

    private static final Logger log = LogManager.getLogger(MakeVideoFromImagesWithAudio.class);

    private static final String VIDEO_NAME = "Finebrot_blue2.avi";
    private static final String AUDIO_FILE = "/home/lukas/Downloads/Arcadia.mp3";
    private static final String FORMAT = "mpegts";
    private static final int frameRate = 25;

    public static void main2(String[] args) throws InterruptedException, IOException {

        log.info("Setup video encoder");

        final Muxer muxer = Muxer.make(VIDEO_NAME, null, FORMAT);
        final MuxerFormat format = muxer.getFormat();
        final Codec codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());
        final Encoder encoder = Encoder.make(codec);
        final Rational framerate = Rational.make(1, frameRate);

        encoder.setWidth(Main.RESOLUTION_WIDTH);
        encoder.setHeight(Main.RESOLUTION_HEIGHT);
        encoder.setPixelFormat(PIX_FMT_YUV420P);
        encoder.setTimeBase(framerate);

        if (format.getFlag(GLOBAL_HEADER)) {
            encoder.setFlag(FLAG_GLOBAL_HEADER, true);
        }

        encoder.open(null, null);
        muxer.addNewStream(encoder);
        muxer.open(null, null);


        log.info("Read Fractal Images");

        final List<BufferedImage> images = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get("/home/lukas/Fractals/del 01/"))) {
            paths.sorted().filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            log.info(path.toUri().toURL());
                            images.add(ImageIO.read(path.toUri().toURL()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }


        log.info("Encode list of images to video");

        final MediaPacket packet = MediaPacket.make();
        final MediaPicture picture = MediaPicture.make(encoder.getWidth(), encoder.getHeight(), PIX_FMT_YUV420P);
        picture.setTimeBase(framerate);

        int i = 0;
        for (BufferedImage image : images) {
            final MediaPictureConverter converter = MediaPictureConverterFactory.createConverter(image, picture);
            converter.toPicture(picture, image, i++);
            do {

                /* ************************* */
                encoder.encode(packet, picture);
                /* ************************* */

                if (packet.isComplete()) {
                    muxer.write(packet, false);
                }
            } while (packet.isComplete());
        }


        log.info("Finish encoding cached data");

        do {
            encoder.encode(packet, null);
            if (packet.isComplete()) {
                muxer.write(packet, false);
            }
        } while (packet.isComplete());


        muxer.close();

        log.info("Finished.");
    }


    public static void main(String[] args) throws InterruptedException, IOException, LineUnavailableException {


        final Demuxer demuxer = Demuxer.make();

        demuxer.open(AUDIO_FILE, null, false, true, null, null);

        final int numStreams = demuxer.getNumStreams();

        /*
         * Iterate through the streams to find the first audio stream
         */
        int audioStreamId = -1;
        Decoder audioDecoder = null;
        for (int i = 0; i < numStreams; i++) {
            final DemuxerStream stream = demuxer.getStream(i);
            final Decoder decoder = stream.getDecoder();
            if (decoder != null && decoder.getCodecType() == MEDIA_AUDIO) {
                audioStreamId = i;
                audioDecoder = decoder;
                // stop at the first one.
                break;
            }
        }
        if (audioStreamId == -1) {
            throw new RuntimeException("could not find audio stream in container: " + AUDIO_FILE);
        }

        /*
         * Now we have found the audio stream in this file.  Let's open up our decoder so it can
         * do work.
         */
        audioDecoder.open(null, null);

        /*
         * We allocate a set of samples with the same number of channels as the
         * coder tells us is in this buffer.
         */
        final MediaAudio samples = MediaAudio.make(
                audioDecoder.getFrameSize(),
                audioDecoder.getSampleRate(),
                audioDecoder.getChannels(),
                audioDecoder.getChannelLayout(),
                audioDecoder.getSampleFormat()
        );

        /*
         * A converter object we'll use to convert Humble Audio to a format that
         * Java Audio can actually play. The details are complicated, but essentially
         * this converts any audio format (represented in the samples object) into
         * a default audio format suitable for Java's speaker system (which will
         * be signed 16-bit audio, stereo (2-channels), resampled to 22,050 samples
         * per second).
         */

        final MediaAudioConverter converter = MediaAudioConverterFactory.createConverter(DEFAULT_JAVA_AUDIO, samples);

        /*
         * An AudioFrame is a wrapper for the Java Sound system that abstracts away
         * some stuff. Go read the source code if you want -- it's not very complicated.
         */
        final AudioFrame audioFrame = AudioFrame.make(converter.getJavaFormat());
        if (audioFrame == null) {
            throw new LineUnavailableException();
        }

        /* We will use this to cache the raw-audio we pass to and from
         * the java sound system.
         */
        ByteBuffer rawAudio = null;

        /*
         * Now, we start walking through the container looking at each packet. This
         * is a decoding loop, and as you work with Humble you'll write a lot
         * of these.
         *
         * Notice how in this loop we reuse all of our objects to avoid
         * reallocating them. Each call to Humble resets objects to avoid
         * unnecessary reallocation.
         */
        final MediaPacket packet = MediaPacket.make();
        while (demuxer.read(packet) >= 0) {
            /*
             * Now we have a packet, let's see if it belongs to our audio stream
             */
            if (packet.getStreamIndex() == audioStreamId) {
                /*
                 * A packet can actually contain multiple sets of samples (or frames of samples
                 * in audio-decoding speak).  So, we may need to call decode audio multiple
                 * times at different offsets in the packet's data.  We capture that here.
                 */
                int offset = 0;
                int bytesRead = 0;
                do {
                    bytesRead += audioDecoder.decode(samples, packet, offset);
                    if (samples.isComplete()) {
                        rawAudio = converter.toJavaAudio(rawAudio, samples);


                        audioFrame.play(rawAudio);


                    }
                    offset += bytesRead;
                } while (offset < packet.getSize());
            }
        }

        // Some audio decoders (especially advanced ones) will cache
        // audio data before they begin decoding, so when you are done you need
        // to flush them. The convention to flush Encoders or Decoders in Humble Video
        // is to keep passing in null until incomplete samples or packets are returned.
        do {
            audioDecoder.decode(samples, null, 0);
            if (samples.isComplete()) {
                rawAudio = converter.toJavaAudio(rawAudio, samples);


                audioFrame.play(rawAudio);


            }
        } while (samples.isComplete());

        // It is good practice to close demuxers when you're done to free
        // up file handles. Humble will EVENTUALLY detect if nothing else
        // references this demuxer and close it then, but get in the habit
        // of cleaning up after yourself, and your future girlfriend/boyfriend
        // will appreciate it.
        demuxer.close();

        // similar with the demuxer, for the audio playback stuff, clean up after yourself.
        audioFrame.dispose();
    }
}
