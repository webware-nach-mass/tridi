package tridi.base;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A list of SPoints to use for extrusion.
 */
public class SPointList extends ArrayList<SPoint> {

	public static class Creator {
		public final SPointList points;
		public Creator(final SPointList points) {
			this.points=points;
			if(points.isEmpty()) {
				start=current=new SPoint();
			} else {
				start=points.get(0);
				current=points.get(points.size() - 1);
			}
		}
		public Creator() {
			points=new SPointList();
			start=current=new SPoint();
		}
		public Creator(final double x,final double y,final double z,final double tx,final double ty,final double tz) {
			super();
			points=new SPointList();
			start=current=new SPoint(x,y,z);
			points.add(start);
			tangent.set(tx,ty,tz);
			tangent.normalize();
		}
		public SPoint start;
		public SPoint current;
		public final SVector tangent=new SVector(1.0,0.0,0.0);
		public void moveTo(final double x,final double y,final double z,final double tx,final double ty,final double tz) {
			start=current=new SPoint(x,y,z);
			points.add(start);
			tangent.set(tx,ty,tz);
			tangent.normalize();
		}
		public void lineTo(final double x,final double y,final double z,final double tx,final double ty,final double tz) {
			current=new SPoint(x,y,z);
			points.add(current);
			tangent.set(tx,ty,tz);
			tangent.normalize();
		}
		public void addStraight(final double l) {
			SPoint np=new SPoint();
			np.setFrom(current);
			np.addInterpolate(tangent,l);
			points.add(np);
			current=np;
		}
		public void addKinkTo(final double x,final double y,final double z) {
			SPoint p=new SPoint(x,y,z);
			points.add(p);
			SVector.subtract(current,p,tangent);
			tangent.normalize();
			current=p;
		}
		public void addKink(final double dx,final double dy,final double dz) {
			SPoint p=new SPoint(dx,dy,dz);
			p.addInterpolate(current,1.0);
			points.add(p);
			current=p;
			tangent.set(dx,dy,dz);
			tangent.normalize();
		}
		public void addCurveTo(final double x1,final double y1,final double z1,final double x2,final double y2,final double z2,final double x3,final double y3,final double z3,final int steps) {
			double x0=current.coords[0],y0=current.coords[1],z0=current.coords[2];
			for(int i=1;i <= steps;++i) {
				double t=i / (double)steps,tt=1.0 - t;
				current=new SPoint(tt * tt * tt * x0 + 3.0 * tt * tt * t * x1 + 3.0 * tt * t * t * x2 + t * t * t * x3, //
						tt * tt * tt * y0 + 3.0 * tt * tt * t * y1 + 3.0 * tt * t * t * y2 + t * t * t * y3, //
						tt * tt * tt * z0 + 3.0 * tt * tt * t * z1 + 3.0 * tt * t * t * z2 + t * t * t * z3);
				points.add(current);
			}
			tangent.set(x3 - x2,y3 - y2,z3 - z2);
			if(!tangent.normalize()) {
				tangent.set(x3 - x1,y3 - y1,z3 - z1);
				if(!tangent.normalize()) {
					tangent.set(x3 - x0,y3 - y0,z3 - z0);
					tangent.normalize();
				}
			}
		}
		public void addCurve(final double dx1,final double dy1,final double dz1,final double dx2,final double dy2,final double dz2,final double dx3,final double dy3,final double dz3,final int steps) {
			addCurveTo(current.coords[0] + dx1,current.coords[1] + dy1,current.coords[2] + dz1,//
					current.coords[0] + dx2,current.coords[1] + dy2,current.coords[2] + dz2,//
					current.coords[0] + dx3,current.coords[1] + dy3,current.coords[2] + dz3,steps);
		}
		/**
		 * Adds a quarter of an ellipse.
		 */
		public void addEllipse(final double l0,final int axis,final double l1,final int steps) {
			SPoint p0=points.get(points.size() - 1);
			SVector t0=tangent;
			SPoint p1=new SPoint(),p2=new SPoint(),p3=new SPoint();
			p1.setFrom(p0);
			p1.addInterpolate(t0,0.6 * l0);
			SVector t3=new SVector();
			t3.coords[axis]=Math.signum(l1);
			p3.setFrom(p0);
			p3.addInterpolate(t0,l0);
			p3.addInterpolate(t3,Math.abs(l1));
			p2.setFrom(p3);
			p2.addInterpolate(t3,-0.6 * Math.abs(l1));
			addCurveTo(p1.coords[0],p1.coords[1],p1.coords[2],//
					p2.coords[0],p2.coords[1],p2.coords[2],//
					p3.coords[0],p3.coords[1],p3.coords[2],steps);
		}
		public void close() {
			if(!start.sameAs(current)) {
				points.add(start);
				SVector.subtract(current,start,tangent);
				tangent.normalize();
				current=start;
			}
		}
	}

	public SPointList() {
		super();
	}
	public SPointList(final Collection<? extends SPoint> c) {
		super(c);
	}
	public SPoint add(final double x,final double y,final double z) {
		SPoint result=new SPoint(x,y,z);
		add(result);
		return result;
	}
	/**
	 * Repeats the first point at the end.
	 */
	public void loop() {
		add(get(0));
	}

	public boolean containsXY(final double x,final double y) {
		SPoint vp=null,np=get(size() - 1);
		int cnt=0;
		for(SPoint p : this) {
			vp=np;
			np=p;
			if(y < vp.coords[1] && y <= np.coords[1]) {
				continue;
			}
			if(y > vp.coords[1] && y >= np.coords[1]) {
				continue;
			}
			if(x < vp.coords[0] && x <= np.coords[0]) {
				continue;
			}
			if(x > vp.coords[0] && x >= np.coords[0]) {
				++cnt;
				continue;
			}
			double px=vp.coords[0] + ((np.coords[0] - vp.coords[0]) * (y - vp.coords[1])) / (np.coords[1] - vp.coords[1]);
			if(x < px) {
				continue;
			}
			++cnt;
		}
		return cnt % 2 != 0;
	}
}
