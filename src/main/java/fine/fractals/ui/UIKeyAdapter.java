package fine.fractals.ui;

import fine.fractals.Application;
import fine.fractals.ApplicationWindow;
import fine.fractals.Main;
import fine.fractals.color.PaletteBW;
import fine.fractals.engine.CalculationThread;
import fine.fractals.engine.FractalEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

            case KeyEvent.VK_S:
                FractalEngine.save();
                break;

            case KeyEvent.VK_C:
                /* Copy cursor location to clipboard and log */
                String targetString
                        = "public static double INIT_IMAGE_TARGET_reT = " + Application.ME.getTarget().getTextReT() + ";\n"
                        + "public static double TARGET_IM = " + Application.ME.getTarget().getTextImX() + ";\n"
                        + "public static double INITIAL_AREA_DOMAIN_SIZE = " + Application.ME.areaDomain.sizeImXString() + ";\n"
                        + "public static double INITIAL_AREA_IMAGE_SIZE = " + Application.ME.areaImage.sizeImXString() + ";";
                log.info(targetString);
                StringSelection stringSelection = new StringSelection(targetString);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                break;

            case KeyEvent.VK_M:
                // Design.toggleDoModulo();
                FractalEngine.ME.calculateFractalColoring();
                break;

            case KeyEvent.VK_NUMPAD0:
            case KeyEvent.VK_0:
                // Venture.colorPalette = new PaletteHue();
                FractalEngine.ME.calculateFractalColoring();
                break;

            case KeyEvent.VK_NUMPAD1:

            case KeyEvent.VK_NUMPAD2:
            case KeyEvent.VK_2:
                Main.colorPalette = new PaletteBW();
                FractalEngine.ME.calculateFractalColoring();
                break;

            case KeyEvent.VK_NUMPAD3:
            case KeyEvent.VK_3:

            case KeyEvent.VK_NUMPAD4:
            case KeyEvent.VK_4:
                break;

            case KeyEvent.VK_NUMPAD5:
            case KeyEvent.VK_5:

                FractalEngine.ME.calculateFractalColoring();
                break;

            case KeyEvent.VK_NUMPAD6:
            case KeyEvent.VK_6:

                FractalEngine.ME.calculateFractalColoring();
                break;

            case KeyEvent.VK_NUMPAD7:
            case KeyEvent.VK_7:

                FractalEngine.ME.calculateFractalColoring();
                break;

            case KeyEvent.VK_NUMPAD8:
            case KeyEvent.VK_8:

                FractalEngine.ME.calculateFractalColoring();
                break;

            case KeyEvent.VK_NUMPAD9:
            case KeyEvent.VK_9:
                // Venture.colorPalette = new PaletteDoubleSafe();
                FractalEngine.ME.calculateFractalColoring();
                break;

            case KeyEvent.VK_MULTIPLY:
                break;

            case KeyEvent.VK_I:
                ApplicationWindow.showInfo = !ApplicationWindow.showInfo;
                break;

            case KeyEvent.VK_Z:
                break;

            case KeyEvent.VK_L:

                break;
            case KeyEvent.VK_R:

                break;
            case KeyEvent.VK_B:

                break;
        }
    }

}
