package tridi.geom;

import tridi.base.Geometry;
import tridi.base.SPoint;
import tridi.base.SVector;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A shifted brick.
 */
public class Parallelepiped implements Geometry {

	public SPoint corner;
	public SVector size;
	public final SVector[] shift;

	public Parallelepiped() {
		super();
		corner=new SPoint(0.0,0.0,0.0);
		size=new SVector(1.0,1.0,1.0);
		shift=new SVector[] {new SVector(0.0,0.0,1.0),null,null};
		calculate();
	}

	public Parallelepiped(final SPoint corner,final SVector size,final SVector shiftX,final SVector shiftY,final SVector shiftZ) {
		super();
		this.corner=corner;
		this.size=size;
		shift=new SVector[] {shiftX,shiftY,shiftZ};
		calculate();
	}

	@Override
	public void calculate() {
		for(int i=0;i < 2;++i) {
			for(int j=0;j < 2;++j) {
				for(int k=0;k < 2;++k) {
					verts[i][j][k].setFrom(corner);
				}
			}
		}
		for(int j=0;j < 2;++j) {
			for(int k=0;k < 2;++k) {
				verts[1][j][k].coords[0]+=size.coords[0];
			}
		}
		if(shift[0] != null) {
			for(int j=0;j < 2;++j) {
				for(int k=0;k < 2;++k) {
					verts[1][j][k].add(shift[0]);
				}
			}
		}
		for(int k=0;k < 2;++k) {
			for(int i=0;i < 2;++i) {
				verts[i][1][k].coords[1]+=size.coords[1];
			}
		}
		if(shift[1] != null) {
			for(int k=0;k < 2;++k) {
				for(int i=0;i < 2;++i) {
					verts[i][1][k].add(shift[1]);
				}
			}
		}
		for(int i=0;i < 2;++i) {
			for(int j=0;j < 2;++j) {
				verts[i][j][1].coords[2]+=size.coords[2];
			}
		}
		if(shift[2] != null) {
			for(int i=0;i < 2;++i) {
				for(int j=0;j < 2;++j) {
					verts[i][j][1].add(shift[2]);
				}
			}
		}
		SVector.vector(verts[0][0][0],verts[0][1][0],verts[0][0][1],norms[0][0]);
		SVector.vector(verts[0][0][0],verts[0][0][1],verts[1][0][0],norms[1][0]);
		SVector.vector(verts[0][0][0],verts[1][0][0],verts[0][1][0],norms[2][0]);
		for(int a=0;a < 3;++a) {
			norms[a][0].normalize();
			norms[a][1].addInterpolate(norms[a][0],-1.0);
			if(size.coords[a] > 0.0) {
				SVector v=norms[a][0];
				norms[a][0]=norms[a][1];
				norms[a][1]=v;
			}
		}
	}
	private final SPoint[][][] verts=new SPoint[][][] { { {new SPoint(),new SPoint()}, {new SPoint(),new SPoint()}}, { {new SPoint(),new SPoint()}, {new SPoint(),new SPoint()}}};
	private final SVector[][] norms=new SVector[][] { {new SVector(),new SVector()}, {new SVector(),new SVector()}, {new SVector(),new SVector()}};

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		//x==corner.x
		renderer.renderTriangle.points[0].setFrom(verts[0][0][0]);
		renderer.renderTriangle.points[1].setFrom(verts[0][1][1]);
		renderer.renderTriangle.points[2].setFrom(verts[0][1][0]);
		renderer.renderTriangle.setSameNormals(norms[0][0],true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].setFrom(verts[0][0][0]);
		renderer.renderTriangle.points[1].setFrom(verts[0][0][1]);
		renderer.renderTriangle.points[2].setFrom(verts[0][1][1]);
		renderer.renderTriangle.setSameNormals(norms[0][0],true);
		renderer.renderTriangle.renderAsDelegate();
		//x==corner.x+size.x
		renderer.renderTriangle.points[0].setFrom(verts[1][0][0]);
		renderer.renderTriangle.points[1].setFrom(verts[1][1][0]);
		renderer.renderTriangle.points[2].setFrom(verts[1][1][1]);
		renderer.renderTriangle.setSameNormals(norms[0][1],true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].setFrom(verts[1][0][0]);
		renderer.renderTriangle.points[1].setFrom(verts[1][1][1]);
		renderer.renderTriangle.points[2].setFrom(verts[1][0][1]);
		renderer.renderTriangle.setSameNormals(norms[0][1],true);
		renderer.renderTriangle.renderAsDelegate();
		//y==corner.y
		renderer.renderTriangle.points[0].setFrom(verts[0][0][0]);
		renderer.renderTriangle.points[1].setFrom(verts[1][0][0]);
		renderer.renderTriangle.points[2].setFrom(verts[1][0][1]);
		renderer.renderTriangle.setSameNormals(norms[1][0],true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].setFrom(verts[0][0][0]);
		renderer.renderTriangle.points[1].setFrom(verts[1][0][1]);
		renderer.renderTriangle.points[2].setFrom(verts[0][0][1]);
		renderer.renderTriangle.setSameNormals(norms[1][0],true);
		renderer.renderTriangle.renderAsDelegate();
		//y==corner.y+size.y
		renderer.renderTriangle.points[0].setFrom(verts[0][1][0]);
		renderer.renderTriangle.points[1].setFrom(verts[1][1][1]);
		renderer.renderTriangle.points[2].setFrom(verts[1][1][0]);
		renderer.renderTriangle.setSameNormals(norms[1][1],true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].setFrom(verts[0][1][0]);
		renderer.renderTriangle.points[1].setFrom(verts[0][1][1]);
		renderer.renderTriangle.points[2].setFrom(verts[1][1][1]);
		renderer.renderTriangle.setSameNormals(norms[1][1],true);
		renderer.renderTriangle.renderAsDelegate();
		//z==corner.z
		renderer.renderTriangle.points[0].setFrom(verts[0][0][0]);
		renderer.renderTriangle.points[1].setFrom(verts[1][1][0]);
		renderer.renderTriangle.points[2].setFrom(verts[1][0][0]);
		renderer.renderTriangle.setSameNormals(norms[2][0],true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].setFrom(verts[0][0][0]);
		renderer.renderTriangle.points[1].setFrom(verts[0][1][0]);
		renderer.renderTriangle.points[2].setFrom(verts[1][1][0]);
		renderer.renderTriangle.setSameNormals(norms[2][0],true);
		renderer.renderTriangle.renderAsDelegate();
		//z==corner.z+size.z
		renderer.renderTriangle.points[0].setFrom(verts[0][0][1]);
		renderer.renderTriangle.points[1].setFrom(verts[1][0][1]);
		renderer.renderTriangle.points[2].setFrom(verts[1][1][1]);
		renderer.renderTriangle.setSameNormals(norms[2][1],true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].setFrom(verts[0][0][1]);
		renderer.renderTriangle.points[1].setFrom(verts[1][1][1]);
		renderer.renderTriangle.points[2].setFrom(verts[0][1][1]);
		renderer.renderTriangle.setSameNormals(norms[2][1],true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.endObject();
	}
	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		renderer.startObject(id);
		//x==corner.x
		renderer.renderQuadrangle.points[0].setFrom(verts[0][0][0]);
		renderer.renderQuadrangle.points[1].setFrom(verts[0][1][0]);
		renderer.renderQuadrangle.points[2].setFrom(verts[0][1][1]);
		renderer.renderQuadrangle.points[3].setFrom(verts[0][0][1]);
		renderer.renderQuadrangle.setSameNormals(norms[0][0],true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//x==corner.x+size.x
		renderer.renderQuadrangle.points[0].setFrom(verts[1][0][0]);
		renderer.renderQuadrangle.points[1].setFrom(verts[1][0][1]);
		renderer.renderQuadrangle.points[2].setFrom(verts[1][1][1]);
		renderer.renderQuadrangle.points[3].setFrom(verts[1][1][0]);
		renderer.renderQuadrangle.setSameNormals(norms[0][1],true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//y==corner.y
		renderer.renderQuadrangle.points[0].setFrom(verts[0][0][0]);
		renderer.renderQuadrangle.points[1].setFrom(verts[0][0][1]);
		renderer.renderQuadrangle.points[2].setFrom(verts[1][0][1]);
		renderer.renderQuadrangle.points[3].setFrom(verts[1][0][0]);
		renderer.renderQuadrangle.setSameNormals(norms[1][0],true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//y==corner.y+size.y
		renderer.renderQuadrangle.points[0].setFrom(verts[0][1][0]);
		renderer.renderQuadrangle.points[1].setFrom(verts[1][1][0]);
		renderer.renderQuadrangle.points[2].setFrom(verts[1][1][1]);
		renderer.renderQuadrangle.points[3].setFrom(verts[0][1][1]);
		renderer.renderQuadrangle.setSameNormals(norms[1][1],true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//z==corner.z
		renderer.renderQuadrangle.points[0].setFrom(verts[0][0][0]);
		renderer.renderQuadrangle.points[1].setFrom(verts[1][0][0]);
		renderer.renderQuadrangle.points[2].setFrom(verts[1][1][0]);
		renderer.renderQuadrangle.points[3].setFrom(verts[0][1][0]);
		renderer.renderQuadrangle.setSameNormals(norms[2][0],true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//z==corner.z+size.z
		renderer.renderQuadrangle.points[0].setFrom(verts[0][0][1]);
		renderer.renderQuadrangle.points[1].setFrom(verts[0][1][1]);
		renderer.renderQuadrangle.points[2].setFrom(verts[1][1][1]);
		renderer.renderQuadrangle.points[3].setFrom(verts[1][0][1]);
		renderer.renderQuadrangle.setSameNormals(norms[2][1],true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		renderer.endObject();
	}
}
