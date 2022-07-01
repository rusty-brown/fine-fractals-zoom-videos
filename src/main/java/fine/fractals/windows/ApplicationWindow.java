package fine.fractals.windows;

import fine.fractals.context.FractalEngineImpl;
import fine.fractals.windows.abst.UIWindow;
import fine.fractals.windows.adapter.UIKeyAdapter;
import fine.fractals.windows.adapter.UIMouseMotionAdapter;
import fine.fractals.windows.dispatcher.UIKeyDispatcher;
import fine.fractals.windows.listener.UIMouseListener;
import fine.fractals.windows.listener.UIMouseWheelListener;

import javax.swing.*;
import java.awt.*;

import static fine.fractals.Main.RESOLUTION_HEIGHT;
import static fine.fractals.Main.RESOLUTION_WIDTH;
import static fine.fractals.context.ApplicationImpl.APP_NAME;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.context.mandelbrot.MandelbrotImpl.Mandelbrot;
import static fine.fractals.fractal.Fractal.NAME;
import static fine.fractals.images.FractalImage.MandelbrotMaskImage;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ApplicationWindow extends UIWindow {

	public final JFrame frame;
	private int lineHeight;
	private String name;

	public static boolean repaintDone = false;
	public static boolean showInfo = true;

	public ApplicationWindow(UIMouseListener uiMouseListener,
							 UIMouseWheelListener uiMouseWheelListener,
							 UIKeyDispatcher uiKeyDispatcher) {
		super.resolutionWidth = RESOLUTION_WIDTH;
		super.resolutionHeight = RESOLUTION_HEIGHT;
		super.name = "Application - " + NAME + " - " + APP_NAME;

		/*  Initialize UI */
		this.frame = new JFrame(name);
		this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.frame.getContentPane().add(this);
		this.frame.pack();
		this.frame.setLocationByPlatform(true);
		this.frame.setVisible(true);

		/* Initialize UI Actions */
		final JLayeredPane layeredPane = this.frame.getRootPane().getLayeredPane();
		layeredPane.addMouseListener(uiMouseListener);
		super.hideDefaultCursor(frame);

		this.motionAdapter = new UIMouseMotionAdapter(this);
		layeredPane.addMouseMotionListener(this.motionAdapter);
		layeredPane.addMouseWheelListener(uiMouseWheelListener);

		this.frame.addKeyListener(new UIKeyAdapter());
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(uiKeyDispatcher);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		// g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

		String n = FractalEngineImpl.calculationProgress;
		if (n == null || "".equals(n)) {
			n = "00";
		}
		this.frame.setTitle(n + " - " + this.name);

		Color textColor;
		/* Paint mandelbrot Mask */
		Mandelbrot.createMask();
		/* image full size */
		// g2d.drawImage(mandelbrotMask, 0, 0, mandelbrotMask.getWidth(), mandelbrotMask.getHeight(), null);
		/* image size fit to window size */
		g2d.drawImage(MandelbrotMaskImage, 0, 0, getWidth(), getHeight(), null);

		textColor = Color.BLACK;

		super.drawMouseCursor(g2d);

		if (showInfo) {

			g2d.setColor(textColor);
			this.lineHeight = g2d.getFontMetrics().getHeight();
			int line = 0;

			/* Calculation Text */
			if (FractalEngineImpl.calculationInProgress) {
				g2d.drawString(FractalEngineImpl.calculationText + " " + FractalEngineImpl.calculationProgress, col(0), row(line));
			}
			line++;

			/* Target coordinates PX */
			g2d.drawString("Target px: ", col(0), row(line));
			g2d.drawString(Target.getScreenFromCornerT() + ", " + Target.getScreenFromCornerX(), col(1), row(line));
			g2d.drawString(Target.getScreenFromCenterT() + ", " + Target.getScreenFromCenterX(), col(2), row(line));
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
		ApplicationWindow.repaintDone = true;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(resolutionWidth, resolutionHeight);
	}

	private int col(int col) {
		return 20 + col * 180;
	}

	private int row(int line) {
		return 20 + line * lineHeight;
	}

}
