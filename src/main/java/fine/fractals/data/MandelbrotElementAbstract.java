package fine.fractals.data;

import fine.fractals.data.mandelbrot.MandelbrotState;

import static fine.fractals.data.mandelbrot.MandelbrotState.*;

public abstract class MandelbrotElementAbstract {

	protected int value;

	protected MandelbrotState state = ActiveNew;

	public MandelbrotElementAbstract() {
		this.value = 0;
	}

	public boolean isHibernatedBlack() {
		return this.state == HibernatedBlack;
	}

	public void setHibernatedBlack() {
		this.state = HibernatedBlack;
		this.value = 0;
	}

	public void setHibernatedBlackNeighbour() {
		if (this.state != HibernatedBlack) {
			this.state = HibernatedBlackNeighbour;
			this.value = 0;
		}
	}

	@SuppressWarnings(value = "unused")
	public void resetForOptimization() {
		this.state = ActiveNew;
	}

	public boolean isHibernatedBlack_Neighbour() {
		return this.state == HibernatedBlackNeighbour;
	}

	public boolean isHibernatedFinished() {
		return this.state == HibernatedFinishedTooShort;
	}

	/* Inside means that path of this divergent element had enough hits inside displayed area image */
	public boolean isHibernatedFinishedLong() {
		return this.state == HibernatedFinishedLong;
	}

	/* set both real and correct value */
	public void setValues(int value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setHibernatedFinished() {
		this.state = HibernatedFinishedTooShort;
	}

	public void setHibernatedFinishedLong() {
		this.state = HibernatedFinishedLong;
	}

	public void setActiveMoved() {
		if (this.state == ActiveNew) {
			this.state = ActiveMoved;
		}
	}

	public void setActiveRecalculate() {
		if (this.state != HibernatedFinishedLong) {
			this.state = ActiveRecalculate;
		}
	}

	/* This should be called only for deep black on Optimization fix */
	public void setActiveFixed() {
		this.state = ActiveFixed;
	}

	public boolean isFixed() {
		return this.state == ActiveFixed;
	}

	public boolean isActiveNew() {
		return this.state == ActiveNew;
	}

	@SuppressWarnings(value = "unused")
	public boolean isActiveRecalculate() {
		return this.state == ActiveRecalculate;
	}

	@SuppressWarnings(value = "unused")
	public boolean isActiveMoved() {
		return this.state == ActiveMoved;
	}

	public boolean isActiveAny() {
		return this.state == ActiveNew || this.state == ActiveMoved || this.state == ActiveFixed;
	}

	public MandelbrotState getState() {
		return this.state;
	}
}
