package tridi.geom;

import java.util.ArrayList;

import tridi.base.Geometry;
import tridi.base.QuadrangleCoords;
import tridi.base.SPoint;
import tridi.base.SVector;
import tridi.base.TriangleCoords;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * A set of facets that share vertices and eventually normals.
 */
public class FaceSet implements Geometry {

	public final ArrayList<SPoint> points=new ArrayList<SPoint>();
	public final ArrayList<SVector> normals=new ArrayList<SVector>();
	public final ArrayList<TriangleCoords> triangles=new ArrayList<TriangleCoords>();
	public final ArrayList<QuadrangleCoords> quadrangles=new ArrayList<QuadrangleCoords>();

	public FaceSet() {
		super();
	}

	public void clear() {
		points.clear();
		triangles.clear();
		quadrangles.clear();
	}
	public void addPoint(final double x,final double y,final double z) {
		points.add(new SPoint(x,y,z));
	}
	public void addNormal(final double nx,final double ny,final double nz) {
		normals.add(new SVector(nx,ny,nz));
	}
	public void add(final double x,final double y,final double z, //
			final double nx,final double ny,final double nz) {
		points.add(new SPoint(x,y,z));
		normals.add(new SVector(nx,ny,nz));
	}
	/**
	 * Adds a triangle with its own normals.
	 */
	public TriangleCoords addTriangle(final int i0,final int i1,final int i2) {
		TriangleCoords result=makeTriangle(points.get(i0),new SVector(),points.get(i1),new SVector(),points.get(i2),new SVector(),false);
		triangles.add(result);
		return result;
	}
	/**
	 * Adds a triangle with shared normals. The normals are supposed OK.
	 */
	public void addTriangle(final int i0,final int i1,final int i2,final int in0,final int in1,final int in2) {
		triangles.add(makeTriangle(points.get(i0),normals.get(in0),points.get(i1),normals.get(in1),points.get(i2),normals.get(in2),true));
	}
	protected TriangleCoords makeTriangle(final SPoint p0,final SVector n0,final SPoint p1,final SVector n1,final SPoint p2,final SVector n2,final boolean normalsOK) {
		return new TriangleCoords(p0,n0,p1,n1,p2,n2,normalsOK);
	}
	/**
	 * Adds a quadrangle with its own normals.
	 */
	public QuadrangleCoords addQuadrangle(final int i0,final int i1,final int i2,final int i3) {
		QuadrangleCoords result=makeQuadrangle(points.get(i0),new SVector(),points.get(i1),new SVector(),points.get(i2),new SVector(),points.get(i3),new SVector(),false);
		quadrangles.add(result);
		return result;
	}
	/**
	 * Adds a quadrangle with shared normals. The normals are supposed OK.
	 */
	public void addQuadrangle(final int i0,final int i1,final int i2,final int i3,//
			final int in0,final int in1,final int in2,final int in3) {
		quadrangles.add(makeQuadrangle(points.get(i0),normals.get(in0),points.get(i1),normals.get(in1),points.get(i2),normals.get(in2),points.get(i3),normals.get(in3),true));
	}
	protected QuadrangleCoords makeQuadrangle(final SPoint p0,final SVector n0,final SPoint p1,final SVector n1,final SPoint p2,final SVector n2,final SPoint p3,final SVector n3,final boolean normalsOK) {
		return new QuadrangleCoords(p0,n0,p1,n1,p2,n2,p3,n3,normalsOK);
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	/**
	 * Just delegates normal calculation to the triangles and quadrangles.
	 * Eventual shared normals will be calculated by the last facet to which they belong.
	 */
	public void calcNormals() {
		for(TriangleCoords t : triangles) {
			t.calcNormals();
		}
		for(QuadrangleCoords q : quadrangles) {
			q.calcNormals();
		}
	}

	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		for(TriangleCoords t : triangles) {
			t.doRender(renderer);
		}
		for(QuadrangleCoords q : quadrangles) {
			q.doRender(renderer);
		}
		renderer.endObject();
	}

	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		renderer.startObject(id);
		for(TriangleCoords t : triangles) {
			t.doRenderObj(renderer);
		}
		for(QuadrangleCoords q : quadrangles) {
			q.doRenderObj(renderer);
		}
		renderer.endObject();
	}
}
