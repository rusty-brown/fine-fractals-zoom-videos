package fine.fractals.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class UIMouseWheelListener implements MouseWheelListener {

    private static final Logger log = LogManager.getLogger(UIMouseWheelListener.class);

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {

        if (UIKeyDispatcher.isCtrl()) {

        } else if (UIKeyDispatcher.isAlt()) {

        }

        if (mwe.getWheelRotation() < 0) {
            /* Mouse wheel moved UP */
        } else {
            /* Mouse wheel moved DOWN */
        }
    }
}
