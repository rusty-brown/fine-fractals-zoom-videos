package fine.fractals.windows.adapter;

import fine.fractals.windows.ApplicationWindow;
import fine.fractals.windows.FractalWindow;
import fine.fractals.windows.abst.UIWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static fine.fractals.context.TargetImpl.Target;

public class UIMouseMotionAdapter extends MouseMotionAdapter {
	private final UIWindow meWindow;
	private ApplicationWindow otherApplicationWindow = null;
	private FractalWindow otherFractalWindow = null;

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
		Target.update(me.getX(), me.getY());
		this.meWindow.activate();
		if (this.otherApplicationWindow != null) {
			this.otherApplicationWindow.deactivate();
		}
		if (this.otherFractalWindow != null) {
			this.otherFractalWindow.deactivate();
		}
	}
}
