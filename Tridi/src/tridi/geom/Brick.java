package tridi.geom;

import tridi.base.SPoint;
import tridi.base.SVector;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A rectangular block with plain faces.
 */
public class Brick extends BrickCoords {

	public Brick() {
		super();
	}

	public Brick(final SPoint corner,final SVector size) {
		super(corner,size);
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		//x==corner.x
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderTriangle.setSameNormals(-Math.signum(size.coords[0]),0.0,0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.setSameNormals(-Math.signum(size.coords[0]),0.0,0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		//x==corner.x+size.x
		renderer.renderTriangle.points[0].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.setSameNormals(Math.signum(size.coords[0]),0.0,0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.setSameNormals(Math.signum(size.coords[0]),0.0,0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		//y==corner.y
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.setSameNormals(0.0,-Math.signum(size.coords[1]),0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.setSameNormals(0.0,-Math.signum(size.coords[1]),0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		//y==corner.y+size.y
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderTriangle.setSameNormals(0.0,Math.signum(size.coords[1]),0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.setSameNormals(0.0,Math.signum(size.coords[1]),0.0,true);
		renderer.renderTriangle.renderAsDelegate();
		//z==corner.z
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.setSameNormals(0.0,0.0,-Math.signum(size.coords[2]),true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderTriangle.setSameNormals(0.0,0.0,-Math.signum(size.coords[2]),true);
		renderer.renderTriangle.renderAsDelegate();
		//z==corner.z+size.z
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.setSameNormals(0.0,0.0,Math.signum(size.coords[2]),true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.renderTriangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.points[2].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderTriangle.setSameNormals(0.0,0.0,Math.signum(size.coords[2]),true);
		renderer.renderTriangle.renderAsDelegate();
		renderer.endObject();
	}
	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		renderer.startObject(id);
		//x==corner.x
		renderer.renderQuadrangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.points[1].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.points[2].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.points[3].set(corner.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.setSameNormals(-Math.signum(size.coords[0]),0.0,0.0,true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//x==corner.x+size.x
		renderer.renderQuadrangle.points[0].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.points[3].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.setSameNormals(Math.signum(size.coords[0]),0.0,0.0,true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//y==corner.y
		renderer.renderQuadrangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.points[1].set(corner.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.points[3].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.setSameNormals(0.0,-Math.signum(size.coords[1]),0.0,true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//y==corner.y+size.y
		renderer.renderQuadrangle.points[0].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.points[3].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.setSameNormals(0.0,Math.signum(size.coords[1]),0.0,true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//z==corner.z
		renderer.renderQuadrangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.points[1].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.points[3].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2]);
		renderer.renderQuadrangle.setSameNormals(0.0,0.0,-Math.signum(size.coords[2]),true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		//z==corner.z+size.z
		renderer.renderQuadrangle.points[0].set(corner.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.points[1].set(corner.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.points[2].set(corner.coords[0] + size.coords[0],corner.coords[1] + size.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.points[3].set(corner.coords[0] + size.coords[0],corner.coords[1],corner.coords[2] + size.coords[2]);
		renderer.renderQuadrangle.setSameNormals(0.0,0.0,Math.signum(size.coords[2]),true);
		renderer.renderQuadrangle.renderObjAsDelegate();
		renderer.endObject();
	}
}
