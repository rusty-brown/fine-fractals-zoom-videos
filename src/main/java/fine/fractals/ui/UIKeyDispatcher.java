package fine.fractals.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

public class UIKeyDispatcher implements KeyEventDispatcher {

    private static boolean ctrlPressed = false;
    private static boolean altPressed = false;

    private static final Logger log = LogManager.getLogger(UIKeyDispatcher.class);

    public UIKeyDispatcher() {

    }

    public static boolean isCtrl() {
        return UIKeyDispatcher.ctrlPressed;
    }

    public static boolean isAlt() {
        return UIKeyDispatcher.altPressed;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent ke) {
        int code = ke.getKeyCode();
        log.info(KeyEvent.getKeyText(code) + " | " + code + " | " + ke.getKeyChar());
        switch (ke.getID()) {
            case KeyEvent.KEY_PRESSED:
                if (code == KeyEvent.VK_CONTROL) {
                    ctrlPressed = true;
                }
                if (code == KeyEvent.VK_ALT) {
                    altPressed = true;
                }
                break;
            case KeyEvent.KEY_RELEASED:
                if (code == KeyEvent.VK_CONTROL) {
                    ctrlPressed = false;
                }
                if (code == KeyEvent.VK_ALT) {
                    altPressed = false;
                }
                break;
        }

        return false;
    }

}
