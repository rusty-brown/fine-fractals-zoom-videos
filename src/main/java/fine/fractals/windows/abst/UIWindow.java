package fine.fractals.windows.abst;

import fine.fractals.windows.ApplicationWindow;
import fine.fractals.windows.FractalWindow;
import fine.fractals.windows.adapter.UIMouseMotionAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static fine.fractals.context.TargetImpl.Target;

public abstract class UIWindow extends JComponent {

	private boolean active = false;
	protected UIMouseMotionAdapter motionAdapter;
	protected String name;

	protected int resolutionWidth;
	protected int resolutionHeight;

	protected UIWindow() {
	}

	protected void hideDefaultCursor(JFrame frame) {
		// Transparent 16 imX 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		// Set the blank cursor to the JFrame.
		frame.getContentPane().setCursor(blankCursor);
	}

	protected void drawMouseCursor(Graphics g) {
		if (active) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.GREEN);
		}
		final int line = 3;
		final int t = Target.getScreenFromCornerT();
		final int x = Target.getScreenFromCornerX();
		g.drawLine(t + line, x, t - line, x);
		g.drawLine(t, x + line, t, x - line);
	}

	public void setApplicationWindow(ApplicationWindow otherApplicationWindow) {
		this.motionAdapter.setApplicationWindow(otherApplicationWindow);
	}

	public void setDesignWindow(FractalWindow otherFractalWindow) {
		this.motionAdapter.setDesignWindow(otherFractalWindow);
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

}