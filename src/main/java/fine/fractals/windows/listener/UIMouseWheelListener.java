package fine.fractals.windows.listener;

import fine.fractals.windows.dispatcher.UIKeyDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class UIMouseWheelListener implements MouseWheelListener {

    private static final Logger log = LogManager.getLogger(UIMouseWheelListener.class);

    public UIMouseWheelListener() {
        log.debug("constructor");
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        if (UIKeyDispatcher.isCtrl()) {
            log.debug("ctrl");
        } else if (UIKeyDispatcher.isAlt()) {
            log.debug("alt");
        }

        if (mwe.getWheelRotation() < 0) {
            /* Mouse wheel moved UP */
            log.debug("up");
        } else {
            /* Mouse wheel moved DOWN */
            log.debug("down");
        }
    }
}
