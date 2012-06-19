package jargrep;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicDirectoryModel;

@SuppressWarnings("serial")
public class JarGrepGUI extends JFrame {

	private class JarFileFilter extends FileFilter {
		FileNameExtensionFilter f = new FileNameExtensionFilter(
				"Java JAR archive file", "jar", "zip");

		@Override
		public String getDescription() {
			return "Java JAR files";
		}

		@Override
		public boolean accept(File file) {
			return !file.isDirectory() && f.accept(file);
		}
	}

	private JList jarFiles = new JList();
	private JScrollPane jarFilesScrollPane;

	private JList classes = new JList();
	private JScrollPane classesScrollPane;
	private DefaultListModel classesModel = new DefaultListModel();

	private JFileChooser directoryChooser = new JFileChooser();
	private JButton chooseFolder = new JButton("Choose folder");

	public JarGrepGUI() {
		super("JarGrepGUI");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		jarFilesScrollPane = new JScrollPane(jarFiles);
		add(jarFilesScrollPane);

		directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		changeJarFilesListDirectory(new File(System.getProperty("user.home",
				".")));
		jarFiles.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		jarFiles.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		jarFiles.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				classesModel.removeAllElements();

				if (event.getValueIsAdjusting()
						|| jarFiles.getSelectedValue() == null) {
					return;
				}

				File path = new File(jarFiles.getSelectedValue().toString());

				if (path.isDirectory()) {
					return;
				}

				try {
					for (String s : JarUtil.getClassesInJarFile(path)) {
						classesModel.addElement(s);
					}

					setSizeToPreferredSize(classesScrollPane);
//					setSizeToPreferredSize(JarGrepGUI.this);

				} catch (IOException exception) {
					JOptionPane.showMessageDialog(JarGrepGUI.this,
							exception.getLocalizedMessage(),
							"Error opening JAR file", JOptionPane.ERROR_MESSAGE);
					exception.printStackTrace();

				}
			}
		});
		setSizeToPreferredSize(jarFiles);

		chooseFolder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int result = directoryChooser.showOpenDialog(JarGrepGUI.this);

				if (result == JFileChooser.APPROVE_OPTION) {
					changeJarFilesListDirectory(directoryChooser
							.getSelectedFile());
					setSizeToPreferredSize(jarFiles);
//					setSizeToPreferredSize(JarGrepGUI.this);
				}
			}
		});
		add(chooseFolder);

		classesScrollPane = new JScrollPane(classes);
		add(classesScrollPane);
		classes.setModel(classesModel);
//		setSizeToPreferredSize(classes);

		setSizeToPreferredSize(this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new JarGrepGUI().setVisible(true);
	}

	private void changeJarFilesListDirectory(File dir) {
		JFileChooser fileChooser = new JFileChooser(dir);
		fileChooser.setFileFilter(new JarFileFilter());
		jarFiles.setModel(new BasicDirectoryModel(fileChooser));
	}

	protected static void setSizeToPreferredSize(Container c) {
		c.setSize(c.getPreferredSize());
	}
}
