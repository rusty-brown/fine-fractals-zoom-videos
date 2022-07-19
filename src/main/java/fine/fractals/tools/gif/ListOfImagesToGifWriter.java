package fine.fractals.tools.gif;

import fine.fractals.data.annotation.EditMe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import static javax.imageio.ImageIO.getImageWritersBySuffix;
import static javax.imageio.ImageTypeSpecifier.createFromBufferedImageType;

public class ListOfImagesToGifWriter {

    @EditMe
    public static final String FINEBROT_IMAGE_LOCATION = "/home/lukas/Fractals/";
    @EditMe
    private static final String VIDEO_NAME = "FinebrotImages.gif";

    private static final Logger log = LogManager.getLogger(ListOfImagesToGifWriter.class);

    protected ImageWriter gifWriter;
    protected ImageWriteParam imageWriteParam;
    protected IIOMetadata imageMetaData;

    public ListOfImagesToGifWriter(ImageOutputStream outputStream, int imageType, int timeBetweenFramesMS, boolean loopContinuously) throws IOException {
        gifWriter = getImageWritersBySuffix("gif").next();
        imageWriteParam = gifWriter.getDefaultWriteParam();
        imageMetaData = gifWriter.getDefaultImageMetadata(createFromBufferedImageType(imageType), imageWriteParam);

        final String metaFormatName = imageMetaData.getNativeMetadataFormatName();
        final IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
        final IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");

        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(timeBetweenFramesMS / 10));
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

        final IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
        commentsNode.setAttribute("CommentExtension", "Created by MAH");

        final IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
        final IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");

        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");

        final int loop = loopContinuously ? 0 : 1;

        child.setUserObject(new byte[]{0x1, (byte) (loop & 0xFF), (byte) (0)});
        appExtensionsNode.appendChild(child);

        imageMetaData.setFromTree(metaFormatName, root);
        gifWriter.setOutput(outputStream);
        gifWriter.prepareWriteSequence(null);
    }

    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
        final int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++) {
            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) {
                return ((IIOMetadataNode) rootNode.item(i));
            }
        }
        final IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return node;
    }

    /**
     * main() method to generate GIF
     */
    public static void main(String[] args) throws Exception {

        final ArrayList<URL> paths = makeListOfFinebrotImages();
        final BufferedImage firstImage = ImageIO.read(paths.get(0));
        final ImageOutputStream output = new FileImageOutputStream(new File(VIDEO_NAME));

        log.info("Build GIF writer");
        log.info(paths.get(0));
        final ListOfImagesToGifWriter writer = new ListOfImagesToGifWriter(output, firstImage.getType(), 40, false);

        writer.writeToSequence(firstImage);
        for (int i = 1; i < paths.size() - 1; i++) {
            BufferedImage nextImage = ImageIO.read(paths.get(i));
            log.info(paths.get(i));
            writer.writeToSequence(nextImage);
        }

        writer.close();
        output.close();
        log.info("end.");
    }

    private static ArrayList<URL> makeListOfFinebrotImages() throws IOException {
        ArrayList<URL> ret = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(FINEBROT_IMAGE_LOCATION))) {
            paths.sorted().filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            ret.add(path.toUri().toURL());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return ret;
    }

    public void writeToSequence(RenderedImage img) throws IOException {
        gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
    }

    public void close() throws IOException {
        gifWriter.endWriteSequence();
    }
}