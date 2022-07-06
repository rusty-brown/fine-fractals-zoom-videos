package fine.fractals.windows;

import fine.fractals.windows.abst.UIWindow;
import fine.fractals.windows.adapter.UIKeyAdapter;
import fine.fractals.windows.adapter.UIMouseMotionAdapter;
import fine.fractals.windows.dispatcher.UIKeyDispatcher;
import fine.fractals.windows.listener.UIMouseListener;
import fine.fractals.windows.listener.UIMouseWheelListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.ApplicationImpl.APP_NAME;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.fractal.abst.Fractal.NAME;
import static fine.fractals.images.FractalImage.MandelbrotMaskImage;
import static java.awt.Color.BLACK;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MandelbrotWindow extends UIWindow {

	private static final Logger log = LogManager.getLogger(MandelbrotWindow.class);

	public final JFrame frame;
	private int lineHeight;

	public static boolean showInfo = true;

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

			/* Area size domain */
			g2d.drawString("Domain h/w: ", col(0), row(line));
			g2d.drawString(AreaMandelbrot.sizeImString(), col(1), row(line));
			g2d.drawString(AreaMandelbrot.sizeReTString(), col(2), row(line));
			line++;

			/* Area size image */
			g2d.drawString("Domain h/w: ", col(0), row(line));
			g2d.drawString(AreaFinebrot.sizeImString(), col(1), row(line));
			g2d.drawString(AreaFinebrot.sizeReTString(), col(2), row(line));
			line++;

			/* Mandelbrot Element value */
			g2d.drawString("El. value: ", col(0), row(line));
			g2d.drawString(Target.getMandelbrotValue(), col(1), row(line));
			g2d.drawString(Target.getMandelbrotState(), col(2), row(line));
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
}
