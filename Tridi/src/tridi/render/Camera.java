package tridi.render;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import tridi.base.SPoint;
import tridi.base.SVector;

/**
 * A Camera.
 */
public class Camera {
	public SPoint eye,lookAt;
	public SVector vertical;
	public double postScale;
	public Camera() {
		super();
		eye=new SPoint(0.0,0.0,0.0); //default eye
		lookAt=new SPoint(0.0,1.0,0.0); //default lookAt
		vertical=new SVector(0.0,0.0,1.0); //default vertical
		postScale=1.0; //default postScale
	}

	private final RenderTransform transform=new RenderTransform();
	public void calcTransform(final int w,final int h) {

		/*  translate space to place eye at (0,0,0), then rotate to place lookAt at (0,something positive,0) and vertical at (0,0,something positive)
		    translation is -eye
		    Y = normalize(lookAt-eye)
		    X = normalize(Y x up)
		    Z = X x Y
				rotation matrix lines are
		    (X, Y, Z)
		 */
		//first, calculate lines
		for(int i=0;i < 3;++i) {
			transform.m[1][i]=lookAt.coords[i] - eye.coords[i];
		}
		double ln=SVector.calcLength(transform.m[1]);
		for(int i=0;i < 3;++i) {
			transform.m[1][i]/=ln;
		}
		SVector.vector(transform.m[1],vertical.coords,transform.m[0]);
		double vn=SVector.calcLength(transform.m[0]);
		for(int i=0;i < 3;++i) {
			transform.m[0][i]/=vn;
		}
		SVector.vector(transform.m[0],transform.m[1],transform.m[2]);
		//apply rotation to translation
		for(int i=0;i < 3;++i) {
			transform.m[i][3]=-eye.coords[0] * transform.m[i][0] - eye.coords[1] * transform.m[i][1] - eye.coords[2] * transform.m[i][2];
		}
		/* pre-scale space to place lookAt at (0,1,0) */
		for(int i=0;i < 3;++i) {
			for(int j=0;j < 4;++j) {
				transform.m[i][j]/=ln;
			}
		}
		/* Apply projection :
		   s=postScale*h/2/length(vertical)
		   u=sx/y+w/2=(sx+yw/2)/y
		   v=-sz/y+h/2=(yh/2-sz)/y
		   depth=y*maximal resolution
		 */
		double s=h / 2.0 / vn * postScale,mu=w / 2.0,mv=h / 2.0;
		for(int j=0;j < 4;++j) {
			double f=transform.m[1][j];
			transform.m[0][j]=s * transform.m[0][j] + mu * f;
			transform.m[1][j]=mv * f - s * transform.m[2][j];
			transform.m[2][j]=f;
		}
		transform.scaleDepth=10000.0;
		//		for(int i=0;i < 3;++i) {
		//			for(int j=0;j < 4;++j) {
		//				System.err.print("  " + transform.m[i][j]);
		//			}
		//			System.err.println();
		//		}
	}
	public RenderTransform getTransform() {
		return transform;
	}

	public void exportPOV(final PrintWriter out) {
		//TODO Camera.exportPOV
	}

	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception x) {
			x.printStackTrace();
		}
		final Camera c=new Camera();
		final SPoint orig=new SPoint();
		final ZPoint dest=new ZPoint();
		JPanel p=new JPanel(new GridLayout(5,2));
		p.add(new JLabel("eye"));
		final JTextField eye=new JTextField("" + c.eye.coords[0] + "," + c.eye.coords[1] + "," + c.eye.coords[2]);
		p.add(eye);
		p.add(new JLabel("lookAt"));
		final JTextField lookAt=new JTextField("" + c.lookAt.coords[0] + "," + c.lookAt.coords[1] + "," + c.lookAt.coords[2]);
		p.add(lookAt);
		p.add(new JLabel("point"));
		final JTextField in=new JTextField("x,y,z");
		p.add(in);
		p.add(new JLabel("result"));
		final JLabel out=new JLabel("u,v,depth");
		p.add(out);
		AbstractAction action=new AbstractAction("calc perspective for w=800 h=600") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				String s=eye.getText();
				int i=s.indexOf(','),j=i < 0 || i + 1 >= s.length() ? -1 : s.indexOf(',',i + 1);
				if(i < 0 || j < 0) {
					out.setText("error in eye");
					return;
				}
				try {
					c.eye.coords[0]=Double.parseDouble(s.substring(0,i).trim());
					c.eye.coords[1]=Double.parseDouble(s.substring(i + 1,j).trim());
					c.eye.coords[2]=Double.parseDouble(s.substring(j + 1).trim());
				} catch(NumberFormatException x) {
					out.setText("error in eye");
					return;
				}
				eye.setText("" + c.eye.coords[0] + "," + c.eye.coords[1] + "," + c.eye.coords[2]);

				s=lookAt.getText();
				i=s.indexOf(',');
				j=i < 0 || i + 1 >= s.length() ? -1 : s.indexOf(',',i + 1);
				if(i < 0 || j < 0) {
					out.setText("error in lookAt");
					return;
				}
				try {
					c.lookAt.coords[0]=Double.parseDouble(s.substring(0,i).trim());
					c.lookAt.coords[1]=Double.parseDouble(s.substring(i + 1,j).trim());
					c.lookAt.coords[2]=Double.parseDouble(s.substring(j + 1).trim());
				} catch(NumberFormatException x) {
					out.setText("error in lookAt");
					return;
				}
				lookAt.setText("" + c.lookAt.coords[0] + "," + c.lookAt.coords[1] + "," + c.lookAt.coords[2]);
				c.calcTransform(800,600);

				s=in.getText();
				i=s.indexOf(',');
				j=i < 0 || i + 1 >= s.length() ? -1 : s.indexOf(',',i + 1);
				if(i < 0 || j < 0) {
					out.setText("error in point");
					return;
				}
				try {
					orig.coords[0]=Double.parseDouble(s.substring(0,i).trim());
					orig.coords[1]=Double.parseDouble(s.substring(i + 1,j).trim());
					orig.coords[2]=Double.parseDouble(s.substring(j + 1).trim());
				} catch(NumberFormatException x) {
					out.setText("error in point");
					return;
				}
				in.setText("" + orig.coords[0] + "," + orig.coords[1] + "," + orig.coords[2]);
				c.getTransform().transform(orig,dest);

				out.setText("" + dest.coords[0] + "," + dest.coords[1] + "," + dest.coords[2]);
			}
		};
		p.add(new JLabel(""));
		p.add(new JButton(action),BorderLayout.SOUTH);
		eye.addActionListener(action);
		lookAt.addActionListener(action);
		in.addActionListener(action);
		JOptionPane.showMessageDialog(null,p);
		System.exit(0);
	}
}
