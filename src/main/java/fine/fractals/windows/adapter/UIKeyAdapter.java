package fine.fractals.windows.adapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static fine.fractals.fractal.finebrot.common.FinebrotAbstractImpl.ITERATION_min;
import static fine.fractals.fractal.finebrot.common.FinebrotCommonImpl.ITERATION_MAX;
import static fine.fractals.fractal.mandelbrot.AreaMandelbrotImpl.AreaMandelbrot;
import static fine.fractals.machine.ApplicationImpl.Application;
import static fine.fractals.machine.TargetImpl.Target;
import static fine.fractals.windows.MandelbrotWindow.showInfo;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_R;

public class UIKeyAdapter extends KeyAdapter {

    private static final Logger log = LogManager.getLogger(UIKeyAdapter.class);

    public UIKeyAdapter() {
        log.debug("constructor");
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        final int code = ke.getKeyCode();
        log.info(code + " | " + ke.getKeyChar());
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
            case VK_R -> {
                Application.repaintMandelbrotWindow();
                Application.repaintFinebrotWindow();
            }
            case VK_K -> {
                ITERATION_MAX *= 1.05;
                log.info("increase ITERATION_MAX  = " + ITERATION_MAX);
            }
            case VK_L -> {
                ITERATION_MAX *= 0.95;
                log.info("decrease ITERATION_MAX  = " + ITERATION_MAX);
            }
            case VK_M -> {
                ITERATION_min *= 1.05;
                log.info("increase ITERATION_min  = " + ITERATION_min);
            }
            case VK_N -> {
                ITERATION_min *= 0.95;
                log.info("decrease ITERATION_min  = " + ITERATION_min);
            }
        }
    }
}
