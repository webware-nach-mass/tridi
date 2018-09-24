package tridi.geom;

import tridi.base.Geometry;
import tridi.base.SPoint;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A half cylinder.
 */
public class HalfCylinder implements Geometry {

	public SPoint baseCenter;
	public double radius;
	/**
	 * sectors=0 means adaptative rendering
	 */
	public int sectors;
	public double length;

	public HalfCylinder() {
		super();
		baseCenter=new SPoint(0.0,0.0,0.0);
		radius=0.1;
		sectors=12;
		length=1.0;
	}
	/**
	 * sectors=0 means adaptative rendering
	 */
	public HalfCylinder(final SPoint baseCenter,final double radius,final int sectors,final double length) {
		super();
		this.baseCenter=baseCenter;
		this.radius=radius;
		this.sectors=sectors;
		this.length=length;
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		int n=sectors;
		if(n == 0) {
			renderer.renderTriangle.points[0].setFrom(baseCenter);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0],baseCenter.coords[1],baseCenter.coords[2] + length);
			if(renderer.transform != null) {
				renderer.transform.transform(renderer.renderTriangle.points[0],renderer.renderTriangle.points[0]);
				renderer.transform.transform(renderer.renderTriangle.points[1],renderer.renderTriangle.points[1]);
			}
			double scale=Math.min(Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[0])),//
					Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[1])));
			n=Math.max(6,(int)(0.5 * Math.PI * radius * scale)); //each facet should be about 2px wide
		}
		for(int i=0;i < n;++i) {
			double a0=-0.5 * Math.PI + i * Math.PI / n,a1=-0.5 * Math.PI + (i + 1) * Math.PI / n, //
			c0=Math.cos(a0),s0=Math.sin(a0), //
			c1=Math.cos(a1),s1=Math.sin(a1), //
			dx0=radius * c0,dy0=radius * s0, //
			dx1=radius * c1,dy1=radius * s1;
			renderer.renderTriangle.points[0].setFrom(baseCenter);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx0,baseCenter.coords[1] + dy0,baseCenter.coords[2]);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx1,baseCenter.coords[1] + dy1,baseCenter.coords[2]);
			renderer.renderTriangle.setSameNormals(0.0,0.0,-1.0,true);
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderTriangle.points[0].set(baseCenter.coords[0] + dx1,baseCenter.coords[1] + dy1,baseCenter.coords[2]);
			renderer.renderTriangle.normals[0].set(c1,s1,0.0);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx0,baseCenter.coords[1] + dy0,baseCenter.coords[2]);
			renderer.renderTriangle.normals[1].set(c0,s0,0.0);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx0,baseCenter.coords[1] + dy0,baseCenter.coords[2] + length);
			renderer.renderTriangle.normals[2].set(c0,s0,0.0);
			renderer.renderTriangle.normalsOK=true;
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderTriangle.points[0].set(baseCenter.coords[0] + dx1,baseCenter.coords[1] + dy1,baseCenter.coords[2]);
			renderer.renderTriangle.normals[0].set(c1,s1,0.0);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx0,baseCenter.coords[1] + dy0,baseCenter.coords[2] + length);
			renderer.renderTriangle.normals[1].set(c0,s0,0.0);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx1,baseCenter.coords[1] + dy1,baseCenter.coords[2] + length);
			renderer.renderTriangle.normals[2].set(c1,s1,0.0);
			renderer.renderTriangle.normalsOK=true;
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderTriangle.points[0].set(baseCenter.coords[0],baseCenter.coords[1],baseCenter.coords[2] + length);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx1,baseCenter.coords[1] + dy1,baseCenter.coords[2] + length);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx0,baseCenter.coords[1] + dy0,baseCenter.coords[2] + length);
			renderer.renderTriangle.setSameNormals(0.0,0.0,1.0,true);
			renderer.renderTriangle.renderAsDelegate();
		}
		renderer.renderTriangle.points[0].set(baseCenter.coords[0],baseCenter.coords[1] - radius,baseCenter.coords[2]);
		renderer.renderTriangle.points[1].set(baseCenter.coords[0],baseCenter.coords[1] + radius,baseCenter.coords[2]);
		renderer.renderTriangle.points[2].set(baseCenter.coords[0],baseCenter.coords[1] + radius,baseCenter.coords[2] + length);
		renderer.renderTriangle.setSameNormals(-1.0,0.0,0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].set(baseCenter.coords[0],baseCenter.coords[1] - radius,baseCenter.coords[2]);
		renderer.renderTriangle.points[1].set(baseCenter.coords[0],baseCenter.coords[1] + radius,baseCenter.coords[2] + length);
		renderer.renderTriangle.points[2].set(baseCenter.coords[0],baseCenter.coords[1] - radius,baseCenter.coords[2] + length);
		renderer.renderTriangle.setSameNormals(-1.0,0.0,0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.endObject();
	}
	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		renderer.startObject(id);
		int n=sectors;
		for(int i=0;i < n;++i) {
			double a0=-0.5 * Math.PI + i * Math.PI / n,a1=-0.5 * Math.PI + (i + 1) * Math.PI / n, //
			c0=Math.cos(a0),s0=Math.sin(a0), //
			c1=Math.cos(a1),s1=Math.sin(a1), //
			dx0=radius * c0,dy0=radius * s0, //
			dx1=radius * c1,dy1=radius * s1;
			renderer.renderTriangle.points[0].setFrom(baseCenter);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx0,baseCenter.coords[1] + dy0,baseCenter.coords[2]);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx1,baseCenter.coords[1] + dy1,baseCenter.coords[2]);
			renderer.renderTriangle.setSameNormals(0.0,0.0,-1.0,true);
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderQuadrangle.points[0].set(baseCenter.coords[0] + dx1,baseCenter.coords[1] + dy1,baseCenter.coords[2]);
			renderer.renderQuadrangle.normals[0].set(c1,s1,0.0);
			renderer.renderQuadrangle.points[1].set(baseCenter.coords[0] + dx0,baseCenter.coords[1] + dy0,baseCenter.coords[2]);
			renderer.renderQuadrangle.normals[1].set(c0,s0,0.0);
			renderer.renderQuadrangle.points[2].set(baseCenter.coords[0] + dx0,baseCenter.coords[1] + dy0,baseCenter.coords[2] + length);
			renderer.renderQuadrangle.normals[2].set(c0,s0,0.0);
			renderer.renderQuadrangle.points[3].set(baseCenter.coords[0] + dx1,baseCenter.coords[1] + dy1,baseCenter.coords[2] + length);
			renderer.renderQuadrangle.normals[3].set(c1,s1,0.0);
			renderer.renderQuadrangle.normalsOK=true;
			renderer.renderQuadrangle.renderObjAsDelegate();
			renderer.renderTriangle.points[0].set(baseCenter.coords[0],baseCenter.coords[1],baseCenter.coords[2] + length);
			renderer.renderTriangle.points[1].set(baseCenter.coords[0] + dx1,baseCenter.coords[1] + dy1,baseCenter.coords[2] + length);
			renderer.renderTriangle.points[2].set(baseCenter.coords[0] + dx0,baseCenter.coords[1] + dy0,baseCenter.coords[2] + length);
			renderer.renderTriangle.setSameNormals(0.0,0.0,1.0,true);
			renderer.renderTriangle.renderAsDelegate();
		}
		renderer.renderQuadrangle.points[0].set(baseCenter.coords[0],baseCenter.coords[1] - radius,baseCenter.coords[2]);
		renderer.renderQuadrangle.points[1].set(baseCenter.coords[0],baseCenter.coords[1] + radius,baseCenter.coords[2]);
		renderer.renderQuadrangle.points[1].set(baseCenter.coords[0],baseCenter.coords[1] + radius,baseCenter.coords[2] + length);
		renderer.renderQuadrangle.points[2].set(baseCenter.coords[0],baseCenter.coords[1] - radius,baseCenter.coords[2] + length);
		renderer.renderQuadrangle.setSameNormals(-1.0,0.0,0.0,true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		renderer.endObject();
	}
}
