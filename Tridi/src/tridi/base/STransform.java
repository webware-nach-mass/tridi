package tridi.base;

/**
 * A transform in scene space.
 */
public class STransform {

	public STransform() {
		for(int i=0;i < 3;++i) {
			m[i][i]=1.0;
		}
	}
	private final double[][] m=new double[3][4];
	private boolean scaling=false;
	private boolean symetry=false;

	public void setFrom(final STransform orig) {
		for(int i=0;i < 3;++i) {
			for(int j=0;j < 4;++j) {
				m[i][j]=orig.m[i][j];
			}
		}
		scaling=orig.scaling;
		symetry=orig.symetry;
	}
	public void setToIdentity() {
		for(int i=0;i < 3;++i) {
			for(int j=0;j < 4;++j) {
				m[i][j]=0.0;
			}
			m[i][i]=1.0;
		}
		scaling=false;
		symetry=false;
	}
	public void translate(final double tx,final double ty,final double tz) {
		m[0][3]+=tx;
		m[1][3]+=ty;
		m[2][3]+=tz;
	}
	public void preTranslate(final double tx,final double ty,final double tz) {
		m[0][3]+=tx * m[0][0] + ty * m[0][1] + tz * m[0][2];
		m[1][3]+=tx * m[1][0] + ty * m[1][1] + tz * m[1][2];
		m[2][3]+=tx * m[2][0] + ty * m[2][1] + tz * m[2][2];
	}
	static public STransform makeTranslation(final double tx,final double ty,final double tz) {
		STransform result=new STransform();
		result.translate(tx,ty,tz);
		return result;
	}
	public void scale(final double s) {
		if(s != 1.0) {
			for(int i=0;i < 3;++i) {
				m[i][i]*=s;
				m[i][3]*=s;
			}
			if(s != -1.0) {
				scaling=true;
			}
			if(s < 0.0) {
				symetry=!symetry;
			}
		}
	}
	public void scale(final int axis,final double s) {
		if(s != 1.0) {
			m[axis][axis]*=s;
			m[axis][3]*=s;
			if(s != -1.0) {
				scaling=true;
			}
			if(s < 0.0) {
				symetry=!symetry;
			}
		}
	}
	public void scale(final double sx,final double sy,final double sz) {
		m[0][0]*=sx;
		m[0][3]*=sx;
		if(sx != -1.0 && sx != 1.0) {
			scaling=true;
		}
		if(sx < 0.0) {
			symetry=!symetry;
		}
		m[1][1]*=sy;
		m[1][3]*=sy;
		if(sy != -1.0 && sy != 1.0) {
			scaling=true;
		}
		if(sy < 0.0) {
			symetry=!symetry;
		}
		m[2][2]*=sz;
		m[2][3]*=sz;
		if(sz != -1.0 && sz != 1.0) {
			scaling=true;
		}
		if(sz < 0.0) {
			symetry=!symetry;
		}
	}
	public void rotate(final int axis,final double angle) {
		double c=Math.cos(angle),s=Math.sin(angle);
		int iy=(axis + 1) % 3,iz=(axis + 2) % 3;
		for(int j=0;j < 4;++j) {
			double ny=m[iy][j] * c - m[iz][j] * s;
			m[iz][j]=m[iz][j] * c + m[iy][j] * s;
			m[iy][j]=ny;
		}
	}
	public void preRotate(final int axis,final double angle) {
		double c=Math.cos(angle),s=Math.sin(angle);
		int jy=(axis + 1) % 3,jz=(axis + 2) % 3;
		for(int i=0;i < 3;++i) {
			double ny=m[i][jy] * c + m[i][jz] * s;
			m[i][jz]=m[i][jz] * c - m[i][jy] * s;
			m[i][jy]=ny;
		}
	}
	/**
	 * Replaces <code>this</code> with <code>other*this</code>
	 */
	public void multiply(final STransform other) {
		for(int j=0;j < 3;++j) {
			setColumn(j,m[0][j] * other.m[0][0] + m[1][j] * other.m[0][1] + m[2][j] * other.m[0][2], //
					m[0][j] * other.m[1][0] + m[1][j] * other.m[1][1] + m[2][j] * other.m[1][2], //
					m[0][j] * other.m[2][0] + m[1][j] * other.m[2][1] + m[2][j] * other.m[2][2]);
		}
		setColumn(3,m[0][3] * other.m[0][0] + m[1][3] * other.m[0][1] + m[2][3] * other.m[0][2] + other.m[0][3], //
				m[0][3] * other.m[1][0] + m[1][3] * other.m[1][1] + m[2][3] * other.m[1][2] + other.m[1][3], //
				m[0][3] * other.m[2][0] + m[1][3] * other.m[2][1] + m[2][3] * other.m[2][2] + other.m[2][3]);
		scaling|=other.scaling;
		if(other.symetry) {
			symetry=!symetry;
		}
	}
	private void setColumn(final int j,final double x,final double y,final double z) {
		m[0][j]=x;
		m[1][j]=y;
		m[2][j]=z;
	}

	/**
	 * Transforms (0,0,0) (extracts the translation part of <code>this</code>) into the given SPoint.
	 */
	public void transform0(final SPoint result) {
		result.set(m[0][3],m[1][3],m[2][3]);
	}
	public void transform(final SPoint orig,final SPoint result) {
		result.set(m[0][0] * orig.coords[0] + m[0][1] * orig.coords[1] + m[0][2] * orig.coords[2] + m[0][3], //
				m[1][0] * orig.coords[0] + m[1][1] * orig.coords[1] + m[1][2] * orig.coords[2] + m[1][3], //
				m[2][0] * orig.coords[0] + m[2][1] * orig.coords[1] + m[2][2] * orig.coords[2] + m[2][3]);
	}
	public void transform(final SVector orig,final SVector result) {
		result.set(m[0][0] * orig.coords[0] + m[0][1] * orig.coords[1] + m[0][2] * orig.coords[2], //
				m[1][0] * orig.coords[0] + m[1][1] * orig.coords[1] + m[1][2] * orig.coords[2], //
				m[2][0] * orig.coords[0] + m[2][1] * orig.coords[1] + m[2][2] * orig.coords[2]);
	}
	public void transform(final CableCoords orig,final CableCoords result) {
		for(int i=0;i < 2;++i) {
			transform(orig.points[i],result.points[i]);
		}
		if(orig.normalsOK) {
			for(int i=0;i < 2;++i) {
				transform(orig.normals[i],result.normals[i]);
			}
			if(scaling) {
				result.normalize();
			}
		} else {
			result.normalsOK=false;
		}
	}
	public void transform(final TriangleCoords orig,final TriangleCoords result) {
		for(int i=0;i < 3;++i) {
			transform(orig.points[i],result.points[i]);
			transform(orig.normals[i],result.normals[i]);
		}
		if(symetry) {
			SPoint tmp=result.points[1];
			result.points[1]=result.points[2];
			result.points[2]=tmp;
			SVector tmv=result.normals[1];
			result.normals[1]=result.normals[2];
			result.normals[2]=tmv;
		}
		if(scaling) {
			result.normalize();
		}
	}
	public void transform(final QuadrangleCoords orig,final QuadrangleCoords result) {
		for(int i=0;i < 4;++i) {
			transform(orig.points[i],result.points[i]);
			transform(orig.normals[i],result.normals[i]);
		}
		if(symetry) {
			SPoint tmp=result.points[1];
			result.points[1]=result.points[3];
			result.points[3]=tmp;
			SVector tmv=result.normals[1];
			result.normals[1]=result.normals[3];
			result.normals[3]=tmv;
		}
		if(scaling) {
			result.normalize();
		}
	}
}
