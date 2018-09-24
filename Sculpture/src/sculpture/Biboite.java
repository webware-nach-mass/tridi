package sculpture;

import tridi.geom.CompoundGeometry;

/**
 * Une boîte symétrisée
 */
public class Biboite extends CompoundGeometry {

	public final static double MINTAILLE=15.0;
	public final static double MAXTAILLE=30.0;

	public final Boite b0,b1;

	public Biboite() {
		double dx=0.5 * Utiles.random(MINTAILLE,MAXTAILLE);
		double dy=0.5 * Utiles.random(MINTAILLE,MAXTAILLE);
		double dz=Mur.EP * Math.ceil(0.5 * Utiles.random(MINTAILLE,MAXTAILLE) / Mur.EP);

		double xc=Utiles.random(-Sculpture.MAXDIST + dx,Sculpture.MAXDIST - dx);
		double yc=Utiles.random(-Sculpture.MAXDIST + dy,Sculpture.MAXDIST - dy);
		double zc=Mur.EP * Math.round(Utiles.random(-Sculpture.MAXDIST + dz,Sculpture.MAXDIST - dz) / Mur.EP);

		double a=Utiles.random(-0.3,0.3) * Math.PI;
		int ouv=-1; //Utiles.random(4);

		b0=new Boite(dx,dy,dz,xc,yc,zc,a,ouv,true);
		add(b0);
		b1=new Boite(dx,dy,dz,xc,yc,zc,a,ouv,false);
		add(b1);
		b0.soustrais(b1);
		b1.soustrais(b0);
		b0.soustraisFond(b1);
	}

	public void soustrais(final Biboite bb) {
		b0.soustrais(bb.b0);
		b0.soustrais(bb.b1);
		b1.soustrais(bb.b0);
		b1.soustrais(bb.b1);
	}
	public void makeSol(final double minz) {
		b0.makeSol(minz);
		b1.makeSol(minz);
	}
}
