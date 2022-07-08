package fine.fractals.windows.listener;

import fine.fractals.machine.EngineThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.fractal.mandelbrot.MandelbrotImpl.Mandelbrot;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.ApplicationImpl.REPEAT;
import static fine.fractals.machine.FractalEngineImpl.calculationInProgress;

public class UIMouseListener implements MouseListener {

    private static final Logger log = LogManager.getLogger(UIMouseListener.class);

    public UIMouseListener() {
        log.debug("constructor");
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        log.debug("mouseClicked()");
        if (!REPEAT) {
            if (SwingUtilities.isRightMouseButton(me)) {
                log.info("Right click");
                if (!calculationInProgress) {
                    AreaMandelbrot.moveToCoordinates();
                    AreaFinebrot.moveToCoordinates();
                    Application.zoomIn();
                    EngineThread.calculate();
                }
            } else {
                log.info("Left click, FIX, then use Enter");
                Mandelbrot.fixDomainOptimizationOnClick();
                Application.repaintWindows();
            }
        } else {
            log.info("click skipped");
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        log.debug("mousePressed()");
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        log.debug("mouseReleased()");
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        log.debug("mouseEntered()");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        log.debug("mouseExited()");
    }
}
