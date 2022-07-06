package fine.fractals.color.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static fine.fractals.context.PaletteImpl.Palette;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ColorTest extends JComponent {

    private final JFrame frame;
    private final Integer resolution;
    private final BufferedImage testImage;
    private final int height = 100;

    private boolean painted = false;

    private static final Logger log = LogManager.getLogger(ColorTest.class);

    public static void execute() {
        new ColorTest();
    }

    private ColorTest() {
        log.debug("init");
        this.resolution = Palette.colorResolution();
        this.testImage = new BufferedImage(resolution, height, TYPE_INT_RGB);

        this.frame = new JFrame("ColorTest: " + Palette.name());
        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.getContentPane().add(this);
        this.frame.pack();
        this.frame.setLocationByPlatform(true);
        this.frame.setVisible(true);

        this.frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == VK_ESCAPE) {
                    frame.dispose();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        log.debug("paintComponent()");
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        if (!painted) {
            painted = true;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < resolution; x++) {
                    testImage.setRGB(x, y, Palette.getSpectrumValue(x).getRGB());
                }
            }
        }
        g2d.drawImage(testImage, 0, 0, resolution, height, null);
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        log.debug("getPreferredSize()");
        return new Dimension(resolution, height);
    }

}
