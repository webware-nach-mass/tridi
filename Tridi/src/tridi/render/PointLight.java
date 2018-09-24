package tridi.render;

import java.io.PrintWriter;

import tridi.base.SPoint;
import tridi.base.SVector;

/**
 * A point light.
 */
public class PointLight extends Light {
	public SPoint location;
	public PointLight(final SPoint location,final double r,final double g,final double b) {
		super(r,g,b);
		this.location=location;
	}
	public PointLight(final double x,final double y,final double z,final double r,final double g,final double b) {
		super(r,g,b);
		location=new SPoint(x,y,z);
	}

	@Override
	public void addColor(final SColor orig,final SPoint p,final SVector n,final SColor dest) {
		double intens=n.coords[0] * (location.coords[0] - p.coords[0]) + n.coords[1] * (location.coords[1] - p.coords[1]) + n.coords[2] * (location.coords[2] - p.coords[2]);
		if(intens > 0.0) {
			double dist=location.calcDist(p);
			if(dist < 1e-6) {
				intens=1.0;
			} else {
				intens/=dist;
			}
			dest.coords[0]+=intens * color.coords[0] * orig.coords[0];
			dest.coords[1]+=intens * color.coords[1] * orig.coords[1];
			dest.coords[2]+=intens * color.coords[2] * orig.coords[2];
		}
	}

	@Override
	public void exportPOV(final PrintWriter out) {
		//TODO PointLight.exportPOV
	}
}
