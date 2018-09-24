package tridi.geom;

import tridi.base.Geometry;
import tridi.base.SPoint;
import tridi.base.SVector;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * An ellipsoid contained in a rectangular block.
 */
public class Ellipsoid implements Geometry {

	public SPoint corner;
	public SVector size;
	/**
	 * slices=0 means adaptative rendering
	 */
	public int slices;

	public Ellipsoid() {
		super();
		corner=new SPoint(0.0,0.0,0.0);
		size=new SVector(1.0,1.0,1.0);
		slices=12;
	}

	public Ellipsoid(final SPoint corner,final SVector size,final int slices) {
		super();
		this.corner=corner;
		this.size=size;
		this.slices=slices;
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	private final SPoint center=new SPoint();
	private final SVector radius=new SVector();
	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		center.setFrom(corner);
		center.addInterpolate(size,0.5);
		radius.set(Math.abs(0.5 * size.coords[0]),Math.abs(0.5 * size.coords[1]),Math.abs(0.5 * size.coords[2]));
		renderer.startObject(id);
		int n=slices;
		if(n == 0) {
			renderer.renderTriangle.points[0].setFrom(center);
			renderer.renderTriangle.points[1].setFrom(corner);
			if(renderer.transform != null) {
				renderer.transform.transform(renderer.renderTriangle.points[0],renderer.renderTriangle.points[0]);
				renderer.transform.transform(renderer.renderTriangle.points[1],renderer.renderTriangle.points[1]);
			}
			double scale=Math.min(Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[0])),//
					Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[1])));
			n=Math.max(6,(int)(Math.PI * (Math.abs(size.coords[0]) + Math.abs(size.coords[1]) + Math.abs(size.coords[2])) / 6.0 * scale)); //each facet should be about 2px wide
		}
		for(int i=1;i < n;++i) {
			double la0=i * Math.PI / n,la1=(i + 1) * Math.PI / n, //
					cla0=Math.cos(la0),sla0=Math.sin(la0),//
					z0=center.coords[2] + radius.coords[2] * cla0,//
					cla1=Math.cos(la1),sla1=Math.sin(la1), //
					z1=center.coords[2] + radius.coords[2] * cla1;
			for(int j=-n;j < n;++j) {
				double lo0=(j - 0.5) * Math.PI / n,lo1=j * Math.PI / n,lo2=(j + 0.5) * Math.PI / n,//
						clo0=Math.cos(lo0),slo0=Math.sin(lo0), //
						clo1=Math.cos(lo1),slo1=Math.sin(lo1), //
						clo2=Math.cos(lo2),slo2=Math.sin(lo2);
				renderer.renderTriangle.points[0].set(center.coords[0] + radius.coords[0] * sla0 * clo0,center.coords[1] + radius.coords[1] * sla0 * slo0,z0);
				renderer.renderTriangle.normals[0].set(sla0 * clo0,sla0 * slo0,cla0);
				renderer.renderTriangle.points[1].set(center.coords[0] + radius.coords[0] * sla1 * clo1,center.coords[1] + radius.coords[1] * sla1 * slo1,z1);
				renderer.renderTriangle.normals[1].set(sla1 * clo1,sla1 * slo1,cla1);
				renderer.renderTriangle.points[2].set(center.coords[0] + radius.coords[0] * sla0 * clo2,center.coords[1] + radius.coords[1] * sla0 * slo2,z0);
				renderer.renderTriangle.normals[2].set(sla0 * clo2,sla0 * slo2,cla0);
				renderer.renderTriangle.normalsOK=true;
				renderer.renderTriangle.renderAsDelegate();
			}
		}
		for(int i=1;i < n;++i) {
			double la0=(i - 1) * Math.PI / n,la1=i * Math.PI / n, //
			cla0=Math.cos(la0),sla0=Math.sin(la0),//
			z0=center.coords[2] + radius.coords[2] * cla0,//
			cla1=Math.cos(la1),sla1=Math.sin(la1), //
			z1=center.coords[2] + radius.coords[2] * cla1;
			for(int j=-n;j < n;++j) {
				double lo0=j * Math.PI / n,lo1=(j + 0.5) * Math.PI / n,lo2=(j + 1) * Math.PI / n,//
				clo0=Math.cos(lo0),slo0=Math.sin(lo0), //
				clo1=Math.cos(lo1),slo1=Math.sin(lo1), //
				clo2=Math.cos(lo2),slo2=Math.sin(lo2);
				renderer.renderTriangle.points[0].set(center.coords[0] + radius.coords[0] * sla1 * clo0,center.coords[1] + radius.coords[1] * sla1 * slo0,z1);
				renderer.renderTriangle.normals[0].set(sla1 * clo0,sla1 * slo0,cla1);
				renderer.renderTriangle.points[1].set(center.coords[0] + radius.coords[0] * sla0 * clo1,center.coords[1] + radius.coords[1] * sla0 * slo1,z0);
				renderer.renderTriangle.normals[1].set(sla0 * clo1,sla0 * slo1,cla0);
				renderer.renderTriangle.points[2].set(center.coords[0] + radius.coords[0] * sla1 * clo2,center.coords[1] + radius.coords[1] * sla1 * slo2,z1);
				renderer.renderTriangle.normals[2].set(sla1 * clo2,sla1 * slo2,cla1);
				renderer.renderTriangle.normalsOK=true;
				renderer.renderTriangle.renderAsDelegate();
			}
		}
		renderer.endObject();
	}

	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		center.setFrom(corner);
		center.addInterpolate(size,0.5);
		radius.set(Math.abs(0.5 * size.coords[0]),Math.abs(0.5 * size.coords[1]),Math.abs(0.5 * size.coords[2]));
		renderer.startObject(id);
		int n=slices;
		for(int i=1;i < n;++i) {
			double la0=i * Math.PI / n,la1=(i + 1) * Math.PI / n, //
					cla0=Math.cos(la0),sla0=Math.sin(la0),//
					z0=center.coords[2] + radius.coords[2] * cla0,//
					cla1=Math.cos(la1),sla1=Math.sin(la1), //
					z1=center.coords[2] + radius.coords[2] * cla1;
			for(int j=-n;j < n;++j) {
				double lo0=(j - 0.5) * Math.PI / n,lo1=j * Math.PI / n,lo2=(j + 0.5) * Math.PI / n,//
						clo0=Math.cos(lo0),slo0=Math.sin(lo0), //
						clo1=Math.cos(lo1),slo1=Math.sin(lo1), //
						clo2=Math.cos(lo2),slo2=Math.sin(lo2);
				renderer.renderTriangle.points[0].set(center.coords[0] + radius.coords[0] * sla0 * clo0,center.coords[1] + radius.coords[1] * sla0 * slo0,z0);
				renderer.renderTriangle.normals[0].set(sla0 * clo0,sla0 * slo0,cla0);
				renderer.renderTriangle.points[1].set(center.coords[0] + radius.coords[0] * sla1 * clo1,center.coords[1] + radius.coords[1] * sla1 * slo1,z1);
				renderer.renderTriangle.normals[1].set(sla1 * clo1,sla1 * slo1,cla1);
				renderer.renderTriangle.points[2].set(center.coords[0] + radius.coords[0] * sla0 * clo2,center.coords[1] + radius.coords[1] * sla0 * slo2,z0);
				renderer.renderTriangle.normals[2].set(sla0 * clo2,sla0 * slo2,cla0);
				renderer.renderTriangle.normalsOK=true;
				renderer.renderTriangle.renderAsDelegate();
			}
		}
		for(int i=1;i < n;++i) {
			double la0=(i - 1) * Math.PI / n,la1=i * Math.PI / n, //
			cla0=Math.cos(la0),sla0=Math.sin(la0),//
			z0=center.coords[2] + radius.coords[2] * cla0,//
			cla1=Math.cos(la1),sla1=Math.sin(la1), //
			z1=center.coords[2] + radius.coords[2] * cla1;
			for(int j=-n;j < n;++j) {
				double lo0=j * Math.PI / n,lo1=(j + 0.5) * Math.PI / n,lo2=(j + 1) * Math.PI / n,//
				clo0=Math.cos(lo0),slo0=Math.sin(lo0), //
				clo1=Math.cos(lo1),slo1=Math.sin(lo1), //
				clo2=Math.cos(lo2),slo2=Math.sin(lo2);
				renderer.renderTriangle.points[0].set(center.coords[0] + radius.coords[0] * sla1 * clo0,center.coords[1] + radius.coords[1] * sla1 * slo0,z1);
				renderer.renderTriangle.normals[0].set(sla1 * clo0,sla1 * slo0,cla1);
				renderer.renderTriangle.points[1].set(center.coords[0] + radius.coords[0] * sla0 * clo1,center.coords[1] + radius.coords[1] * sla0 * slo1,z0);
				renderer.renderTriangle.normals[1].set(sla0 * clo1,sla0 * slo1,cla0);
				renderer.renderTriangle.points[2].set(center.coords[0] + radius.coords[0] * sla1 * clo2,center.coords[1] + radius.coords[1] * sla1 * slo2,z1);
				renderer.renderTriangle.normals[2].set(sla1 * clo2,sla1 * slo2,cla1);
				renderer.renderTriangle.normalsOK=true;
				renderer.renderTriangle.renderAsDelegate();
			}
		}
		renderer.endObject();
	}
}
