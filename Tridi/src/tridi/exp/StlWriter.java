package tridi.exp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import tridi.base.TriangleCoords;

/**
 * Tool to export as (ascii) .stl.
 */
public class StlWriter {

	private final PrintWriter out;
	private final String id;

	public StlWriter(final Writer out,final String id) {
		this.out=new PrintWriter(out);
		this.out.print("solid ");
		this.out.println(id);
		this.id=id;
	}
	@SuppressWarnings("resource")
	public StlWriter(final File f,final String id) throws IOException {
		this(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"),id);
	}

	//	DecimalFormat format=new DecimalFormat("#0.000000");
	private void print(final double d) {
		out.print(' ');
		out.print(Math.round(d * 1000.0) / 1000.0);
	}

	public void startObject(final String id) {
		//nothing to do, there can be only 1 object
	}
	public void addTriangle(final TriangleCoords t) {
		out.print("facet normal");
		for(int i=0;i < 3;++i) {
			print(t.normals[0].coords[i]);
		}
		out.println();
		out.println("outer loop");
		for(int i=0;i < 3;++i) {
			out.print("vertex");
			for(int a=0;a < 3;++a) {
				print(t.points[i].coords[a]);
			}
			out.println();
		}
		out.println("endloop");
		out.println("endfacet");
	}
	public void endObject() {
		//nothing to do, there can be only 1 object
	}
	public void close() {
		out.print("endsolid ");
		out.println(id);
		out.close();
	}
}
