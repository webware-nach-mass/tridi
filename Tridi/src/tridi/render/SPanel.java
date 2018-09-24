package tridi.render;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

/**
 * A JPanel with a 3D Scene on it.
 */
public class SPanel extends JPanel {
	public SPanel() {
		this(null,null);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				render();
			}
		});
	}
	public SPanel(final Stage stage,final Camera camera) {
		super(null);
		setStage(stage);
		setCamera(camera);
	}
	private Stage stage;
	public void setStage(final Stage stage) {
		this.stage=stage == null ? new Stage() : stage;
	}
	public Stage getStage() {
		return stage;
	}
	private Camera camera;
	public void setCamera(final Camera camera) {
		this.camera=camera == null ? new Camera() : camera;
	}
	public Camera getCamera() {
		return camera;
	}

	private ZBufferRenderer renderer=null;
	public void render() {
		if(renderer == null || renderer.buf.w != getWidth() || renderer.buf.h != getHeight()) {
			renderer=new ZBufferRenderer(camera,getWidth(),getHeight());
		}
		stage.render(renderer);
	}
	@Override
	protected void paintComponent(final Graphics g) {
		if(getWidth() <= 0 || getHeight() <= 0) {
			super.paintComponent(g);
			return;
		}
		if(renderer == null || renderer.buf.w != getWidth() || renderer.buf.h != getHeight()) {
			render();
		}
		g.drawImage(renderer.buf.image,0,0,this);
	}
}
