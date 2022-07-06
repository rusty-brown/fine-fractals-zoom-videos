package fine.fractals.windows.adapter;

import fine.fractals.windows.MandelbrotWindow;
import fine.fractals.windows.FinebrotWindow;
import fine.fractals.windows.abst.UIWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static fine.fractals.context.TargetImpl.Target;

public class UIMouseMotionAdapter extends MouseMotionAdapter {
	private final UIWindow meWindow;
	private MandelbrotWindow mandelbrotWindow = null;
	private FinebrotWindow finebrotWindow = null;

	public UIMouseMotionAdapter(UIWindow meWindow) {
		this.meWindow = meWindow;
	}

	public void setMandelbrotWindow(MandelbrotWindow mandelbrotWindow) {
		this.mandelbrotWindow = mandelbrotWindow;
	}

	public void setFinebrotWindow(FinebrotWindow finebrotWindow) {
		this.finebrotWindow = finebrotWindow;
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		Target.update(me.getX(), me.getY());
		this.meWindow.activate();
		if (this.mandelbrotWindow != null) {
			this.mandelbrotWindow.deactivate();
		}
		if (this.finebrotWindow != null) {
			this.finebrotWindow.deactivate();
		}
	}
}
