package tridi.base;

import java.util.ArrayList;

/**
 * A list of SPoints carrying along a referential.
 */
public class SPath {

	public final SPointList points=new SPointList();
	public final ArrayList<SVector> tangents=new ArrayList<SVector>();
	public final ArrayList<SVector> normals=new ArrayList<SVector>();

	public SPath() {
		super();
	}

	/**
	 * Basic adding. Tangent and normal will ne normalized but it won't be checked that they are orthogonal.
	 */
	public void add(final double x,final double y,final double z,//
			final double tx,final double ty,final double tz, //
			final double nx,final double ny,final double nz) {
		points.add(new SPoint(x,y,z));
		SVector v=new SVector(tx,ty,tz);
		v.normalize();
		tangents.add(v);
		v=new SVector(nx,ny,nz);
		v.normalize();
		normals.add(v);
	}

	public void addStraight(final double l) {
		SPoint p=points.get(points.size() - 1);
		SVector t=tangents.get(tangents.size() - 1);
		SPoint np=new SPoint();
		np.setFrom(p);
		np.addInterpolate(t,l);
		points.add(np);
		tangents.add(t);
		normals.add(normals.get(normals.size() - 1));
	}

	public static enum StepKind {
		FOLLOW {
			@Override
			public SVector calcNormal(final SVector oldTangent,final SVector oldNormal,final SVector newTangent) {
				newTangent.normalize();
				//calculate new third vector as new tangent x old normal
				SVector n=new SVector();
				SVector.vector(newTangent.coords,oldNormal.coords,n.coords);
				n.normalize();
				//calculate new normal as new third vector x new tangent
				SVector.vector(n.coords,newTangent.coords,n.coords);
				n.normalize();
				return n;
			}
		},//
		KEEP_Z {
			@Override
			public SVector calcNormal(final SVector oldTangent,final SVector oldNormal,final SVector newTangent) {
				//keep z of tangent
				newTangent.coords[2]=oldTangent.coords[2];
				newTangent.normalize();
				//calculate rotation of tangent around z
				double a=Math.atan2(newTangent.coords[1],newTangent.coords[0]) - Math.atan2(oldTangent.coords[1],oldTangent.coords[0]);
				//apply same rotation to normal
				SVector result=new SVector(oldNormal.coords[0] * Math.cos(a) - oldNormal.coords[1] * Math.sin(a),//
						oldNormal.coords[0] * Math.sin(a) + oldNormal.coords[1] * Math.cos(a),//
						oldNormal.coords[2]);
				return result;
			}
		};

		abstract public SVector calcNormal(SVector oldTangent,final SVector oldNormal,final SVector newTangent);
	}

	public void addKinkTo(final double x,final double y,final double z,final StepKind stepKind) {
		SPoint p=points.get(points.size() - 1);
		//new tangent is along the new segment
		SVector t=new SVector(x - p.coords[0],y - p.coords[1],z - p.coords[2]);
		SVector n=stepKind.calcNormal(tangents.get(tangents.size() - 1),normals.get(normals.size() - 1),t);
		//change referential without changing location
		points.add(p);
		tangents.add(t);
		normals.add(n);
		//then change location without changing referential
		points.add(new SPoint(x,y,z));
		tangents.add(t);
		normals.add(n);
	}
	public void addKink(final double dx,final double dy,final double dz,final StepKind stepKind) {
		SPoint p=points.get(points.size() - 1);
		//new tangent is along the new segment
		SVector t=new SVector(dx,dy,dz);
		SVector n=stepKind.calcNormal(tangents.get(tangents.size() - 1),normals.get(normals.size() - 1),t);
		//change referential without changing location
		points.add(p);
		tangents.add(t);
		normals.add(n);
		//then change location without changing referential
		points.add(new SPoint(p.coords[0] + dx,p.coords[1] + dy,p.coords[2] + dz));
		tangents.add(t);
		normals.add(n);
	}

	public void addCurveTo(final double x1,final double y1,final double z1,final double x2,final double y2,final double z2,final double x3,final double y3,final double z3,final int steps,final StepKind stepKind) {
		SPoint p=points.get(points.size() - 1);
		double x0=p.coords[0],y0=p.coords[1],z0=p.coords[2];
		SVector oldt=tangents.get(tangents.size() - 1),oldn=normals.get(normals.size() - 1);
		for(int i=1;i <= steps;++i) {
			double t=i / (double)steps,tt=1.0 - t;
			points.add(tt * tt * tt * x0 + 3.0 * tt * tt * t * x1 + 3.0 * tt * t * t * x2 + t * t * t * x3, //
					tt * tt * tt * y0 + 3.0 * tt * tt * t * y1 + 3.0 * tt * t * t * y2 + t * t * t * y3, //
					tt * tt * tt * z0 + 3.0 * tt * tt * t * z1 + 3.0 * tt * t * t * z2 + t * t * t * z3);
			SVector newt=new SVector(-3.0 * tt * tt * x0 + 3.0 * (-2.0 * tt * t + tt * tt) * x1 + 3.0 * (-t * t + 2.0 * tt * t) * x2 + 3.0 * t * t * x3,//
					-3.0 * tt * tt * y0 + 3.0 * (-2.0 * tt * t + tt * tt) * y1 + 3.0 * (-t * t + 2.0 * tt * t) * y2 + 3.0 * t * t * y3,//
					-3.0 * tt * tt * z0 + 3.0 * (-2.0 * tt * t + tt * tt) * z1 + 3.0 * (-t * t + 2.0 * tt * t) * z2 + 3.0 * t * t * z3);
			SVector newn=stepKind.calcNormal(oldt,oldn,newt);
			tangents.add(newt);
			normals.add(newn);
			oldt=newt;
			oldn=newn;
		}
	}
	public void addCurve(final double dx1,final double dy1,final double dz1,final double dx2,final double dy2,final double dz2,final double dx3,final double dy3,final double dz3,final int steps,final StepKind stepKind) {
		SPoint p=points.get(points.size() - 1);
		addCurveTo(p.coords[0] + dx1,p.coords[1] + dy1,p.coords[2] + dz1,//
				p.coords[0] + dx2,p.coords[1] + dy2,p.coords[2] + dz2,//
				p.coords[0] + dx3,p.coords[1] + dy3,p.coords[2] + dz3,steps,stepKind);
	}
	/**
	 * Adds a quarter of an ellipse.
	 */
	public void addEllipse(final double l0,final int axis,final double l1,final int steps,final StepKind stepKind) {
		SPoint p0=points.get(points.size() - 1);
		SVector t0=tangents.get(tangents.size() - 1);
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
				p3.coords[0],p3.coords[1],p3.coords[2],steps,stepKind);
	}
	//
	//	public void copyFrom(final SPath l) {
	//		SPoint lvp=null,p=null;
	//		for(SPoint lnp : l.points) {
	//			if(lnp != lvp) {
	//				p=new SPoint();
	//				p.setFrom(lnp);
	//			}
	//			points.add(p);
	//			lvp=lnp;
	//		}
	//		SVector lvt=null,t=null;
	//		for(SVector lnt : l.tangents) {
	//			if(lnt != lvt) {
	//				t=new SVector();
	//				t.setFrom(lnt);
	//			}
	//			tangents.add(t);
	//			lvt=lnt;
	//		}
	//	}

}
