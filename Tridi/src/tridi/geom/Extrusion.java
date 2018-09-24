package tridi.geom;

import java.util.ArrayList;

import tridi.base.Geometry;
import tridi.base.SPath;
import tridi.base.SPoint;
import tridi.base.SPointList;
import tridi.base.SVector;
import tridi.base.TriangleDelegate;
import tridi.exp.ObjRenderer;
import tridi.render.TriangleRenderer;

/**
 * Extrusion of a section along a spine.
 */
public class Extrusion implements Geometry {

	/**
	 * The spine.
	 * The z-axis of the section will follow its tangents,
	 * the x-axis of the section will follow its normals.
	 */
	public final SPath spine;
	/**
	 * Section at the beginning of the spine.
	 */
	public final SPointList baseSection;
	/**
	 * Section at the end of the spine.
	 * <code>null</code> means: same as the baseSection.
	 * If not null, must have the same point count as the baseSection.
	 */
	public final SPointList topSection;

	public Extrusion() {
		super();
		spine=new SPath();
		baseSection=new SPointList();
		topSection=null;
	}

	public Extrusion(final SPath spine,final SPointList baseSection,final SPointList topSection) {
		super();
		this.spine=spine;
		this.baseSection=baseSection;
		this.topSection=topSection;
	}

	@Override
	public void calculate() {
		//nothing to do
	}

	private final ArrayList<SPoint> temp=new ArrayList<SPoint>();
	private final SVector[] vrep= {new SVector(),new SVector(),new SVector()};
	private final SVector[] nrep= {new SVector(),new SVector(),new SVector()};
	private final SPoint vs0=new SPoint(),ns0=new SPoint(),vs1=new SPoint(),ns1=new SPoint();
	private final SPoint vp0=new SPoint(),vp1=new SPoint(),np0=new SPoint(),np1=new SPoint();
	@Override
	public void render(final String id,final TriangleRenderer renderer) {
		renderer.startObject(id);
		//calculate first referential
		vrep[2].setFrom(spine.tangents.get(0));
		vrep[0].setFrom(spine.normals.get(0));
		SVector.vector(vrep[2].coords,vrep[0].coords,vrep[1].coords);
		vrep[1].normalize();
		SPoint vc=spine.points.get(0);
		//cap base
		cap(baseSection,vc,-1,renderer.renderTriangle);
		//extrude sides
		SPointList topsec=topSection == null ? baseSection : topSection;
		double vprop=0.0;
		for(int n=spine.points.size() - 1,i=1;i <= n;++i) {
			double nprop=i / (double)n;
			//calculate referential
			nrep[2].setFrom(spine.tangents.get(i));
			nrep[0].setFrom(spine.normals.get(i));
			SVector.vector(nrep[2].coords,nrep[0].coords,nrep[1].coords);
			nrep[1].normalize();
			SPoint nc=spine.points.get(i);
			//follow section
			vs0.setToInterpolate(baseSection.get(0),topsec.get(0),vprop);
			ns0.setToInterpolate(baseSection.get(0),topsec.get(0),nprop);
			vp0.setFrom(vc);
			np0.setFrom(nc);
			for(int k=0;k < 3;++k) {
				vp0.addInterpolate(vrep[k],vs0.coords[k]);
				np0.addInterpolate(nrep[k],ns0.coords[k]);
			}
			for(int m=baseSection.size(),j=1;j < m;++j) {
				boolean mustRender=baseSection.get(j) != baseSection.get(j - 1) || topsec.get(j) != topsec.get(j - 1);
				vs1.setToInterpolate(baseSection.get(j),topsec.get(j),vprop);
				ns1.setToInterpolate(baseSection.get(j),topsec.get(j),nprop);
				vp1.setFrom(vc);
				np1.setFrom(nc);
				for(int k=0;k < 3;++k) {
					vp1.addInterpolate(vrep[k],vs1.coords[k]);
					np1.addInterpolate(nrep[k],ns1.coords[k]);
				}
				if(mustRender) {
					renderer.renderTriangle.points[0].setFrom(vp0);
					renderer.renderTriangle.points[1].setFrom(vp1);
					renderer.renderTriangle.points[2].setFrom(np1);
					renderer.renderTriangle.calcNormals();
					renderer.renderTriangle.renderAsDelegate();
					renderer.renderTriangle.points[0].setFrom(vp0);
					renderer.renderTriangle.points[1].setFrom(np1);
					renderer.renderTriangle.points[2].setFrom(np0);
					renderer.renderTriangle.calcNormals();
					renderer.renderTriangle.renderAsDelegate();
				}
				vs0.setFrom(vs1);
				ns0.setFrom(ns1);
				vp0.setFrom(vp1);
				np0.setFrom(np1);
			}
			vrep[0].setFrom(nrep[0]);
			vrep[1].setFrom(nrep[1]);
			vrep[2].setFrom(nrep[2]);
			vc=nc;
			vprop=nprop;
		}
		//cap end
		cap(topsec,vc,1,renderer.renderTriangle);
		renderer.endObject();
	}
	private void cap(final SPointList section,final SPoint vc,final int direction,final TriangleDelegate triangle) {
		SPoint vp=null;
		for(SPoint p : section) {
			if(vp != p) {
				temp.add(p);
				vp=p;
			}
		}
		if(temp.get(0).sameAs(temp.get(temp.size() - 1))) {
			temp.remove(temp.size() - 1);
		}
		for(int tries=0;temp.size() >= 3 && tries < temp.size();/*nothing*/) {
			SPoint p0=temp.get(0),p1=temp.get(1),p2=temp.get(2);
			boolean ok=Math.abs((p1.coords[0] - p0.coords[0]) * (p2.coords[1] - p1.coords[1]) - (p1.coords[1] - p0.coords[1]) * (p2.coords[0] - p1.coords[0])) > 1e-4;
			if(ok) {
				for(int n=temp.size() - 1,i=3;i <= n;++i) {
					if(linesIntersect(p0,p2,temp.get(i - 1),temp.get(i))) {
						ok=false;
						break;
					}
				}
			}
			if(ok && temp.size() > 3) {
				ok=isInsideTemp((p0.coords[0] + p2.coords[0]) / 2.0,(p0.coords[1] + p2.coords[1]) / 2.0);
			}
			if(ok) {
				triangle.points[0].setFrom(vc);
				triangle.points[1].setFrom(vc);
				triangle.points[2].setFrom(vc);
				for(int k=0;k < 3;++k) {
					triangle.points[0].addInterpolate(vrep[k],p0.coords[k]);
					triangle.points[1].addInterpolate(vrep[k],p1.coords[k]);
					triangle.points[2].addInterpolate(vrep[k],p2.coords[k]);
				}
				triangle.setSameNormals(direction * vrep[2].coords[0],direction * vrep[2].coords[1],direction * vrep[2].coords[2],true);
				triangle.renderAsDelegate();
				temp.remove(1);
				tries=0;
			} else {
				temp.remove(0);
				temp.add(p0);
				++tries;
			}
		}
		temp.clear();
	}
	/**
	 * Checks if lines in xy intersect strictly
	 */
	private boolean linesIntersect(final SPoint p0,final SPoint p1,final SPoint q0,final SPoint q1) {
		double det=(p1.coords[0] - p0.coords[0]) * (q0.coords[1] - q1.coords[1]) - (p1.coords[1] - p0.coords[1]) * (q0.coords[0] - q1.coords[0]);
		if(Math.abs(det) < 1e-3) {
			return false;
		}
		double l=(q0.coords[0] - p0.coords[0]) * (q0.coords[1] - q1.coords[1]) - (q0.coords[1] - p0.coords[1]) * (q0.coords[0] - q1.coords[0]) / det;
		double m=(p1.coords[0] - p0.coords[0]) * (q0.coords[1] - p0.coords[1]) - (p1.coords[1] - p0.coords[1]) * (q0.coords[0] - p0.coords[0]) / det;
		return l > 0.0 && l < 1.0 && m > 0.0 && m < 1.0;
	}
	private boolean isInsideTemp(final double x,final double y) {
		int cnt=0;
		for(int n=temp.size(),i=0;i < n;++i) {
			SPoint p0=temp.get(i),p1=temp.get((i + 1) % n);
			if(p0.coords[1] < y && p1.coords[1] <= y) {
				continue;
			}
			if(p0.coords[1] > y && p1.coords[1] >= y) {
				continue;
			}
			if(p0.coords[0] > x && p1.coords[0] >= x) {
				continue;
			}
			double dy=p1.coords[1] - p0.coords[1];
			if(Math.abs(dy) < 1e-3) {
				if(p0.coords[0] < x && p1.coords[0] <= x) {
					continue;
				}
				++cnt;
			} else {
				if(p0.coords[0] + ((p1.coords[0] - p0.coords[0]) * (y - p0.coords[1])) / dy <= x) {
					++cnt;
				}
			}
		}
		return cnt % 2 == 1;
	}

	@Override
	public void renderObj(final String id,final ObjRenderer renderer) {
		renderer.startObject(id);
		//calculate first referential
		vrep[2].setFrom(spine.tangents.get(0));
		vrep[0].setFrom(spine.normals.get(0));
		SVector.vector(vrep[2].coords,vrep[0].coords,vrep[1].coords);
		vrep[1].normalize();
		SPoint vc=spine.points.get(0);
		//cap base
		cap(baseSection,vc,-1,renderer.renderTriangle);
		//extrude sides
		SPointList topsec=topSection == null ? baseSection : topSection;
		double vprop=0.0;
		for(int n=spine.points.size() - 1,i=1;i <= n;++i) {
			double nprop=i / (double)n;
			//calculate referential
			nrep[2].setFrom(spine.tangents.get(i));
			nrep[0].setFrom(spine.normals.get(i));
			SVector.vector(nrep[2].coords,nrep[0].coords,nrep[1].coords);
			nrep[1].normalize();
			SPoint nc=spine.points.get(i);
			//follow section
			vs0.setToInterpolate(baseSection.get(0),topsec.get(0),vprop);
			ns0.setToInterpolate(baseSection.get(0),topsec.get(0),nprop);
			vp0.setFrom(vc);
			np0.setFrom(nc);
			for(int k=0;k < 3;++k) {
				vp0.addInterpolate(vrep[k],vs0.coords[k]);
				np0.addInterpolate(nrep[k],ns0.coords[k]);
			}
			for(int m=baseSection.size(),j=1;j < m;++j) {
				boolean mustRender=baseSection.get(j) != baseSection.get(j - 1) || topsec.get(j) != topsec.get(j - 1);
				vs1.setToInterpolate(baseSection.get(j),topsec.get(j),vprop);
				ns1.setToInterpolate(baseSection.get(j),topsec.get(j),nprop);
				vp1.setFrom(vc);
				np1.setFrom(nc);
				for(int k=0;k < 3;++k) {
					vp1.addInterpolate(vrep[k],vs1.coords[k]);
					np1.addInterpolate(nrep[k],ns1.coords[k]);
				}
				if(mustRender) {
					renderer.renderQuadrangle.points[0].setFrom(vp0);
					renderer.renderQuadrangle.points[1].setFrom(vp1);
					renderer.renderQuadrangle.points[2].setFrom(np1);
					renderer.renderQuadrangle.points[3].setFrom(np0);
					renderer.renderQuadrangle.calcNormals();
					renderer.renderQuadrangle.renderObjAsDelegate();
				}
				vs0.setFrom(vs1);
				ns0.setFrom(ns1);
				vp0.setFrom(vp1);
				np0.setFrom(np1);
			}
			vrep[0].setFrom(nrep[0]);
			vrep[1].setFrom(nrep[1]);
			vrep[2].setFrom(nrep[2]);
			vc=nc;
			vprop=nprop;
		}
		//cap end
		cap(topsec,vc,1,renderer.renderTriangle);
		renderer.endObject();
	}

}
