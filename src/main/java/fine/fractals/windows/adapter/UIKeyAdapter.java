package fine.fractals.windows.adapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.windows.MandelbrotWindow.showInfo;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_R;

public class UIKeyAdapter extends KeyAdapter {

    private static final Logger log = LogManager.getLogger(UIKeyAdapter.class);

    public UIKeyAdapter() {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        final int code = ke.getKeyCode();
        log.info(KeyEvent.getKeyText(code) + " | " + code + " | " + ke.getKeyChar());
        switch (code) {
            case KeyEvent.VK_C -> {
                /* Copy cursor location to clipboard copy it */
                final String targetString
                        = "public static double INIT_IMAGE_TARGET_re = " + Target.getTextRe() + ";\n"
                        + "public static double INIT_IMAGE_TARGET_im = " + Target.getTextIm() + ";\n"
                        + "public static double INITIAL_AREA_DOMAIN_SIZE = " + AreaMandelbrot.sizeImString() + ";\n"
                        + "public static double INITIAL_AREA_IMAGE_SIZE = " + AreaFinebrot.sizeImString() + ";";
                System.out.println(targetString);
                final StringSelection stringSelection = new StringSelection(targetString);
                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
            case VK_I -> showInfo = !showInfo;
            case VK_R -> Application.repaintWindows();
        }
    }
}
