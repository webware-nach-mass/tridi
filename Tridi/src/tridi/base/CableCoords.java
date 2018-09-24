package tridi.base;


/**
 * Base class for a line.
 */
abstract public class CableCoords {
	public final SPoint[] points;
	public final SVector[] normals;

	public CableCoords() {
		super();
		points=new SPoint[] {new SPoint(),new SPoint()};
		normals=new SVector[] {new SVector(),new SVector()};
	}
	public CableCoords(final SPoint p0,final SPoint p1) {
		super();
		points=new SPoint[] {p0,p1};
		normals=new SVector[] {new SVector(),new SVector()};
	}
	public CableCoords(final SPoint p0,final SVector n0,final SPoint p1,final SVector n1) {
		super();
		points=new SPoint[] {p0,p1};
		normals=new SVector[] {n0,n1};
	}
	public void setFrom(final CableCoords orig) {
		for(int i=0;i < 2;++i) {
			points[i].setFrom(orig.points[i]);
			normals[i].setFrom(orig.normals[i]);
		}
		normalsOK=orig.normalsOK;
	}

	public void setSameNormals(final double nx,final double ny,final double nz,final boolean normalsOK) {
		for(int i=0;i < 2;++i) {
			normals[i].set(nx,ny,nz);
		}
		this.normalsOK=normalsOK;
	}
	public void calcNormals() {
		SVector.subtract(points[1],points[0],normals[0]);
		normalsOK=normals[0].normalize();
		if(normalsOK) {
			normals[1].set(-normals[0].coords[0],-normals[0].coords[1],-normals[0].coords[2]);
		}
	}
	public void normalize() {
		normalsOK=true;
		for(int i=0;i < 2;++i) {
			normalsOK&=normals[i].normalize();
		}
	}
	/**
	 * false means: ignore normals and don't render light (use 1 color)
	 */
	public boolean normalsOK=false;
}
