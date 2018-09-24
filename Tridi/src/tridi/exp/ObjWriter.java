package tridi.exp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

import tridi.base.QuadrangleCoords;
import tridi.base.SPoint;
import tridi.base.SVector;
import tridi.base.TriangleCoords;
import tridi.geom.Brick;
import tridi.render.Transformation;

/**
 * Tool to export as .obj.
 */
public class ObjWriter {
	private final PrintWriter out;

	public ObjWriter(final Writer out) {
		this.out=new PrintWriter(out);
	}
	public ObjWriter(final File f) throws UnsupportedEncodingException,FileNotFoundException {
		out=new PrintWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
	}

	private int nextId=0;
	public void startObject(final String id) {
		out.print("o ");
		if(id == null) {
			out.print("Object.");
			out.println(nextId);
			++nextId;
		} else {
			out.println(id);
		}
	}

	private final MessageFormat vertexFormat=new MessageFormat("v {0,number,0.######} {1,number,0.######} {2,number,0.######}");
	private final Number[] coords=new Number[3];
	private final HashMap<String,String> vertices=new HashMap<String,String>();
	private int nextVertexId=1;
	protected String addVertex(final SPoint v) {
		coords[0]=new Double(v.coords[0]);
		coords[1]=new Double(v.coords[1]);
		coords[2]=new Double(v.coords[2]);
		String vs=vertexFormat.format(coords);
		String result=vertices.get(vs);
		if(result == null) {
			result=" " + nextVertexId;
			++nextVertexId;
			vertices.put(vs,result);
			out.println(vs);
		}
		return result;
	}
	private final ArrayList<String> faces=new ArrayList<String>();
	public void addTriangle(final TriangleCoords t) {
		faces.add("f" + addVertex(t.points[0]) + addVertex(t.points[1]) + addVertex(t.points[2]));
	}
	public void addQuadrangle(final QuadrangleCoords t) {
		faces.add("f" + addVertex(t.points[0]) + addVertex(t.points[1]) + addVertex(t.points[2]) + addVertex(t.points[3]));
	}
	public void endObject() {
		out.println("usemtl");
		out.println("s off");
		for(String f : faces) {
			out.println(f);
		}
		faces.clear();
		vertices.clear();
	}
	public void close() {
		out.close();
	}

	/**
	 * Test.
	 */
	public static void main(final String[] args) throws Throwable {
		Brick b=new Brick(new SPoint(0.0,0.0,0.0),new SVector(10.0,20.0,30.0));
		Transformation<Brick> t=new Transformation<Brick>(b);
		//t.transform.translate(10.0,20.0,30.0);
		t.transform.rotate(2,Math.PI / 3.0);
		ObjWriter out=new ObjWriter(new File("test/test.obj"));
		t.renderObj("O_0",new ObjRenderer(1.0,out));
		out.close();
		System.exit(0);
	}
}
