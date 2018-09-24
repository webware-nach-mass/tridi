package tridi.exp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import tridi.base.QuadrangleCoords;
import tridi.base.SPoint;
import tridi.base.TriangleCoords;
import tridi.geom.FaceSet;

/**
 * Somebody who can read .obj files.
 */
public class ObjReader {

	public static HashMap<String,FaceSet> read(final File f,final double unscale) throws IOException {
		if(!f.exists() || f.length() < 10) {
			return new HashMap<String,FaceSet>();
		}
		BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
		HashMap<String,FaceSet> result=read(in,unscale);
		in.close();
		return result;
	}
	public static HashMap<String,FaceSet> read(final BufferedReader in,final double unscale) throws IOException {
		HashMap<String,SPoint> pts=new HashMap<String,SPoint>();
		int idx=0;
		HashMap<String,FaceSet> result=new HashMap<String,FaceSet>();
		FaceSet res=null;
		for(int ln=1;true;++ln) {
			String l=in.readLine();
			if(l == null) {
				break;
			}
			if(l.startsWith("o ")) {
				res=new FaceSet();
				result.put(l.substring(2).trim(),res);
			} else if(l.startsWith("v ")) {
				if(res == null) {
					System.err.println("Unexpected vertex on line " + ln + ": " + l);
					res=new FaceSet();
					String id="o_" + result.size();
					if(result.containsKey(id)) {
						for(int i=result.size() + 1;result.containsKey(id);++i) {
							id="o_" + i;
						}
					}
					result.put(id,res);
				}
				SPoint p=new SPoint();
				++idx;
				pts.put("" + idx,p);
				res.points.add(p);
				try {
					l=l.substring(2).trim();
					int i=l.indexOf(' ');
					p.coords[0]=Double.parseDouble(l.substring(0,i)) / unscale;
					l=l.substring(i + 1).trim();
					i=l.indexOf(' ');
					p.coords[1]=Double.parseDouble(l.substring(0,i)) / unscale;
					l=l.substring(i + 1).trim();
					p.coords[2]=Double.parseDouble(l) / unscale;
				} catch(NumberFormatException x) {
					System.err.println("Bad vertex on line " + ln + ": " + l);
				}
			} else if(l.startsWith("f ")) {
				if(res == null) {
					System.err.println("Unexpected face on line " + ln + ": " + l);
					res=new FaceSet();
					String id="o_" + result.size();
					if(result.containsKey(id)) {
						for(int i=result.size() + 1;result.containsKey(id);++i) {
							id="o_" + i;
						}
					}
					result.put(id,res);
				}
				l=l.substring(2).trim();
				int i=l.indexOf(' ');
				SPoint p0=pts.get(l.substring(0,i));
				if(p0 == null) {
					System.err.println("Missing vertex " + l.substring(0,i) + " on line " + ln);
					p0=new SPoint();
				}
				l=l.substring(i + 1).trim();
				i=l.indexOf(' ');
				SPoint p1=pts.get(l.substring(0,i));
				if(p1 == null) {
					System.err.println("Missing vertex " + l.substring(0,i) + " on line " + ln);
					p1=new SPoint();
				}
				l=l.substring(i + 1).trim();
				i=l.indexOf(' ');
				if(i < 0) {
					i=l.length();
				}
				SPoint p2=pts.get(l.substring(0,i));
				if(p2 == null) {
					System.err.println("Missing vertex " + l.substring(0,i) + " on line " + ln);
					p2=new SPoint();
				}
				if(i >= l.length()) {
					TriangleCoords t=new TriangleCoords(p0,p1,p2);
					t.calcNormals();
					res.triangles.add(t);
				} else {
					l=l.substring(i + 1).trim();
					SPoint p3=pts.get(l);
					if(p3 == null) {
						System.err.println("Missing vertex " + l + " on line " + ln);
						p3=new SPoint();
					}
					QuadrangleCoords q=new QuadrangleCoords(p0,p1,p2,p3);
					q.calcNormals();
					res.quadrangles.add(q);
				}
			} //else ignore
		}
		return result;
	}
}
