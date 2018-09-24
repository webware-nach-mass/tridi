package tridi.base;

import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * Base class for a quadrangular facet.
 */
public class QuadrangleCoords {
	public final SPoint[] points;
	public final SVector[] normals;

	public QuadrangleCoords() {
		super();
		points=new SPoint[] {new SPoint(),new SPoint(),new SPoint(),new SPoint()};
		normals=new SVector[] {new SVector(),new SVector(),new SVector(),new SVector()};
		normalsOK=false;
	}
	public QuadrangleCoords(final SPoint p0,final SPoint p1,final SPoint p2,final SPoint p3) {
		super();
		points=new SPoint[] {p0,p1,p2,p3};
		normals=new SVector[] {new SVector(),new SVector(),new SVector(),new SVector()};
		normalsOK=false;
	}
	public QuadrangleCoords(final SPoint p0,final SVector n0,final SPoint p1,final SVector n1,final SPoint p2,final SVector n2,final SPoint p3,final SVector n3,final boolean normalsOK) {
		super();
		points=new SPoint[] {p0,p1,p2,p3};
		normals=new SVector[] {n0,n1,n2,n3};
		this.normalsOK=normalsOK;
	}
	public void setFrom(final QuadrangleCoords orig) {
		for(int i=0;i < 4;++i) {
			points[i].setFrom(orig.points[i]);
			normals[i].setFrom(orig.normals[i]);
		}
		normalsOK=orig.normalsOK;
	}

	public void setSameNormals(final double nx,final double ny,final double nz,final boolean normalsOK) {
		for(int i=0;i < 4;++i) {
			normals[i].set(nx,ny,nz);
		}
		this.normalsOK=normalsOK;
	}
	public void setSameNormals(final SVector n,final boolean normalsOK) {
		for(int i=0;i < 4;++i) {
			normals[i].setFrom(n);
		}
		this.normalsOK=normalsOK;
	}
	/**
	 * Will set all normals from (p1-p0)x(p2-p0)
	 */
	public boolean calcNormals() {
		SVector.vector(points[0],points[1],points[2],normals[0]);
		normalsOK=normals[0].normalize();
		if(normalsOK) {
			normals[1].setFrom(normals[0]);
			normals[2].setFrom(normals[0]);
			normals[3].setFrom(normals[0]);
		}
		return normalsOK;
	}
	public boolean calcNormalsAwayFrom(final SPoint center) {
		for(int i=0;i < 4;++i) {
			SVector.subtract(center,points[i],normals[i]);
		}
		return normalize();
	}
	public boolean normalize() {
		normalsOK=true;
		for(int i=0;i < 4;++i) {
			if(!normals[i].normalize()) {
				normalsOK=false;
			}
		}
		return normalsOK;
	}
	/**
	 * Eventually exchanges p1 and p3 to ensure that calculated first normal (p1-p0)x(p3-p0) will be in the present direction of the first normal.
	 */
	public void orientToFirstNormal() {
		double nx=normals[0].coords[0],ny=normals[0].coords[1],nz=normals[0].coords[2];
		SVector.vector(points[0],points[1],points[3],normals[0]);
		if(nx * normals[0].coords[0] + ny * normals[0].coords[1] + nz * normals[0].coords[2] < 0.0) {
			SPoint p=points[1];
			points[1]=points[3];
			points[3]=p;
		}
		normals[0].set(nx,ny,nz);
	}
	public boolean normalsOK=false;

	public void doRender(final TriangleRenderer renderer) {
		for(int i=0;i < 3;++i) {
			renderer.renderTriangle.points[i].setFrom(points[i]);
			renderer.renderTriangle.normals[i].setFrom(normals[i]);
		}
		renderer.renderTriangle.normalsOK=normalsOK;
		renderer.renderTriangle.renderAsDelegate();
		for(int i=0;i < 3;++i) {
			renderer.renderTriangle.points[i].setFrom(points[(i + 2) % 4]);
			renderer.renderTriangle.normals[i].setFrom(normals[(i + 2) % 4]);
		}
		renderer.renderTriangle.normalsOK=normalsOK;
		renderer.renderTriangle.renderAsDelegate();
	}

	public void doRenderObj(final ObjRenderer renderer) {
		renderer.renderQuadrangle.setFrom(this);
		renderer.renderQuadrangle.renderObjAsDelegate();
	}
}
