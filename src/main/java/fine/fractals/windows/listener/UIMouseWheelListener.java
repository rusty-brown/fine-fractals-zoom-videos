package fine.fractals.windows.listener;

import fine.fractals.windows.dispatcher.UIKeyDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class UIMouseWheelListener implements MouseWheelListener {

    private static final Logger log = LogManager.getLogger(UIMouseWheelListener.class);

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {

        if (UIKeyDispatcher.isCtrl()) {
            log.info("ctrl");
        } else if (UIKeyDispatcher.isAlt()) {
            log.info("alt");
        }

        if (mwe.getWheelRotation() < 0) {
            /* Mouse wheel moved UP */
            log.info("up");
        } else {
            /* Mouse wheel moved DOWN */
            log.info("down");
        }
    }
}
