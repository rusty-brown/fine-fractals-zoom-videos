package fine.fractals.windows;

import fine.fractals.windows.adapter.UIKeyAdapter;
import fine.fractals.windows.adapter.UIMouseMotionAdapter;
import fine.fractals.windows.common.UIWindow;
import fine.fractals.windows.dispatcher.UIKeyDispatcher;
import fine.fractals.windows.listener.UIMouseListener;
import fine.fractals.windows.listener.UIMouseWheelListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.NAME;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_HEIGHT;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.RESOLUTION_WIDTH;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.images.FractalImages.MandelbrotMaskImage;
import static fine.fractals.machine.ApplicationImpl.APP_NAME;
import static fine.fractals.machine.TargetImpl.Target;
import static java.awt.Color.BLACK;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MandelbrotWindow extends UIWindow {

    private static final Logger log = LogManager.getLogger(MandelbrotWindow.class);
    public static boolean showInfo = true;
    public final JFrame frame;
    private int lineHeight;

    public MandelbrotWindow(UIMouseListener uiMouseListener,
                            UIMouseWheelListener uiMouseWheelListener,
                            UIKeyDispatcher uiKeyDispatcher) {
        log.debug("initialize");
        super.name = NAME + " - " + APP_NAME;

        this.frame = new JFrame(name);
        this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.frame.getContentPane().add(this);
        this.frame.pack();
        this.frame.setLocationByPlatform(true);
        this.frame.setVisible(true);

        log.debug("actions");
        final JLayeredPane layeredPane = this.frame.getRootPane().getLayeredPane();
        layeredPane.addMouseListener(uiMouseListener);
        super.hideDefaultCursor(frame);

        log.debug("adapter");
        this.motionAdapter = new UIMouseMotionAdapter(this);
        layeredPane.addMouseMotionListener(this.motionAdapter);
        layeredPane.addMouseWheelListener(uiMouseWheelListener);

        log.debug("listener");
        this.frame.addKeyListener(new UIKeyAdapter());

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(uiKeyDispatcher);
    }

    @Override
    public void paintComponent(Graphics g) {
        log.debug("paintComponent()");
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g.create();
        this.frame.setTitle(this.name);

        log.debug("drawImage");
        /* image size fit to window size */
        g2d.drawImage(MandelbrotMaskImage, 0, 0, getWidth(), getHeight(), null);

        log.debug("drawMouseCursor");
        super.drawMouseCursor(g2d);

        if (showInfo) {
            g2d.setColor(BLACK);
            this.lineHeight = g2d.getFontMetrics().getHeight();
            int line = 0;

            /* Target coordinates PX */
            g2d.drawString("Target px: ", col(0), row(line));
            g2d.drawString(Target.getScreenFromCornerY() + ", " + Target.getScreenFromCornerX(), col(1), row(line));
            g2d.drawString(Target.getScreenFromCenterX() + ", " + Target.getScreenFromCenterY(), col(2), row(line));
            line++;

            /* Target coordinates domain */
            g2d.drawString("Target: ", col(0), row(line));
            g2d.drawString(Target.getTextRe(), col(1), row(line));
            g2d.drawString(Target.getTextIm(), col(2), row(line));
            line++;

            /* Area size Mandelbrot */
            g2d.drawString("Domain h/w: ", col(0), row(line));
            g2d.drawString(AreaMandelbrot.sizeImString(), col(1), row(line));
            g2d.drawString(AreaMandelbrot.sizeReString(), col(2), row(line));
            line++;

            /* Area size Finebrot */
            g2d.drawString("Domain h/w: ", col(0), row(line));
            g2d.drawString(AreaFinebrot.sizeImString(), col(1), row(line));
            g2d.drawString(AreaFinebrot.sizeReString(), col(2), row(line));
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
    }

    private int col(int col) {
        return 20 + col * 180;
    }

    private int row(int line) {
        return 20 + line * lineHeight;
    }

    public void setFinebrotWindow(FinebrotWindow otherFinebrotWindow) {
        this.motionAdapter.setFinebrotWindow(otherFinebrotWindow);
    }

    public void alwaysOnTheTop(boolean top) {
        frame.setAlwaysOnTop(top);
    }
}
