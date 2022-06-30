package fine.fractals.windows.adapter;

import fine.fractals.windows.ApplicationWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.TargetImpl.Target;

public class UIKeyAdapter extends KeyAdapter {

    private static final Logger log = LogManager.getLogger(UIKeyAdapter.class);
    private boolean start = false;

    public UIKeyAdapter() {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int code = ke.getKeyCode();
        log.info(KeyEvent.getKeyText(code) + " | " + code + " | " + ke.getKeyChar());
        switch (code) {

            case KeyEvent.VK_C:
                /* Copy cursor location to clipboard copy it */
                final String targetString
                        = "public static double INIT_IMAGE_TARGET_reT = " + Target.getTextReT() + ";\n"
                        + "public static double TARGET_IM = " + Target.getTextImX() + ";\n"
                        + "public static double INITIAL_AREA_DOMAIN_SIZE = " + AreaMandelbrot.sizeImXString() + ";\n"
                        + "public static double INITIAL_AREA_IMAGE_SIZE = " + AreaFinebrot.sizeImXString() + ";";
                System.out.println(targetString);
                StringSelection stringSelection = new StringSelection(targetString);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                break;


            case KeyEvent.VK_I:
                ApplicationWindow.showInfo = !ApplicationWindow.showInfo;
                break;
        }
    }
}
