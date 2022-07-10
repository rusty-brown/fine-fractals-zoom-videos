package fine.fractals.windows.adapter;

import fine.fractals.windows.FinebrotWindow;
import fine.fractals.windows.MandelbrotWindow;
import fine.fractals.windows.common.UIWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static fine.fractals.machine.TargetImpl.Target;

public class UIMouseMotionAdapter extends MouseMotionAdapter {

    private static final Logger log = LogManager.getLogger(MouseMotionAdapter.class);

    private final UIWindow meWindow;
    private MandelbrotWindow mandelbrotWindow = null;
    private FinebrotWindow finebrotWindow = null;

    public UIMouseMotionAdapter(UIWindow meWindow) {
        log.debug("constructor");
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
