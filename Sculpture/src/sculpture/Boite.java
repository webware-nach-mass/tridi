package sculpture;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import tridi.base.SPoint;
import tridi.base.STransform;
import tridi.base.SVector;
import tridi.geom.Brick;
import tridi.geom.CompoundGeometry;
import tridi.render.Transformation;

/**
 * Une boîte
 */
public class Boite extends CompoundGeometry {

	public final static double PILIER=5.0;

	private final double dx,dy;
	public final double z0;
	public final double z1;
	public final ArrayList<Mur> murs=new ArrayList<Mur>();
	public final STransform t=new STransform();
	public final AffineTransform aff=new AffineTransform();
	public final Quadrilatere plan;
	public final ArrayList<Fond> fonds=new ArrayList<Fond>();
	private final boolean[] piliers= {true,true,true,true};
	public Boite(final double dx,final double dy,final double dz,final double xc,final double yc,final double zc,final double a,final int ouv,final boolean gauche) {
		this.dx=dx;
		this.dy=dy;
		z0=zc - dz;
		z1=zc + dz;
		t.rotate(2,gauche ? a : -a);
		t.translate(xc,gauche ? yc : -yc,0.0);
		aff.translate(xc,gauche ? yc : -yc);
		aff.rotate(gauche ? a : -a);
		plan=new Quadrilatere(-dx,-dy,2.0 * dx,2.0 * dy,aff);
		new Fond(this,plan.pts[0],plan.pts[1],plan.pts[2]);
		new Fond(this,plan.pts[0],plan.pts[2],plan.pts[3]);
		//		for(int i=0,j=1;i < 4;++i,j=(j + 1) % 4) {
		//			add(new Cable(new SPoint(plan.pts[i].getX(),plan.pts[i].getY(),z1 + 1.0),new SPoint(plan.pts[j].getX(),plan.pts[j].getY(),z1 + 1.0)));
		//		}
		//		add(new Transformation<Cage>(new Cage(new SPoint(-dx,-dy,zc - dz),new SVector(2.0 * dx,2.0 * dy,2.0 * dz)),t));
		if(ouv != 0) {
			//new Mur(this,-dx + 0.5 * Mur.EP,gauche ? -dy + 0.5 * Mur.EP : dy - 0.5 * Mur.EP,zc - dz,2.0 * dx - Mur.EP,0.0,2.0 * dz,t,aff);
			new Mur(this,-dx,gauche ? -dy + 0.5 * Mur.EP : dy - 0.5 * Mur.EP,zc - dz,2.0 * dx,0.0,2.0 * dz,t,aff);
		}
		if(ouv != 1) {
			//new Mur(this,-dx + 0.5 * Mur.EP,-dy + 0.5 * Mur.EP,zc - dz,0.0,2.0 * dy - Mur.EP,2.0 * dz,t,aff);
			new Mur(this,-dx + 0.5 * Mur.EP,-dy,zc - dz,0.0,2.0 * dy,2.0 * dz,t,aff);
		}
		if(ouv != 2) {
			//new Mur(this,-dx + 0.5 * Mur.EP,gauche ? dy - 0.5 * Mur.EP : -dy + 0.5 * Mur.EP,zc - dz,2.0 * dx - Mur.EP,0.0,2.0 * dz,t,aff);
			new Mur(this,-dx,gauche ? dy - 0.5 * Mur.EP : -dy + 0.5 * Mur.EP,zc - dz,2.0 * dx,0.0,2.0 * dz,t,aff);
		}
		if(ouv != 3) {
			//new Mur(this,dx - 0.5 * Mur.EP,-dy + 0.5 * Mur.EP,zc - dz,0.0,2.0 * dy - Mur.EP,2.0 * dz,t,aff);
			new Mur(this,dx - 0.5 * Mur.EP,-dy,zc - dz,0.0,2.0 * dy,2.0 * dz,t,aff);
		}
		//add(new Transformation<Brick>(new Brick(new SPoint(-dx,-dy,zc - dz),new SVector(2.0 * dx,2.0 * dy,Mur.EP)),t));
	}

	public void soustrais(final Boite b) {
		for(int i=murs.size() - 1;i >= 0;--i) {
			murs.get(i).soustrais(b);
		}
		if(z0 > b.z0 && z0 < b.z1) {
			soustraisFond(b);
		}
		if(z0 >= b.z0) {
			for(int i=0;i < 4;++i) {
				if(b.plan.contains(plan.pts[i])) {
					piliers[i]=false;
				}
			}
		}
	}
	public void soustraisFond(final Boite b) {
		for(int i=fonds.size() - 1;i >= 0;--i) {
			fonds.get(i).soustrais(b);
		}
	}
	public void makeSol(final double minz) {
		for(Fond f : fonds) {
			f.makeSol();
		}
		if(minz < z0) {
			if(piliers[0]) {
				add(new Transformation<Brick>(new Brick(new SPoint(-dx,-dy,minz),new SVector(PILIER,PILIER,z0 - minz)),t));
			}
			if(piliers[1]) {
				add(new Transformation<Brick>(new Brick(new SPoint(dx - PILIER,-dy,minz),new SVector(PILIER,PILIER,z0 - minz)),t));
			}
			if(piliers[2]) {
				add(new Transformation<Brick>(new Brick(new SPoint(dx - PILIER,dy - PILIER,minz),new SVector(PILIER,PILIER,z0 - minz)),t));
			}
			if(piliers[3]) {
				add(new Transformation<Brick>(new Brick(new SPoint(-dx,dy - PILIER,minz),new SVector(PILIER,PILIER,z0 - minz)),t));
			}
		}
	}
}
