package fine.fractals.windows.listener;

import fine.fractals.engine.CalculationThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.context.ApplicationImpl.REPEAT;
import static fine.fractals.context.FractalEngineImpl.calculationInProgress;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.mandelbrot.MandelbrotImpl.Mandelbrot;

public class UIMouseListener implements MouseListener {

    private static final Logger log = LogManager.getLogger(UIMouseListener.class);

    public UIMouseListener() {
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (!REPEAT) {
            if (SwingUtilities.isRightMouseButton(me)) {
                log.info("Right click");
                if (!calculationInProgress) {
                    AreaMandelbrot.moveToCoordinates();
                    AreaFinebrot.moveToCoordinates();
                    Application.zoomIn();
                    CalculationThread.calculate();
                }
            } else {
                log.info("Left click, FIX, then use Enter");
                Mandelbrot.fixDomainOptimizationOnClick();
                Application.repaint();
            }
        } else {
            log.info("click skipped");
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
