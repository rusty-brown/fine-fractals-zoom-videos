package fine.fractals.windows;

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
import static fine.fractals.fractal.Fractal.NAME;
import static fine.fractals.images.FractalImage.FinebrotImage;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FinebrotWindow extends UIWindow {

	public final JFrame frame;

	public FinebrotWindow(UIMouseListener uiMouseListener,
						  UIMouseWheelListener uiMouseWheelListener,
						  UIKeyDispatcher uiKeyDispatcher
	) {
		super.name = NAME + " - " + APP_NAME;

		/*  Initialize UI */
		this.frame = new JFrame(name);
		this.frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.frame.getContentPane().add(this);
		this.frame.pack();
		this.frame.setLocationByPlatform(true);
		this.frame.setVisible(true);

        /* Initialize UI Actions */
		final JLayeredPane layeredPane = this.frame.getRootPane().getLayeredPane();
		super.hideDefaultCursor(frame);

		this.motionAdapter = new UIMouseMotionAdapter(this);
		layeredPane.addMouseMotionListener(this.motionAdapter);

		this.frame.addKeyListener(new UIKeyAdapter());

		layeredPane.addMouseListener(uiMouseListener);
		layeredPane.addMouseWheelListener(uiMouseWheelListener);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(uiKeyDispatcher);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		final Graphics2D g2d = (Graphics2D) g.create();
		/* image size fit to window size */
		g2d.drawImage(FinebrotImage, 0, 0, getWidth(), getHeight(), null);

		super.drawMouseCursor(g2d);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
	}
}
