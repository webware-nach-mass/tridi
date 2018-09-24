package sculpture;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import tridi.base.STransform;
import tridi.exp.ObjRenderer;
import tridi.exp.ObjWriter;
import tridi.geom.CompoundGeometry;

/**
 * Une sculpture
 */
public class Sculpture extends CompoundGeometry {

	public final static double MAXDIST=35.0;

	private final ArrayList<Biboite> bbs=new ArrayList<Biboite>();
	public Sculpture() {
		super();
		double minz=Double.MAX_VALUE;
		for(int i=0;i < 5;++i) {
			Biboite nbb=new Biboite();
			for(Biboite vbb : bbs) {
				vbb.soustrais(nbb);
				nbb.soustrais(vbb);
			}
			bbs.add(nbb);
			add(nbb);
			if(nbb.b0.z0 < minz) {
				minz=nbb.b0.z0;
			}
		}
		for(Biboite bb : bbs) {
			bb.makeSol(minz);
		}
	}

	private final static STransform sym3d=new STransform();
	static {
		sym3d.scale(-1.0,1.0,1.0);
	}

	public void genere(final File f) throws IOException {
		ObjWriter out=new ObjWriter(f);
		renderObj("sculpture",new ObjRenderer(1.0,out));
		out.close();
	}

	public static double l(final Point2D a,final Point2D b,final Point2D c,final Point2D d) {
		double det=(b.getX() - a.getX()) * (c.getY() - d.getY()) - (b.getY() - a.getY()) * (c.getX() - d.getX());
		if(Math.abs(det) < 1e-6) {
			return Double.NaN;
		}
		double m=((b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX())) / det;
		if(m < 0.0 || m > 1.0) {
			return Double.NaN;
		}
		return ((c.getX() - a.getX()) * (c.getY() - d.getY()) - (c.getY() - a.getY()) * (c.getX() - d.getX())) / det;
	}
}
