package fine.fractals.color.things;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class ColorTest extends JComponent {

    private static JFrame frame;
    private static Palette palette;
    private static Integer resolution = null;
    private boolean painted = false;
    private BufferedImage testImage = null;

    private ColorTest() {
    }

    public static void execute(Palette palette) {
        resolution = palette.colorResolution();

        ColorTest.palette = palette;
        ColorTest app = new ColorTest();

        frame = new JFrame("ColorTest: " + Palette.getName());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(app);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                }
            }
        });
    }

    private BufferedImage applyTestColorPalette() {

        BufferedImage testImage = new BufferedImage(resolution, 100, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < resolution; x++) {
                testImage.setRGB(x, y, palette.getSpectrumValue(x).getRGB());
            }
        }
        return testImage;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        if (!painted) {
            painted = true;
            testImage = applyTestColorPalette();
        }
        g2d.drawImage(testImage, 0, 0, resolution, 100, null);
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(resolution, 100);
    }

}
