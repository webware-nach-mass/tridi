package tridi.geom;

import tridi.base.Geometry;
import tridi.base.SPoint;
import tridi.base.SVector;

/**
 * A rectangular block.
 */
abstract public class BrickCoords implements Geometry {

	public SPoint corner;
	public SVector size;

	public BrickCoords() {
		super();
		corner=new SPoint(0.0,0.0,0.0);
		size=new SVector(1.0,1.0,1.0);
	}

	public BrickCoords(final SPoint corner,final SVector size) {
		super();
		this.corner=corner;
		this.size=size;
	}

	public void set(final double x0,final double y0,final double z0,final double dx,final double dy,final double dz) {
		corner.set(x0,y0,z0);
		size.set(dx,dy,dz);
	}
	public void setFrom(final BrickCoords b) {
		corner.setFrom(b.corner);
		size.setFrom(b.size);
	}

	@Override
	public void calculate() {
		//nothing to do
	}
}
