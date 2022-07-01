package fine.fractals.windows.adapter;

import fine.fractals.windows.MandelbrotWindow;
import fine.fractals.windows.FinebrotWindow;
import fine.fractals.windows.abst.UIWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static fine.fractals.context.TargetImpl.Target;

public class UIMouseMotionAdapter extends MouseMotionAdapter {
	private final UIWindow meWindow;
	private MandelbrotWindow otherMandelbrotWindow = null;
	private FinebrotWindow otherFinebrotWindow = null;

	public UIMouseMotionAdapter(UIWindow meWindow) {
		this.meWindow = meWindow;
	}

	public void setApplicationWindow(MandelbrotWindow otherMandelbrotWindow) {
		this.otherMandelbrotWindow = otherMandelbrotWindow;
	}

	public void setDesignWindow(FinebrotWindow otherFinebrotWindow) {
		this.otherFinebrotWindow = otherFinebrotWindow;
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		Target.update(me.getX(), me.getY());
		this.meWindow.activate();
		if (this.otherMandelbrotWindow != null) {
			this.otherMandelbrotWindow.deactivate();
		}
		if (this.otherFinebrotWindow != null) {
			this.otherFinebrotWindow.deactivate();
		}
	}
}
