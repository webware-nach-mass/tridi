package sculpture;

import java.awt.geom.Point2D;

import tridi.geom.FaceSet;

/**
 * Un triangle du fond d'une Boite.
 */
public class Fond {

	public final Boite boite;
	public final Point2D[] pts=new Point2D[3];
	public Fond(final Boite boite,final Point2D p0,final Point2D p1,final Point2D p2) {
		this.boite=boite;
		pts[0]=p0;
		pts[1]=p1;
		pts[2]=p2;
		boite.fonds.add(this);
	}

	public void soustrais(final Boite b) {
		soustrais(b,0,1);
	}
	private void soustrais(final Boite b,final int i,final int j) {
		boolean c0=estAGauche(pts[0],b.plan.pts[i],b.plan.pts[j]), //
		c1=estAGauche(pts[1],b.plan.pts[i],b.plan.pts[j]), //
		c2=estAGauche(pts[2],b.plan.pts[i],b.plan.pts[j]);
		if(c0 && c1 && c2) {
			//entièrement du bon côté de b.ij
			return;
		}
		if(!c0 && !c1 && !c2) {
			//entièrement du mauvais côté de b.ij
			if(i + 1 < 4) {
				soustrais(b,i + 1,(j + 1) % 4);
			} else {
				boite.fonds.remove(this);
			}
			return;
		}
		while(c0) {
			Point2D p=pts[0];
			pts[0]=pts[1];
			pts[1]=pts[2];
			pts[2]=p;
			boolean c=c0;
			c0=c1;
			c1=c2;
			c2=c;
		}
		if(c1 && !c2) {
			Point2D p=pts[0];
			pts[0]=pts[2];
			pts[2]=pts[1];
			pts[1]=p;
			boolean c=c0;
			c0=c2;
			c2=c1;
			c1=c;
		}
		if(!c1) {
			//c0 et c1 sont du mauvais côté de b.ij, c2 est du bon côté
			double l0=Sculpture.l(b.plan.pts[i],b.plan.pts[j],pts[0],pts[2]), //
			l1=Sculpture.l(b.plan.pts[i],b.plan.pts[j],pts[1],pts[2]);
			Point2D p0=new Point2D.Double(b.plan.pts[i].getX() + l0 * (b.plan.pts[j].getX() - b.plan.pts[i].getX()),b.plan.pts[i].getY() + l0 * (b.plan.pts[j].getY() - b.plan.pts[i].getY()));
			Point2D p1=new Point2D.Double(b.plan.pts[i].getX() + l1 * (b.plan.pts[j].getX() - b.plan.pts[i].getX()),b.plan.pts[i].getY() + l1 * (b.plan.pts[j].getY() - b.plan.pts[i].getY()));
			if(i + 1 < 4) {
				new Fond(boite,p0,p1,pts[2]); //celui-ci est entièrement du bon côté
				//pour le reste, on continue le tour
				new Fond(boite,pts[0],p1,p0).soustrais(b,i + 1,(j + 1) % 4);
				pts[2]=p1;
				soustrais(b,i + 1,(j + 1) % 4);
			} else {
				// le tour est fini, et seul le coin 2 est du bon côté de b.ij
				pts[0]=p0;
				pts[1]=p1;
			}
			return;
		}
		//c0 est du mauvais côté de b.ij, c1 et c2 sont du bon côté
		double l1=Sculpture.l(b.plan.pts[i],b.plan.pts[j],pts[0],pts[1]), //
				l2=Sculpture.l(b.plan.pts[i],b.plan.pts[j],pts[0],pts[2]);
		Point2D p1=new Point2D.Double(b.plan.pts[i].getX() + l1 * (b.plan.pts[j].getX() - b.plan.pts[i].getX()),b.plan.pts[i].getY() + l1 * (b.plan.pts[j].getY() - b.plan.pts[i].getY()));
		Point2D p2=new Point2D.Double(b.plan.pts[i].getX() + l2 * (b.plan.pts[j].getX() - b.plan.pts[i].getX()),b.plan.pts[i].getY() + l2 * (b.plan.pts[j].getY() - b.plan.pts[i].getY()));
		if(i + 1 < 4) {
			new Fond(boite,p2,p1,pts[1]); // celui-ci est entièrement du bon côté
			new Fond(boite,p2,pts[1],pts[2]); //celui-ci aussi
			//pour le reste, on continue le tour
			pts[2]=p2;
			pts[1]=p1;
			soustrais(b,i + 1,(j + 1) % 4);
		} else {
			new Fond(boite,p2,p1,pts[1]); // celui-ci est entièrement du bon côté
			pts[0]=p2; //et ce reste-là aussi.
		}
	}
	private static boolean estAGauche(final Point2D a,final Point2D b,final Point2D c) {
		return (c.getX() - b.getX()) * (a.getY() - b.getY()) - (c.getY() - b.getY()) * (a.getX() - b.getX()) < 0.0;
	}
	private FaceSet sol=null;
	public void makeSol() {
		sol=new FaceSet();
		sol.addNormal(0.0,0.0,-1.0);
		sol.addNormal(0.0,0.0,1.0);
		for(int i=0,j=1;i < 3;++i,j=(i + 1) % 3) {
			sol.addPoint(pts[i].getX(),pts[i].getY(),boite.z0);
			sol.addPoint(pts[i].getX(),pts[i].getY(),boite.z0 + Mur.EP);
			double dx=pts[j].getX() - pts[i].getX(),dy=pts[j].getY() - pts[i].getY();
			double n=Math.sqrt(dx * dx + dy * dy);
			sol.addNormal(dy / n,-dx / n,0.0);
		}
		sol.addTriangle(0,4,2,0,0,0);
		sol.addTriangle(1,3,5,1,1,1);
		sol.addQuadrangle(0,1,3,2,2,2,2,2);
		sol.addQuadrangle(2,3,5,4,3,3,3,3);
		sol.addQuadrangle(4,5,1,0,4,4,4,4);
		boite.add(sol);
	}
}
