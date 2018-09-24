package tridi.render;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.util.Arrays;

/**
 * A z-buffer.
 */
public class ZBuffer {
	public final int w;
	public final int h;
	public final Image image;
	public ZBuffer(final int w,final int h) {
		super();
		this.w=w;
		this.h=h;
		rgb=new int[w * h];
		z=new int[w * h];
		source=new MemoryImageSource(w,h,rgb,0,w);
		source.setAnimated(true);
		image=Toolkit.getDefaultToolkit().createImage(source);
	}
	private final int[] rgb;
	private final MemoryImageSource source;
	private final int[] z;

	public void startFrame() {
		//nothing to do
	}

	public void clear(final int background) {
		Arrays.fill(rgb,background | 0xFF000000);
		Arrays.fill(z,Integer.MAX_VALUE);
	}
	public void set(final int x,final int y,final int z,final int rgb) {
		if(x < 0 || x >= w || y < 0 || y >= h || z < 0 || z > this.z[x + w * y]) {
			return;
		}
		this.rgb[x + w * y]=rgb | 0xFF000000;
		this.z[x + w * y]=z;
	}
	public void set(final int x,final int y,final int z,final double r,final double g,final double b) {
		if(x < 0 || x >= w || y < 0 || y >= h || z < 0 || z > this.z[x + w * y]) {
			return;
		}
		this.rgb[x + w * y]=((r >= 1.0 ? 255 : r <= 0 ? 0 : (int)Math.round(255 * r)) << 16) | ((g >= 1.0 ? 255 : g <= 0.0 ? 0 : (int)Math.round(255 * g)) << 8) | (b >= 1.0 ? 255 : b <= 0.0 ? 0 : (int)Math.round(255 * b)) | 0xFF000000;
		this.z[x + w * y]=z;
	}
	public int getRGB(final int x,final int y) {
		return x < 0 || x >= w || y < 0 || y >= h ? 0 : rgb[x + w * y];
	}
	public int getZ(final int x,final int y) {
		return x < 0 || x >= w || y < 0 || y >= h ? 0 : z[x + w * y];
	}

	public void endFrame() {
		source.newPixels();
	}

}
