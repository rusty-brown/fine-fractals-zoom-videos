package fine.fractals.ui;

import fine.fractals.Application;
import fine.fractals.engine.FractalEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UIRefreshThread extends Thread {

	private static final Logger log = LogManager.getLogger(UIRefreshThread.class);

	private static final int INTERVAL = 500;

	private static boolean running = false;
	private static int index = 0;
	private int myIndex;

	private UIRefreshThread() {
		this.myIndex = UIRefreshThread.index++;
	}

	public static void runRefreshThread() {
		if (!running) {
			new UIRefreshThread().start();
		} else {
			log.info("don't start new refresh thread (" + index + ")");
		}
	}

	public void run() {
		running = true;
		log.info("ui: (" + this.myIndex + ") refresh START");
		try {
			synchronized (this) {
				wait(INTERVAL);
				while (FractalEngine.calculationInProgress) {
					// log.info("ui: " + this.myIndex + " refresh");
					System.out.print(".");
					Application.ME.repaintMandelbrot();
					wait(INTERVAL);
				}
				log.info("ui: " + this.myIndex + " refresh END");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		running = false;
	}

}
