package tridi.base;

import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * Base class for a triangular facet.
 */
public class TriangleCoords {
	public final SPoint[] points;
	public final SVector[] normals;

	public TriangleCoords() {
		super();
		points=new SPoint[] {new SPoint(),new SPoint(),new SPoint()};
		normals=new SVector[] {new SVector(),new SVector(),new SVector()};
		normalsOK=false;
	}
	public TriangleCoords(final SPoint p0,final SPoint p1,final SPoint p2) {
		super();
		points=new SPoint[] {p0,p1,p2};
		normals=new SVector[] {new SVector(),new SVector(),new SVector()};
		normalsOK=false;
	}
	public TriangleCoords(final SPoint p0,final SVector n0,final SPoint p1,final SVector n1,final SPoint p2,final SVector n2,final boolean normalsOK) {
		super();
		points=new SPoint[] {p0,p1,p2};
		normals=new SVector[] {n0,n1,n2};
		this.normalsOK=normalsOK;
	}
	public void setFrom(final TriangleCoords orig) {
		for(int i=0;i < 3;++i) {
			points[i].setFrom(orig.points[i]);
			normals[i].setFrom(orig.normals[i]);
		}
		normalsOK=orig.normalsOK;
	}

	public void setSameNormals(final double nx,final double ny,final double nz,final boolean normalsOK) {
		for(int i=0;i < 3;++i) {
			normals[i].set(nx,ny,nz);
		}
		this.normalsOK=normalsOK;
	}
	public void setSameNormals(final SVector n,final boolean normalsOK) {
		for(int i=0;i < 3;++i) {
			normals[i].setFrom(n);
		}
		this.normalsOK=normalsOK;
	}
	public boolean calcNormals() {
		SVector.vector(points[0],points[1],points[2],normals[0]);
		normalsOK=normals[0].normalize();
		if(normalsOK) {
			normals[1].setFrom(normals[0]);
			normals[2].setFrom(normals[0]);
		}
		return normalsOK;
	}
	public boolean calcNormalsAwayFrom(final SPoint center) {
		for(int i=0;i < 3;++i) {
			SVector.subtract(center,points[i],normals[i]);
		}
		return normalize();
	}
	public boolean normalize() {
		normalsOK=true;
		for(int i=0;i < 3;++i) {
			if(!normals[i].normalize()) {
				normalsOK=false;
			}
		}
		return normalsOK;
	}
	/**
	 * Eventually exchanges 2 points to ensure that calculated first normal will be in the present direction of the first normal.
	 */
	public void orientToFirstNormal() {
		double nx=normals[0].coords[0],ny=normals[0].coords[1],nz=normals[0].coords[2];
		SVector.vector(points[0],points[1],points[2],normals[0]);
		if(nx * normals[0].coords[0] + ny * normals[0].coords[1] + nz * normals[0].coords[2] < 0.0) {
			SPoint p=points[1];
			points[1]=points[2];
			points[2]=p;
		}
		normals[0].set(nx,ny,nz);
	}
	public boolean normalsOK=false;

	public void doRender(final TriangleRenderer renderer) {
		renderer.renderTriangle.setFrom(this);
		renderer.renderTriangle.renderAsDelegate();
	}
	public void doRenderObj(final ObjRenderer renderer) {
		renderer.renderTriangle.setFrom(this);
		renderer.renderTriangle.renderAsDelegate();
	}
}
