package csgfortridi.bsp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import tridi.base.PolyCoords;
import tridi.base.SVector;
import tridi.csg.Plane;
import tridi.geom.FaceSet;

public class BSPnode {
	Plane partition;
	ArrayList<PolyCoords> coincidentPoly;
	BSPnode front;
	BSPnode back;

	public static int numberOfNodes = 0;
	public static int numberOfPolys = 0;

	/*
	 * public BSPnode(final PolyCoords p) { partition = new Plane(p.points[0],
	 * p.normals[0]); coincidentPoly = new ArrayList<PolyCoords>();
	 * coincidentPoly.add(p); front = null; back = null; }
	 */

	private BSPnode(final Deque<PolyCoords> polygons) {
		++numberOfNodes;
		SVector temp = new SVector();
		PolyCoords p = polygons.pop();
		partition = new Plane(p.points[0], p.normals[0]);// p.normals might be adjusted so it should be recalculated
		coincidentPoly = new ArrayList<PolyCoords>();
		++numberOfPolys;
		coincidentPoly.add(p);
		ArrayDeque<PolyCoords> frontList = new ArrayDeque<>();
		ArrayDeque<PolyCoords> backList = new ArrayDeque<>();
		while (!polygons.isEmpty()) {
			p = polygons.pop();
			switch (p.classify(partition)) {
			case COINCIDENT:
				++numberOfPolys;
				this.coincidentPoly.add(p);
				break;
			case FRONT:
				frontList.add(p);
				break;
			case BACK:
				backList.add(p);
				break;
			case SECANT:
				p.split(partition, frontList, backList, temp);
			}
		}
		if (frontList.isEmpty()) {
			front = null;
		} else {
			front = new BSPnode(frontList);
		}
		if (backList.isEmpty()) {
			back = null;
		} else {
			back = new BSPnode(backList);
		}
	}

	private static ArrayDeque<PolyCoords> faceSetToPolygons(final FaceSet f) {
		ArrayDeque<PolyCoords> polygons = new ArrayDeque<PolyCoords>(f.triangles);
		polygons.addAll(f.quadrangles);
		return polygons;
	}

	public BSPnode(final FaceSet f) {
		this(faceSetToPolygons(f));
	}

	public void print(final String indent) {
		System.out.println(partition);
		System.out.print("Polys: ");
		for (PolyCoords p : coincidentPoly) {
			System.out.print(p + " ");
		}
		System.out.println();
		if (front == null) {
			System.out.print(indent);
			System.out.println("front = null");
		} else {
			System.out.print(indent);
			System.out.print("front = ");
			front.print(indent + "  ");
		}
		if (back == null) {
			System.out.print(indent);
			System.out.println("back = null");
		} else {
			System.out.print(indent);
			System.out.print("back = ");
			back.print(indent + "  ");
		}
	}

}
