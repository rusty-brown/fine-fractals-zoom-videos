package fine.fractals.windows.adapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static fine.fractals.context.ApplicationImpl.Application;
import static fine.fractals.context.TargetImpl.Target;
import static fine.fractals.context.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.windows.MandelbrotWindow.showInfo;
import static java.awt.event.KeyEvent.*;

public class UIKeyAdapter extends KeyAdapter {

    private static final Logger log = LogManager.getLogger(UIKeyAdapter.class);

    public UIKeyAdapter() {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        final int code = ke.getKeyCode();
        log.info(KeyEvent.getKeyText(code) + " | " + code + " | " + ke.getKeyChar());
        switch (code) {
            case VK_C -> {
                final String targetString
                        = "INIT_MANDELBROT_AREA_SIZE = " + AreaMandelbrot.sizeImString() + ";\n"
                        + "INIT_MANDELBROT_TARGET_re = " + Target.getTextRe() + ";\n"
                        + "INIT_MANDELBROT_TARGET_im = " + Target.getTextIm() + ";\n";
                /* Write cursor location to console */
                System.out.println(targetString);
                /* Copy cursor location to clipboard */
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(targetString), null);
            }
            case VK_I -> showInfo = !showInfo;
            case VK_R -> Application.repaintWindows();
        }
    }
}
