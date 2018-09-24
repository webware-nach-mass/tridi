package tridi.render;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A JPanel with an SPanel on it and controls for its Camera.
 */
public class SViewer extends JPanel {

	public final SPanel spanel=new SPanel();

	double camR=5.0;
	double camDZ=5.0;

	public SViewer() {
		this(5.0,5.0);
	}
	public SViewer(final double camR,final double camDZ) {
		super(new BorderLayout());
		add(spanel,BorderLayout.CENTER);
		add(zoom,BorderLayout.WEST);
		add(ws,BorderLayout.EAST);
		add(ss,BorderLayout.SOUTH);
		this.camR=camR;
		this.camDZ=camDZ;
		ChangeListener cammove=new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				double r=(zoom.getMaximum() + zoom.getMinimum() - zoom.getValue()) * SViewer.this.camR / 1000.0;
				double z=ws.getValue() * SViewer.this.camDZ / 1000.0;
				double a=ss.getValue() * Math.PI / 1000.0;
				spanel.getCamera().eye.set(r * Math.cos(a),r * Math.sin(a),z);
				refresh();
			}
		};
		zoom.addChangeListener(cammove);
		ws.addChangeListener(cammove);
		ss.addChangeListener(cammove);
		spanel.getCamera().lookAt.set(0.0,0.0,0.0);
		cammove.stateChanged(null);
	}

	protected final JSlider zoom=new JSlider(SwingConstants.VERTICAL,10,2000,1000);
	protected final JSlider ws=new JSlider(SwingConstants.VERTICAL,-1000,1000,300);
	protected final JSlider ss=new JSlider(SwingConstants.HORIZONTAL,-1000,1000,0);

	public void setSliders(final int zoom,final int ws,final int ss) {
		this.zoom.setValue(zoom);
		this.ws.setValue(ws);
		this.ss.setValue(ss);
	}

	/**
	 * Call this when you've changed the Geometries or the Camera.
	 */
	public void refresh() {
		spanel.render();
		spanel.repaint();
	}
}
