package fine.fractals.windows.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.machine.TargetImpl.Target;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class UIMouseListener implements MouseListener {

    private static final Logger log = LogManager.getLogger(UIMouseListener.class);

    public UIMouseListener() {
        log.debug("constructor");
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        log.debug("mouseClicked()");
        if (isRightMouseButton(me)) {
            log.info("move to new coordinates");
            log.info(Target.getTextRe() + " = " + Target.getTextIm());
            AreaMandelbrot.moveToCoordinates();
            AreaFinebrot.moveToCoordinates();
        } else {
            log.debug("Left click");
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
