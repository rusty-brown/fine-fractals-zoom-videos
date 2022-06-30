package fine.fractals.data;

import fine.fractals.mandelbrot.MandelbrotMaskColors;
import fine.fractals.mandelbrot.MandelbrotState;

import java.awt.*;

public abstract class ElementAbstract {

	// TODO remove color
	protected Color color;
	protected int lastIteration;
	protected Integer value;
	protected Integer valueCorrected;
	protected MandelbrotState state = MandelbrotState.ActiveNew;

	public ElementAbstract() {
		this.lastIteration = 0;
		this.valueCorrected = 0;
		this.value = 0;
	}

	public boolean isHibernatedBlack() {
		return this.state == MandelbrotState.HibernatedBlack;
	}

	public void setHibernatedBlack() {
		this.state = MandelbrotState.HibernatedBlack;
		this.value = 0;
		this.valueCorrected = 0;
	}

	public void setHibernatedBlackNeighbour() {
		if (this.state != MandelbrotState.HibernatedBlack) {
			this.state = MandelbrotState.HibernatedBlackNeighbour;
			this.value = 0;
			this.valueCorrected = 0;
		}
	}

	public void resetForOptimization() {
		this.state = MandelbrotState.ActiveNew;
		this.color = null;
	}

	public boolean isHibernatedBlack_Neighbour() {
		return this.state == MandelbrotState.HibernatedBlackNeighbour;
	}

	public boolean isHibernatedFinished() {
		return this.state == MandelbrotState.HibernatedFinished;
	}

	/* Inside means that path of this divergent element had enough hits inside displayed area image */
	public boolean isHibernatedFinishedInside() {
		return this.state == MandelbrotState.HibernatedFinishedInside;
	}

	public int getLastIteration() {
		return this.lastIteration;
	}

	public void setLastIteration(int lastIteration) {
		this.lastIteration = lastIteration;
	}

	/* set both real and correct value */
	public void setValues(int value) {
		this.value = value;
		this.valueCorrected = value;
	}

	public void setValueCorrect(int value) {
		this.valueCorrected = value;
	}

	public Integer getValue() {
		return this.value;
	}

	public Integer getValueCorrected() {
		return this.valueCorrected;
	}

	public void setBlack() {
		this.value = 0;
		this.valueCorrected = 0;
	}

	public void setHibernatedFinished() {
		this.state = MandelbrotState.HibernatedFinished;
	}

	public void setHibernatedFinishedInside() {
		this.state = MandelbrotState.HibernatedFinishedInside;
	}

	public void setActiveMoved() {
		if (this.state == MandelbrotState.ActiveNew) {
			this.state = MandelbrotState.ActiveMoved;
		}
	}

	public void setActiveRecalculate() {
		if (this.state != MandelbrotState.HibernatedFinishedInside) {
			this.state = MandelbrotState.ActiveRecalculate;
			this.color = MandelbrotMaskColors.RECALCULATE;
		}
	}

	/* This should be called only for deep black on Optimization fix */
	public void setActiveFixed() {
		this.state = MandelbrotState.ActiveFixed;
	}

	public boolean isFixed() {
		return this.state == MandelbrotState.ActiveFixed;
	}

	public boolean isActiveNew() {
		return this.state == MandelbrotState.ActiveNew;
	}

	public boolean isActiveRecalculate() {
		return this.state == MandelbrotState.ActiveRecalculate;
	}

	public boolean isActiveMoved() {
		return this.state == MandelbrotState.ActiveMoved;
	}

	public boolean isActiveAny() {

		return this.state == MandelbrotState.ActiveNew || this.state == MandelbrotState.ActiveMoved || this.state == MandelbrotState.ActiveFixed;

	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	public MandelbrotState getState() {
		return this.state;
	}

}
