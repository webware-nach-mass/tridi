package sculpture;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import tridi.render.SViewer;

/**
 * Le JFrame
 */
public class Musee extends JFrame {

	public Musee() {
		super("Sculpture");
		sculpture=new Sculpture();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		vue.setPreferredSize(new Dimension(800,600));
		getContentPane().add(vue,BorderLayout.CENTER);
		final JPanel p=new JPanel(new BorderLayout());
		getContentPane().add(p,BorderLayout.EAST);
		p.add(new JButton(new AbstractAction("Nouveau") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				vue.spanel.getStage().removeGeometry(sculpture);
				sculpture=new Sculpture();
				p.invalidate();
				p.revalidate();
				p.repaint();
				vue.spanel.getStage().addGeometry(sculpture);
				vue.refresh();
			}
		}),BorderLayout.NORTH);
		p.add(new JButton(new AbstractAction("Export .obj") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				exportObj();
			}
		}),BorderLayout.SOUTH);
		vue.spanel.getStage().addGeometry(sculpture);
	}

	Sculpture sculpture;
	final SViewer vue=new SViewer(150.0,300.0);

	private JFileChooser fc=new JFileChooser(".");
	void exportObj() {
		if(fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File f=fc.getSelectedFile();
		try {
			sculpture.genere(f);
		} catch(IOException x) {
			x.printStackTrace();
			JOptionPane.showMessageDialog(this,x.getLocalizedMessage(),"Erreur à l'écriture de " + f,JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception x) {
			x.printStackTrace();
		}
		Musee t=new Musee() {
			@Override
			public void dispose() {
				super.dispose();
				System.exit(0);
			}
		};
		t.pack();
		t.setVisible(true);
	}
}
