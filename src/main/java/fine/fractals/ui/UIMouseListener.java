package fine.fractals.ui;

import fine.fractals.Application;
import fine.fractals.engine.CalculationThread;
import fine.fractals.engine.FractalEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UIMouseListener implements MouseListener {

	private static final Logger log = LogManager.getLogger(UIMouseListener.class);

	public UIMouseListener() {
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		if (!Application.REPEAT) {
			if (SwingUtilities.isRightMouseButton(me)) {
				log.info("Right click");
				if (!FractalEngine.calculationInProgress) {
					Application.ME.areaDomain.moveToCoordinates(Application.ME.getTarget());
					Application.ME.areaImage.moveToCoordinates(Application.ME.getTarget());
					Application.ME.zoomIn();
					CalculationThread.calculate(0);
				}
			} else {
				log.info("Left click, FIX, then use Enter");
				Application.ME.getEngine().fixDomainOnClick(Application.ME.getTarget());
			}
		} else {
			log.info("click skipped");
		}
	}

	@Override
	public void mousePressed(MouseEvent me) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
