package cc.csli;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FileUtil {

	private static String getDate(Date date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(date).toString();
	}

	private static BufferedReader getBufferedReader(String path) throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(path)));
	}

	@SuppressWarnings("static-access")
	private static boolean copy(String from, String dirPath, JFrame root) {
		boolean isCommon = true;
		File fromFile = new File(from);
		if (!fromFile.exists())
			return false;
		makeDirs(dirPath);
		try {
			File toFile = new File(dirPath + File.separatorChar + fromFile.getName());
			if (toFile.exists()) {
				int k = checkFileVersion(fromFile, toFile);
				if (k == -1) {
					System.out.println("文件版本在目标版本之前，处理为不覆盖！若要处理请人工处理！" + "\n原文件:" + fromFile.getAbsolutePath() + "\n目标文件:" + toFile.getAbsolutePath());
					JOptionPane jp = new JOptionPane();
					jp.setBounds(new Rectangle(new Point(400, 400)));

					int isYes = jp.showConfirmDialog(root, "发现相同的文件，文件版本在目标版本之前！是否要进行覆盖？" + "\n当前文件：" + fromFile.getAbsolutePath() + "，修改日期:" + getDate(new Date(fromFile.lastModified())) + "\n目标文件：" + toFile.getAbsolutePath() + "，修改日期:" + getDate(new Date(toFile.lastModified())));
					if (isYes == 0) {
						isCommon = false;
						System.out.println("您选择了是！");
					} else {
						return false;
					}
				}

				if (k == 0) {
					System.out.println("相同文件重复，处理为不覆盖！若要处理请人工处理！" + "\n原文件:" + fromFile.getAbsolutePath() + "\n目标文件:" + toFile.getAbsolutePath());
					return true;
				}
				if (k == 1)
					isCommon = false;
			}
			if (!toFile.exists()) {
				toFile.createNewFile();
				isCommon = false;
			}
			if (!isCommon) {
				InputStream is = new FileInputStream(fromFile);
				OutputStream out = new FileOutputStream(toFile);
				byte[] b = new byte[1024];
				int len = -1;
				while ((len = is.read(b)) != -1) {
					out.write(b, 0, len);
				}
				out.flush();
				out.close();
				is.close();
				toFile.setLastModified(fromFile.lastModified());

				return true;
			}
		} catch (Exception e) {
			System.out.println("Copy Error!");
		}

		return false;
	}

	private static void makeDirs(String path) {
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
	}

	private static int checkFileVersion(File file1, File file2) {
		long file1LastTime = file1.lastModified();
		long file2LastTime = file2.lastModified();
		if (file1LastTime > file2LastTime)
			return 1;
		if (file1LastTime < file2LastTime)
			return -1;
		return 0;
	}

	public static boolean becomePackage(String fileList, String cutStr, String dir, JFrame root) throws Exception {
		// String dir = "E:\\生成包文件夹\\";
		dir = dir + "\\";
		// String cutStr = "E:\\RUITAI\\rtlis";
		String filePath = null;
		String addStr = null;
		String fromFile = null;
		String toFile = null;
		boolean flag = false;
		try {
			// System.out.println("fileList:"+fileList);
			BufferedReader br = FileUtil.getBufferedReader(fileList);
			addStr = br.readLine();
			addStr = addStr.trim();
			while ((filePath = br.readLine()) != null) {
				if ("".equals(filePath.trim()))
					continue;
				filePath = filePath.replaceAll("/", "\\\\");
				// if (!(filePath.startsWith("E") || filePath.startsWith("java")
				// || filePath
				// .startsWith("ui")))
				// continue;
				System.out.println(filePath.replaceAll("\\\\", "/"));
				if (filePath.startsWith(cutStr)) {
					fromFile = filePath.trim();
					toFile = dir + addStr + File.separatorChar + getCenter(cutStr, fromFile);
					copy(fromFile, toFile, root);

				} else {

					fromFile = cutStr + File.separatorChar + filePath.trim();
					toFile = dir + addStr + File.separatorChar + filePath.substring(0, filePath.trim().lastIndexOf("\\"));
					/*
					 * System.out.println("From:"+fromFile);
					 * System.out.println("toFile:"+toFile);
					 */
					flag = copy(fromFile, toFile, root);
				}
			}
			br.close();
			return flag;
		} catch (FileNotFoundException e) {
			System.out.println("列表文件没有找到！");
			throw new Exception("列表文件没有找到！");
		} catch (IOException e) {
			System.out.println("列表文件读取出错！");
			throw new Exception("列表文件读取出错！");
		}
	}

	private static String getCenter(String flag, String message) {
		int k1 = message.trim().indexOf(flag);
		int k2 = message.trim().lastIndexOf("\\");
		if (k1 != -1 && k2 != -1)
			return message.substring(flag.length() + 1, k2 + 1);
		return null;
	}

}
