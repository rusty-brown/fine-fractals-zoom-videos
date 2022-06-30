package fine.fractals.windows.adapter;

import fine.fractals.windows.abst.UIWindow;
import fine.fractals.windows.ApplicationWindow;
import fine.fractals.windows.FractalWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.context.TargetImpl.Target;

public class UIMouseMotionAdapter extends MouseMotionAdapter {
	private final UIWindow meWindow;
	private ApplicationWindow otherApplicationWindow = null;
	private FractalWindow otherFractalWindow = null;

	private int mousePositionT;
	private int mousePositionX;

	public UIMouseMotionAdapter(UIWindow meWindow) {
		this.meWindow = meWindow;
	}

	public void setApplicationWindow(ApplicationWindow otherApplicationWindow) {
		this.otherApplicationWindow = otherApplicationWindow;
	}

	public void setDesignWindow(FractalWindow otherFractalWindow) {
		this.otherFractalWindow = otherFractalWindow;
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		mousePositionT = me.getX();
		mousePositionX = me.getY();
		Target.update(mousePositionT, mousePositionX);
		this.meWindow.activate();
		if (this.otherApplicationWindow != null) {
			this.otherApplicationWindow.deactivate();
		}
		if (this.otherFractalWindow != null) {
			this.otherFractalWindow.deactivate();
		}
		Application.repaint();
	}
}
