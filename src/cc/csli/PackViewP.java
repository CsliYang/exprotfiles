package cc.csli;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

@SuppressWarnings("serial")
public class PackViewP extends JFrame {

	private static final int FILE_OPEN_CHOOSE = 1;
	private static final int FILE_SAVE_CHOOSE = 2;

	private static final int INPUT_PATH = 1;
	private static final int OUTPUT_PATH = 2;
	private JButton jb = new JButton();
	private JButton jb1 = new JButton();
	private JButton jb2 = new JButton();

	private String inputPath = "D:\\work\\workspace\\exportfiles"; // 请修改成自己项目的绝对路径
	private String outputPath = "D:\\exportdir\\";// 请修改成自定义的目录
	// private JLabel jl = new JLabel("打包: ");
	private JLabel jl0 = new JLabel();
	private JButton cancel = new JButton("退出");
	private JTextPane jText1 = new JTextPane();
	private JTextPane jText2 = new JTextPane();

	private PackViewP() {
		this.setTitle("打包工具");
		this.setBounds(400, 400, 380, 180);
		this.setLayout(null);
		this.setResizable(false);
		jb.setText("打包清单");
		jb1.setText("程序根目录");
		jb2.setText("输出目录");

		jText1.setText(inputPath);
		jText2.setText(outputPath);

		jb.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				if (packs()) {
					jl0.setText("成功打包！");
					jb.setText("...继续");
				} else {
					jl0.setText("打包失败！");
				}
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		jb1.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				choosePath(INPUT_PATH);
				jText1.setText(inputPath);
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});
		jb2.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				choosePath(OUTPUT_PATH);
				jText2.setText(outputPath);
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		cancel.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				close();
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		jb1.setBounds(10, 5, 100, 30);
		jText1.setBounds(120, 5, 250, 30);
		// jText1.setEnabled(false);

		jb2.setBounds(10, 40, 100, 30);
		jText2.setBounds(120, 40, 250, 30);
		// jText2.setEditable(false);

		jb.setBounds(10, 100, 100, 30);
		cancel.setBounds(120, 100, 100, 30);

		jl0.setBounds(230, 100, 100, 30);

		this.add(jb1);
		this.add(jText1);
		this.add(jb2);
		this.add(jText2);
		this.add(jb);
		this.add(cancel);
		this.add(jl0);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	@SuppressWarnings("static-access")
	private List<String> chooseFile(int chooseMode) {
		try {
			JFileChooser fileChooser;
			File[] fileName;
			fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.setDialogTitle("文件打包");
			fileChooser.setDragEnabled(true);
			fileChooser.setAutoscrolls(true);
			// fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if (f.isDirectory())
						return true;

					if (f.getName().endsWith(".TXT") || f.getName().endsWith(".txt")) {
						return true;
					}
					return false;
				}

				public String getDescription() {
					return ".txt";
				}
			});

			fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());

			// fileChooser.setBackground(Color.GRAY);
			fileChooser.setOpaque(true);
			fileChooser.setDoubleBuffered(true);
			int returnVal = -1;
			switch (chooseMode) {
			case FILE_OPEN_CHOOSE:
				returnVal = fileChooser.showOpenDialog(this);
				break;
			case FILE_SAVE_CHOOSE:
				returnVal = fileChooser.showSaveDialog(this);
				break;
			}
			if (returnVal == fileChooser.APPROVE_OPTION)
				fileName = fileChooser.getSelectedFiles();
			else
				fileName = null;
			List<String> list = new ArrayList<String>();
			System.out.println("打包文件路径列表：");
			String filePath = null;
			for (int i = 0; i < fileName.length; i++) {
				filePath = fileName[i].getAbsolutePath();
				if (filePath.toUpperCase().endsWith("TXT")) {
					list.add(filePath);
					System.out.println("序号   " + i + "   " + filePath);
				} else {
					System.out.println("序号   " + i + "   " + filePath + " >>该文件不能作为打包文件!   ");
				}
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 选择文件夹路径
	 * 
	 * @return
	 */
	private boolean choosePath(int id) {
		try {
			JFileChooser fileChooser;
			fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			switch (id) {
			case INPUT_PATH:
				fileChooser.setDialogTitle("打包文件根目录");
				fileChooser.setCurrentDirectory(new File(inputPath));
				break;
			case OUTPUT_PATH:
				fileChooser.setDialogTitle("输出文件目录");
				fileChooser.setCurrentDirectory(new File(outputPath));
				break;

			}

			fileChooser.setDragEnabled(true);
			fileChooser.setAutoscrolls(true);
			fileChooser.setAcceptAllFileFilterUsed(true);
			fileChooser.setOpaque(true);
			fileChooser.setDoubleBuffered(true);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			fileChooser.showOpenDialog(this);

			switch (id) {
			case INPUT_PATH:
				inputPath = fileChooser.getSelectedFile().toString();
				break;
			case OUTPUT_PATH:
				outputPath = fileChooser.getSelectedFile().toString();
				break;

			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void close() {
		this.dispose();
	}

	private boolean packs() {
		boolean flag = true;
		List<String> fileName = chooseFile(1);
		if (fileName == null || fileName.size() <= 0) {
			System.out.println("打包原始文件没有找到");
			flag = false;

		} else {
			for (int i = 0; i < fileName.size(); i++) {
				try {
					flag = FileUtil.becomePackage(fileName.get(i), inputPath, outputPath, this);
				} catch (Exception e) {
					return false;
				}
			}
		}
		return flag;
	}

	public static void main(String[] args) {
		new PackViewP();
	}
}
