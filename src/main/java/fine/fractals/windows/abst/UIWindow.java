package fine.fractals.windows.abst;

import fine.fractals.windows.adapter.UIMouseMotionAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static fine.fractals.context.TargetImpl.Target;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public abstract class UIWindow extends JComponent {

	private boolean active = false;
	protected UIMouseMotionAdapter motionAdapter;
	protected String name;

	protected UIWindow() {
	}

	protected void hideDefaultCursor(JFrame frame) {
		// Transparent 16 X 16 pixel cursor image.
		final BufferedImage cursorImg = new BufferedImage(16, 16, TYPE_INT_ARGB);
		// Create a new blank cursor.
		final Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		// Set the blank cursor to the JFrame.
		frame.getContentPane().setCursor(blankCursor);
	}

	protected void drawMouseCursor(Graphics g) {
		if (active) {
			g.setColor(RED);
		} else {
			g.setColor(GREEN);
		}
		final int line = 3;
		final int x = Target.getScreenFromCornerX();
		final int y = Target.getScreenFromCornerY();
		g.drawLine(x + line, y, x - line, y);
		g.drawLine(x, y + line, x, y - line);
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

}