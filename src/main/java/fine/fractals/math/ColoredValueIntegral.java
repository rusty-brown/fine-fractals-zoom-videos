package fine.fractals.math;

import fine.fractals.Main;
import fine.fractals.data.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class ColoredValueIntegral {

	private static final Logger log = LogManager.getLogger(ColoredValueIntegral.class);

	private final HashMap<Integer, Pivot> valueIntegralWithColorLimits;

	private final LinkedList<Integer> designValues;

	private Integer currentlyColoredPivotValue = null;

	private int left;
	private Integer fullColoring;

	public void setFullColoring(int fullColoring) {
		this.fullColoring = fullColoring;
	}

	public void finalize(HashMap<Integer, Color> finalColors) {
		ArrayList<Integer> values = new ArrayList<>(this.valueIntegralWithColorLimits.keySet());

		for (Integer val : values) {
			Pivot p = this.valueIntegralWithColorLimits.get(val);

			double r = 0;
			double g = 0;
			double b = 0;
			double cAmount = 0;

			Color colorUsed;
			for (Coloring c : p.appliedColors) {
				colorUsed = Main.colorPalette.getSpectrumValue(c.colorIndex);
				r += c.amount * colorUsed.getRed();
				g += c.amount * colorUsed.getGreen();
				b += c.amount * colorUsed.getBlue();
				cAmount += c.amount;
			}
			finalColors.put(val, new Color((int) (r / cAmount), (int) (g / cAmount), (int) (b / cAmount)));
		}
	}

	private class Coloring {
		public int amount;

		public int colorIndex;

		public Coloring(int amount, int colorIndex) {
			this.amount = amount;
			this.colorIndex = colorIndex;
		}
	}

	private class Pivot {

		public int designValue;
		public int valuePixelCount;
		public ArrayList<Coloring> appliedColors = new ArrayList<>();

		public int needsToBePainted;

		public Pivot(int designValue, int valueAmount) {
			this.designValue = designValue;
			this.valuePixelCount = valueAmount;
			this.needsToBePainted = this.valuePixelCount;
		}

		public void increase() {
			this.valuePixelCount++;
			this.needsToBePainted = this.valuePixelCount;
		}

		public void paintMe(int colorIndex, int paintAmount) {
			this.appliedColors.add(new Coloring(paintAmount, colorIndex));
			this.needsToBePainted = this.needsToBePainted - paintAmount;
		}
	}

	/*
	 * Count how many of which value is there
	 */
	public ColoredValueIntegral(Screen screen) {

		this.valueIntegralWithColorLimits = new HashMap<>();

		// Initialize
		for (int x = 0; x < Main.RESOLUTION_IMAGE_HEIGHT; x++) {
			for (int t = 0; t < Main.RESOLUTION_IMAGE_WIDTH; t++) {
				int designValue = screen.colorFor(t, x);
				if (valueIntegralWithColorLimits.containsKey(designValue)) {
					valueIntegralWithColorLimits.get(designValue).increase();
				} else {
					valueIntegralWithColorLimits.put(designValue, new Pivot(designValue, 1));
				}
			}
		}

		this.designValues = new LinkedList<>(this.valueIntegralWithColorLimits.keySet());
		Collections.sort(designValues);

		log.info(" - All values to paint: " + designValues.size());
		log.info(" - smallest: " + designValues.get(0));
		log.info(" - largest:  " + designValues.get(designValues.size() - 1));
		log.info(" - All colors: " + Main.colorPalette.colorResolution());
	}


	private Integer getNextValueToPaint() {
		// TODO This should be perfect match
		if (designValues.isEmpty()) {
			return null;
		}
		final Integer ret = designValues.get(0);
		designValues.remove(0);
		return ret;
	}

	int usedPaintTotal = 0;

	public int applyPaint(int colorIndex, int paintAmount) {
		if (currentlyColoredPivotValue == null) {
			currentlyColoredPivotValue = getNextValueToPaint();
		}
		// Image pixel amount couldn't be divided precisely into the number of colors without reminder left */
		if (left > 0) {
			paintAmount += left;
			left = 0;
		}

		final Pivot pivot = this.valueIntegralWithColorLimits.get(currentlyColoredPivotValue);

		if (pivot == null) {
			// TODO
			verify();
		}

		final int colorLeft;
		if (pivot.needsToBePainted < paintAmount) {
			/*
				Pivot coloring will be finished
				This color will be left to color next pivot
			 */

			int exactPaintAmount = pivot.needsToBePainted;
			pivot.paintMe(colorIndex, exactPaintAmount);

			usedPaintTotal += exactPaintAmount;

			colorLeft = paintAmount - exactPaintAmount;

			// switch to next pivot
			currentlyColoredPivotValue = getNextValueToPaint();
		} else if (pivot.needsToBePainted > paintAmount) {
			/*
				Pivot will be colored only partially.
				No paint with this color will be left
			*/

			pivot.paintMe(colorIndex, paintAmount);

			usedPaintTotal += paintAmount;

			colorLeft = 0;
		} else {
			/*
				Pivot will be colored exactly.
				No paint with this color will be left
			*/
			pivot.paintMe(colorIndex, paintAmount);

			usedPaintTotal += paintAmount;

			colorLeft = 0;

			currentlyColoredPivotValue = getNextValueToPaint();
		}

		return colorLeft;
	}

	private void verify() {
		/* ========================================== */

		ArrayList<Integer> values = new ArrayList<>(this.valueIntegralWithColorLimits.keySet());
		Collections.sort(values);

		HashMap<Integer, Integer> paintUsed = new HashMap<>();

		for (Integer val : values) {
			Pivot p = this.valueIntegralWithColorLimits.get(val);
			int paintedBy = 0;
			for (Coloring c : p.appliedColors) {
				paintedBy += c.amount;
				if (paintUsed.containsKey(c.colorIndex)) {
					int amt = paintUsed.get(c.colorIndex);
					paintUsed.put(c.colorIndex, c.amount + amt);
				} else {
					paintUsed.put(c.colorIndex, c.amount);
				}
			}
			log.info(p.designValue + "(" + p.valuePixelCount + " = " + paintedBy + "), 0=" + p.needsToBePainted + ", " + p.appliedColors.size() + "{" + usedColors(p.appliedColors) + "}" + usedPaint(p.appliedColors));
			Assert.assertEquals(p.valuePixelCount, paintedBy);
			Assert.assertEquals(0, p.needsToBePainted);
		}
		for (int ind = 0; ind < paintUsed.size(); ind++) {
			log.info("colorR: " + ind + ":" + paintUsed.get(ind));
			Assert.assertEquals(paintUsed.get(ind), fullColoring);
		}
		Assert.assertEquals(paintUsed.size(), Main.colorPalette.colorResolution());
	}

	private String usedColors(ArrayList<Coloring> appliedColors) {
		String ret = "";
		for (Coloring c : appliedColors) {
			if (!"".equals(ret)) {
				ret += ",";
			}
			ret += c.colorIndex;
		}
		return ret;
	}

	private String usedPaint(ArrayList<Coloring> appliedColors) {
		String ret = "(";
		for (Coloring c : appliedColors) {
			if (!"(".equals(ret)) {
				ret += "-";
			}
			ret += c.amount;
		}
		return ret + ")";
	}

	public void setColorLeft(int left) {
		this.left = left;
	}
}
