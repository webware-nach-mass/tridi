package tridi.exp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import tridi.base.SPoint;

/**
 * An ObjWriter that flattens too low z values.
 */
public class FlatteningObjWriter extends ObjWriter {

	public final double minz;
	public FlatteningObjWriter(final Writer out,final double minz) {
		super(out);
		this.minz=minz;
	}

	public FlatteningObjWriter(final File f,final double minz) throws UnsupportedEncodingException,FileNotFoundException {
		super(f);
		this.minz=minz;
	}

	private final SPoint deleg=new SPoint();
	@Override
	protected String addVertex(final SPoint v) {
		deleg.set(v.coords[0],v.coords[1],Math.max(minz,v.coords[2]));
		return super.addVertex(deleg);
	}
}
