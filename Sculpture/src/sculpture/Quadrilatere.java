package sculpture;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Un truc à 4 coins
 */
public class Quadrilatere {

	public final Point2D[] pts=new Point2D[4];
	public Quadrilatere(final double x0,final double y0,final double dx,final double dy,final AffineTransform aff) {
		pts[0]=new Point2D.Double(x0,y0);
		pts[1]=new Point2D.Double(x0 + dx,y0);
		pts[2]=new Point2D.Double(x0 + dx,y0 + dy);
		pts[3]=new Point2D.Double(x0,y0 + dy);
		for(int i=0;i < 4;++i) {
			aff.transform(pts[i],pts[i]);
		}
	}

	public boolean contains(final Point2D p) {
		int n=0;
		for(int i=0,j=1;i < 4;++i,j=(i + 1) % 4) {
			if(p.getY() <= pts[i].getY() && p.getY() <= pts[j].getY()) {
				continue;
			}
			if(p.getY() >= pts[i].getY() && p.getY() >= pts[j].getY()) {
				continue;
			}
			double l=(p.getY() - pts[i].getY()) / (pts[j].getY() - pts[i].getY());
			if(p.getX() < pts[i].getX() + l * (pts[j].getX() - pts[i].getX())) {
				++n;
			}
		}
		return n % 2 == 1;
	}
}
