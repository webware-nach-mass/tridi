package tridi.geom;

import tridi.base.Geometry;
import tridi.base.SPoint;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A sector of a torus.
 */
public class TorusSector implements Geometry {

	public SPoint center;
	public double minorRadius;
	/**
	 * sectors=0 means adaptative rendering
	 */
	public int minorSectors;
	public double majorRadius;
	public double angleStart;
	public double angleExtent;
	/**
	 * sectors=0 means adaptative rendering
	 */
	public int majorSectors;

	public TorusSector() {
		center=new SPoint(0.0,0.0,0.0);
		minorRadius=0.1;
		minorSectors=24;
		majorRadius=1.0;
		angleStart=0.0;
		angleExtent=0.5 * Math.PI;
		majorSectors=12;
	}
	/**
	 * sectors=0 means adaptative rendering
	 */
	public TorusSector(final SPoint center,final double minorRadius,final int minorSectors,final double majorRadius,final double angleStart,final double angleExtent,final int majorSectors) {
		super();
		this.center=center;
		this.minorRadius=minorRadius;
		this.minorSectors=minorSectors;
		this.majorRadius=majorRadius;
		this.angleStart=angleStart;
		this.angleExtent=angleExtent;
		this.majorSectors=majorSectors;
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		int ni=majorSectors,nj=minorSectors;
		if(ni == 0 || nj == 0) {
			renderer.renderTriangle.points[0].setFrom(center);
			renderer.renderTriangle.points[1].set(center.coords[0] + majorRadius + minorRadius,center.coords[1] + majorRadius + minorRadius,center.coords[2] + minorRadius);
			if(renderer.transform != null) {
				renderer.transform.transform(renderer.renderTriangle.points[0],renderer.renderTriangle.points[0]);
				renderer.transform.transform(renderer.renderTriangle.points[1],renderer.renderTriangle.points[1]);
			}
			double scale=Math.min(Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[0])),//
					Math.abs(renderer.getScaleAt(renderer.renderTriangle.points[1])));
			if(ni == 0) {
				ni=Math.max(6,(int)(0.5 * angleExtent * majorRadius * scale)); //each facet should be about 2px wide
			}
			if(nj == 0) {
				nj=Math.max(6,(int)(Math.PI * minorRadius * scale)); //each facet should be about 2px wide
			}
		}
		for(int i=0;i < ni;++i) {
			double a0=angleStart + (i * angleExtent) / ni,a1=angleStart + ((i + 1) * angleExtent) / ni;
			double c0=Math.cos(a0),s0=Math.sin(a0);
			double c1=Math.cos(a1),s1=Math.sin(a1);
			for(int j=0;j < nj;++j) {
				double b0=-Math.PI + (j * 2.0 * Math.PI) / nj,b1=-Math.PI + ((j + 1) * 2.0 * Math.PI) / nj;
				double u0=Math.cos(b0),v0=Math.sin(b0);
				double u1=Math.cos(b1),v1=Math.sin(b1);
				renderer.renderTriangle.points[0].set(center.coords[0] + (majorRadius + minorRadius * u0) * c0,center.coords[1] + (majorRadius + minorRadius * u0) * s0,center.coords[2] + minorRadius * v0);
				renderer.renderTriangle.normals[0].set(u0 * c0,u0 * s0,v0);
				renderer.renderTriangle.points[1].set(center.coords[0] + (majorRadius + minorRadius * u1) * c0,center.coords[1] + (majorRadius + minorRadius * u1) * s0,center.coords[2] + minorRadius * v1);
				renderer.renderTriangle.normals[1].set(u1 * c0,u1 * s0,v1);
				renderer.renderTriangle.points[2].set(center.coords[0] + (majorRadius + minorRadius * u1) * c1,center.coords[1] + (majorRadius + minorRadius * u1) * s1,center.coords[2] + minorRadius * v1);
				renderer.renderTriangle.normals[2].set(u1 * c1,u1 * s1,v1);
				renderer.renderTriangle.normalsOK=true;
				renderer.renderTriangle.renderAsDelegate();
				renderer.renderTriangle.points[0].set(center.coords[0] + (majorRadius + minorRadius * u0) * c0,center.coords[1] + (majorRadius + minorRadius * u0) * s0,center.coords[2] + minorRadius * v0);
				renderer.renderTriangle.normals[0].set(u0 * c0,u0 * s0,v0);
				renderer.renderTriangle.points[1].set(center.coords[0] + (majorRadius + minorRadius * u1) * c1,center.coords[1] + (majorRadius + minorRadius * u1) * s1,center.coords[2] + minorRadius * v1);
				renderer.renderTriangle.normals[1].set(u1 * c1,u1 * s1,v1);
				renderer.renderTriangle.points[2].set(center.coords[0] + (majorRadius + minorRadius * u0) * c1,center.coords[1] + (majorRadius + minorRadius * u0) * s1,center.coords[2] + minorRadius * v0);
				renderer.renderTriangle.normals[2].set(u0 * c1,u0 * s1,v0);
				renderer.renderTriangle.normalsOK=true;
				renderer.renderTriangle.renderAsDelegate();
			}
		}
		double c0=Math.cos(angleStart),s0=Math.sin(angleStart);
		double c1=Math.cos(angleStart + angleExtent),s1=Math.sin(angleStart + angleExtent);
		for(int j=0;j < nj;++j) {
			double b0=-Math.PI + (j * 2.0 * Math.PI) / nj,b1=-Math.PI + ((j + 1) * 2.0 * Math.PI) / nj;
			double u0=Math.cos(b0),v0=Math.sin(b0);
			double u1=Math.cos(b1),v1=Math.sin(b1);
			renderer.renderTriangle.points[0].set(center.coords[0] + majorRadius * c0,center.coords[1] + majorRadius * s0,center.coords[2]);
			renderer.renderTriangle.points[1].set(center.coords[0] + (majorRadius + minorRadius * u0) * c0,center.coords[1] + (majorRadius + minorRadius * u0) * s0,center.coords[2] + minorRadius * v0);
			renderer.renderTriangle.points[2].set(center.coords[0] + (majorRadius + minorRadius * u1) * c0,center.coords[1] + (majorRadius + minorRadius * u1) * s0,center.coords[2] + minorRadius * v1);
			renderer.renderTriangle.setSameNormals(s0,-c0,0.0,true);
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderTriangle.points[0].set(center.coords[0] + majorRadius * c1,center.coords[1] + majorRadius * s1,center.coords[2]);
			renderer.renderTriangle.points[1].set(center.coords[0] + (majorRadius + minorRadius * u0) * c1,center.coords[1] + (majorRadius + minorRadius * u0) * s1,center.coords[2] + minorRadius * v0);
			renderer.renderTriangle.points[2].set(center.coords[0] + (majorRadius + minorRadius * u1) * c1,center.coords[1] + (majorRadius + minorRadius * u1) * s1,center.coords[2] + minorRadius * v1);
			renderer.renderTriangle.setSameNormals(-s1,c1,0.0,true);
			renderer.renderTriangle.renderAsDelegate();
		}
		renderer.endObject();
	}

	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		renderer.startObject(id);
		int ni=majorSectors,nj=minorSectors;
		for(int i=0;i < ni;++i) {
			double a0=angleStart + (i * angleExtent) / ni,a1=angleStart + ((i + 1) * angleExtent) / ni;
			double c0=Math.cos(a0),s0=Math.sin(a0);
			double c1=Math.cos(a1),s1=Math.sin(a1);
			for(int j=0;j < nj;++j) {
				double b0=-Math.PI + (j * 2.0 * Math.PI) / nj,b1=-Math.PI + ((j + 1) * 2.0 * Math.PI) / nj;
				double u0=Math.cos(b0),v0=Math.sin(b0);
				double u1=Math.cos(b1),v1=Math.sin(b1);
				renderer.renderQuadrangle.points[0].set(center.coords[0] + (majorRadius + minorRadius * u0) * c0,center.coords[1] + (majorRadius + minorRadius * u0) * s0,center.coords[2] + minorRadius * v0);
				renderer.renderQuadrangle.normals[0].set(u0 * c0,u0 * s0,v0);
				renderer.renderQuadrangle.points[1].set(center.coords[0] + (majorRadius + minorRadius * u1) * c0,center.coords[1] + (majorRadius + minorRadius * u1) * s0,center.coords[2] + minorRadius * v1);
				renderer.renderQuadrangle.normals[1].set(u1 * c0,u1 * s0,v1);
				renderer.renderQuadrangle.points[2].set(center.coords[0] + (majorRadius + minorRadius * u1) * c1,center.coords[1] + (majorRadius + minorRadius * u1) * s1,center.coords[2] + minorRadius * v1);
				renderer.renderQuadrangle.normals[2].set(u1 * c1,u1 * s1,v1);
				renderer.renderQuadrangle.points[3].set(center.coords[0] + (majorRadius + minorRadius * u0) * c1,center.coords[1] + (majorRadius + minorRadius * u0) * s1,center.coords[2] + minorRadius * v0);
				renderer.renderQuadrangle.normals[3].set(u0 * c1,u0 * s1,v0);
				renderer.renderQuadrangle.normalsOK=true;
				renderer.renderQuadrangle.renderObjAsDelegate();
			}
		}
		double c0=Math.cos(angleStart),s0=Math.sin(angleStart);
		double c1=Math.cos(angleStart + angleExtent),s1=Math.sin(angleStart + angleExtent);
		for(int j=0;j < nj;++j) {
			double b0=-Math.PI + (j * 2.0 * Math.PI) / nj,b1=-Math.PI + ((j + 1) * 2.0 * Math.PI) / nj;
			double u0=Math.cos(b0),v0=Math.sin(b0);
			double u1=Math.cos(b1),v1=Math.sin(b1);
			renderer.renderTriangle.points[0].set(center.coords[0] + majorRadius * c0,center.coords[1] + majorRadius * s0,center.coords[2]);
			renderer.renderTriangle.points[1].set(center.coords[0] + (majorRadius + minorRadius * u0) * c0,center.coords[1] + (majorRadius + minorRadius * u0) * s0,center.coords[2] + minorRadius * v0);
			renderer.renderTriangle.points[2].set(center.coords[0] + (majorRadius + minorRadius * u1) * c0,center.coords[1] + (majorRadius + minorRadius * u1) * s0,center.coords[2] + minorRadius * v1);
			renderer.renderTriangle.setSameNormals(s0,-c0,0.0,true);
			renderer.renderTriangle.renderAsDelegate();
			renderer.renderTriangle.points[0].set(center.coords[0] + majorRadius * c1,center.coords[1] + majorRadius * s1,center.coords[2]);
			renderer.renderTriangle.points[1].set(center.coords[0] + (majorRadius + minorRadius * u0) * c1,center.coords[1] + (majorRadius + minorRadius * u0) * s1,center.coords[2] + minorRadius * v0);
			renderer.renderTriangle.points[2].set(center.coords[0] + (majorRadius + minorRadius * u1) * c1,center.coords[1] + (majorRadius + minorRadius * u1) * s1,center.coords[2] + minorRadius * v1);
			renderer.renderTriangle.setSameNormals(-s1,c1,0.0,true);
			renderer.renderTriangle.renderAsDelegate();
		}
		renderer.endObject();
	}

}
