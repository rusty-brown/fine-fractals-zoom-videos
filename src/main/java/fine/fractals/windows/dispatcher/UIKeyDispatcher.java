package fine.fractals.windows.dispatcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.KEY_PRESSED;
import static java.awt.event.KeyEvent.KEY_RELEASED;
import static java.awt.event.KeyEvent.VK_ALT;
import static java.awt.event.KeyEvent.VK_CONTROL;

public class UIKeyDispatcher implements KeyEventDispatcher {

    private static final Logger log = LogManager.getLogger(UIKeyDispatcher.class);
    private static boolean ctrlPressed = false;
    private static boolean altPressed = false;

    public UIKeyDispatcher() {
        log.debug("constructor");
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
            case KEY_PRESSED -> {
                if (code == VK_CONTROL) {
                    ctrlPressed = true;
                }
                if (code == VK_ALT) {
                    altPressed = true;
                }
            }
            case KEY_RELEASED -> {
                ctrlPressed = false;
                altPressed = false;
            }
        }
        return false;
    }
}
