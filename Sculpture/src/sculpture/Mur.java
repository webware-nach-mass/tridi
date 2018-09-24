package sculpture;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import tridi.base.STransform;
import tridi.geom.Brick;
import tridi.render.Transformation;

/**
 * Un mur
 */
public class Mur extends Transformation<Brick> {

	public final static double EP=1.0;

	public final Boite boite;
	private double z0;
	private double z1;
	private final Point2D p0=new Point2D.Double();
	private final Point2D dp=new Point2D.Double();
	private final Point2D p1=new Point2D.Double();
	private final AffineTransform aff;
	public final Point2D p0t=new Point2D.Double();
	public final Point2D p1t=new Point2D.Double();

	public Mur(final Boite boite,final double x0,final double y0,final double z0,final double dx,final double dy,final double dz,final STransform trans,final AffineTransform aff) {
		//super(new Brick(new SPoint(x0 - 0.5 * EP,y0 - 0.5 * EP,z0),new SVector(dx + EP,dy + EP,dz)),trans);
		super(new Brick(),trans);
		if(Math.abs(dx) <= 1e-3) {
			geometry.set(x0 - 0.5 * EP,y0,z0,EP,dy,dz);
		} else {
			geometry.set(x0,y0 - 0.5 * EP,z0,dx,EP,dz);
		}
		this.boite=boite;
		this.z0=z0;
		this.z1=z0 + dz;
		this.aff=aff;
		p0.setLocation(x0,y0);
		dp.setLocation(dx,dy);
		p1.setLocation(x0 + dx,y0 + dy);
		calcTrans();
		this.boite.murs.add(this);
		this.boite.add(this);
	}
	private void destroy() {
		boite.remove(this);
		boite.murs.remove(this);
	}

	private void calcTrans() {
		aff.transform(p0,p0t);
		aff.transform(p1,p1t);
	}

	public void soustrais(final Boite b) {
		if(b.z0 >= z1 || b.z1 <= z0) {
			return;
		}
		if(z0 < b.z0) {
			new Mur(boite,p0.getX(),p0.getY(),z0,dp.getX(),dp.getY(),b.z0 - z0,transform,aff);
			z0=b.z0;
			geometry.corner.coords[2]=z0;
			geometry.size.coords[2]=z1 - z0;
		}
		if(z1 > b.z1) {
			new Mur(boite,p0.getX(),p0.getY(),z1,dp.getX(),dp.getY(),b.z1 - z1,transform,aff);
			z1=b.z1;
			geometry.size.coords[2]=z1 - z0;
		}
		double l0=Double.NaN,l1=Double.NaN;
		for(int i=0,j=1;i < 4;++i,j=(i + 1) % 4) {
			double l=Sculpture.l(p0t,p1t,b.plan.pts[i],b.plan.pts[j]);
			if(Double.isNaN(l)) {
				continue;
			}
			if(Double.isNaN(l0)) {
				l0=l;
			} else {
				l1=l;
			}
		}
		if(Double.isNaN(l0)) {
			return; //pas d'intersection
		}
		if(Double.isNaN(l1)) {
			System.err.println("berk"); //une seule intersection
			System.err.println(p0t);
			System.err.println(p1t);
			for(int i=0,j=1;i < 4;i++,j=(i + 1) % 4) {
				System.err.println(b.plan.pts[i]);
				System.err.println(Sculpture.l(p0t,p1t,b.plan.pts[i],b.plan.pts[j]));
			}
			return;
		}
		if(l1 < l0) {
			double l=l0;
			l0=l1;
			l1=l;
		}
		if(l1 <= 0.0) {
			return; //pas d'intersection
		}
		if(l0 >= 1.0) {
			return; //pas d'intersection
		}
		if(l0 <= 0.0 && l1 >= 1.0) { //entièrement dans b
			destroy();
			return;
		}
		if(l0 <= 0.0) {
			//p0t dedans, p1t dehors
			p0.setLocation(p0.getX() + l1 * dp.getX(),p0.getY() + l1 * dp.getY());
			dp.setLocation(p1.getX() - p0.getX(),p1.getY() - p0.getY());
			geometry.corner.set(p0.getX() - 0.5 * EP,p0.getY() - 0.5 * EP,z0);
			geometry.size.set(dp.getX() + EP,dp.getY() + EP,z1 - z0);
			calcTrans();
			return;
		}
		if(l1 >= 1.0) {
			//p0t dehors, p1t dedans
			p1.setLocation(p0.getX() + l0 * dp.getX(),p0.getY() + l0 * dp.getY());
			dp.setLocation(p1.getX() - p0.getX(),p1.getY() - p0.getY());
			geometry.size.set(dp.getX() + EP,dp.getY() + EP,z1 - z0);
			calcTrans();
			return;
		}
		new Mur(boite,p0.getX() + l1 * dp.getX(),p0.getY() + l1 * dp.getY(),z0,p1.getX() - (p0.getX() + l1 * dp.getX()),p1.getY() - (p0.getY() + l1 * dp.getY()),z1 - z0,transform,aff);
		p1.setLocation(p0.getX() + l0 * dp.getX(),p0.getY() + l0 * dp.getY());
		dp.setLocation(p1.getX() - p0.getX(),p1.getY() - p0.getY());
		geometry.size.set(dp.getX() + EP,dp.getY() + EP,z1 - z0);
		calcTrans();
		return;
	}
}
