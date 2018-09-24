package tridi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;

import tridi.base.STransform;
import tridi.exp.ObjRenderer;
import tridi.exp.ObjWriter;
import tridi.geom.CompoundGeometry;
import tridi.geom.TorusSector;
import tridi.render.SPanel;
import tridi.render.SViewer;
import tridi.render.Stage;
import tridi.render.Transformation;

/**
 * A test JFrame
 */
public class TestTridi extends JFrame {

	public TestTridi() {
		super("Tridi");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		SViewer viewer=new SViewer(5.0,5.0);
		final SPanel tp=viewer.spanel;
		tp.setPreferredSize(new Dimension(800,600));
		getContentPane().add(viewer,BorderLayout.CENTER);
		Stage sc=tp.getStage();

		final Transformation<CompoundGeometry> cg=new Transformation<CompoundGeometry>(new CompoundGeometry());
		sc.addGeometry(cg);

		cg.geometry.add(new TorusSector());

		try {
			ObjWriter out=new ObjWriter(new File("test/test.obj"));
			sc.renderObj(new ObjRenderer(90.0,out));
			out.close();
		} catch(IOException x) {
			x.printStackTrace();
		}

		cg.transform=new STransform();
		timer=new Timer(100,new ActionListener() {
			private double angle=0.0;
			@Override
			public void actionPerformed(final ActionEvent e) {
				//angle+=Math.PI / 100.0;
				if(angle >= Math.PI) {
					angle-=2.0 * Math.PI;
				}
				cg.transform.setToIdentity();
				cg.transform.rotate(0,angle);
				cg.transform.rotate(1,2.0 * angle);
				tp.render();
				tp.repaint();
			}
		});
	}

	private final Timer timer;
	@Override
	public void dispose() {
		if(timer != null) {
			timer.stop();
		}
		super.dispose();
	}

	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception x) {
			x.printStackTrace();
		}
		TestTridi t=new TestTridi() {
			@Override
			public void dispose() {
				super.dispose();
				System.exit(0);
			}
		};
		t.pack();
		t.setVisible(true);
		if(t.timer != null) {
			t.timer.start();
		}
	}
}
