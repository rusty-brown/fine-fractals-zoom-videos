package fine.fractals.data;

import fine.fractals.mandelbrot.MandelbrotState;

import static fine.fractals.mandelbrot.MandelbrotState.*;

public abstract class MandelbrotElementAbstract {

	protected int lastIteration;
	protected int value;

	protected MandelbrotState state = ActiveNew;

	public MandelbrotElementAbstract() {
		this.lastIteration = 0;
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

	public void resetForOptimization() {
		this.state = ActiveNew;
	}

	public boolean isHibernatedBlack_Neighbour() {
		return this.state == HibernatedBlackNeighbour;
	}

	public boolean isHibernatedFinished() {
		return this.state == HibernatedFinished;
	}

	/* Inside means that path of this divergent element had enough hits inside displayed area image */
	public boolean isHibernatedFinishedInside() {
		return this.state == HibernatedFinishedInside;
	}

	/* set both real and correct value */
	public void setValues(int value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setHibernatedFinished() {
		this.state = HibernatedFinished;
	}

	public void setHibernatedFinishedInside() {
		this.state = HibernatedFinishedInside;
	}

	public void setActiveMoved() {
		if (this.state == ActiveNew) {
			this.state = ActiveMoved;
		}
	}

	public void setActiveRecalculate() {
		if (this.state != HibernatedFinishedInside) {
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

	public boolean isActiveRecalculate() {
		return this.state == ActiveRecalculate;
	}

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
