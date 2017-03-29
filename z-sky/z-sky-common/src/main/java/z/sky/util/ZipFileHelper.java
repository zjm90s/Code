package z.sky.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * ZIP压缩工具
 * 
 * @author jianming.zhou
 *
 */
public class ZipFileHelper {

	private final static int BUF_SIZE = 1024;

	/**
	 * 创建压缩包
	 * 
	 * @param fromDir
	 *            指定目录
	 * @param toZipFile
	 *            打包后文件名
	 * @throws Exception
	 */
	public static void zipFile(File fromDir, File toZipFile) throws Exception {
		if (!fromDir.isDirectory()) {
			throw new Exception("directory not exists " + fromDir.getAbsolutePath());
		}

		try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(toZipFile))) {
			for (File picFile : fromDir.listFiles()) {
				if (picFile.getName().endsWith(".zip")) {
					continue;
				}
				ZipEntry ze = new ZipEntry(picFile.getName());
				ze.setSize(picFile.length());
				ze.setTime(picFile.lastModified());
				zout.putNextEntry(ze);
				byte[] buf = new byte[BUF_SIZE];
				int readLen = 0;
				try (InputStream in = new BufferedInputStream(new FileInputStream(picFile))) {
					while ((readLen = in.read(buf, 0, buf.length)) != -1) {
						zout.write(buf, 0, readLen);
					}
				}
			}
		}
	}

	/**
	 * 解压
	 * 
	 * @param fromZipFile
	 *            待解压文件
	 * @param toDir
	 *            解压目录
	 * @return 解压出来的文件
	 * @throws Exception
	 */
	public static List<String> unzipFile(File fromZipFile, File toDir) throws Exception {
		if (!fromZipFile.isFile()) {
			throw new Exception("file not exists " + fromZipFile.getAbsolutePath());
		}
		if (!toDir.exists()) {
			toDir.mkdirs();
		}
		List<String> fileList = new ArrayList<>();
		try (ZipFile zfile = new ZipFile(fromZipFile.getAbsoluteFile())) {
			Enumeration<?> zList = zfile.entries();
			ZipEntry ze = null;
			byte[] buf = new byte[BUF_SIZE];
			while (zList.hasMoreElements()) {
				ze = (ZipEntry) zList.nextElement();
				if (ze.isDirectory()) {
					continue;
				}

				String fileName = String.format("%s/%s", toDir.getAbsoluteFile(), ze.getName());
				fileList.add(fileName);
				FileOutputStream fout = new FileOutputStream(fileName);
				try (InputStream in = new BufferedInputStream(zfile.getInputStream(ze));
						OutputStream out = new BufferedOutputStream(fout)) {
					int readLen = 0;
					while ((readLen = in.read(buf, 0, buf.length)) != -1) {
						out.write(buf, 0, readLen);
					}
				}
			}
		}
		return fileList;
	}

	/**
	 * 文件分割
	 * 
	 * <p>
	 * zipFile.zip => zipFile.zip_1 + zipFile.zip_2 + ...
	 * 
	 * @param zipFile
	 *            待分割zip文件
	 * @param size
	 *            分割大小
	 * @return 分割得到的文件
	 * @throws Exception
	 */
	public static List<String> splitFile(File zipFile, int size) throws Exception {
		List<String> splitFileList = new ArrayList<>();
		if (!zipFile.isFile()) {
			throw new Exception("file not exists " + zipFile.getAbsolutePath());
		}
		size = size > 0 ? size : 1024 * 1024;
		String filename = zipFile.getAbsolutePath();

		byte[] buf = new byte[BUF_SIZE];
		try (FileInputStream fin = new FileInputStream(zipFile)) {
			int readsize = 0;
			int pos = 0;
			int num = 1;
			boolean isNewFile = true;
			File splFile = null;
			FileOutputStream fout = null;
			while ((readsize = fin.read(buf, 0, buf.length)) > 0) {
				if (isNewFile) {
					isNewFile = false;
					String splFileName = String.format("%s_%s", filename, num);
					splitFileList.add(splFileName);
					splFile = new File(splFileName);
					fout = new FileOutputStream(splFile);
				}
				fout.write(buf, 0, readsize);
				pos += readsize;
				if (pos > size * num) {
					num++;
					isNewFile = true;
					fout.close();
				}
			}
			if (fout != null) {
				fout.close();
			}
		}
		return splitFileList;
	}

	/**
	 * 合并文件
	 * 
	 * <p>
	 * zipFile.zip_1 + zipFile.zip_2 + ... => zipFile.zip
	 * 
	 * @param zipFile
	 *            目标文件
	 * @throws Exception
	 */
	public static void combination(File zipFile) throws Exception {
		String filename = zipFile.getAbsolutePath();
		try (FileOutputStream fout = new FileOutputStream(zipFile)) {
			int num = 1;
			File filein = null;
			FileInputStream fin = null;
			byte[] buf = new byte[BUF_SIZE];
			while (true) {
				filein = new File(String.format("%s_%s", filename, num));
				if (!filein.isFile()) {
					break;
				}
				fin = new FileInputStream(filein);
				int readsize = 0;
				while ((readsize = fin.read(buf, 0, buf.length)) > 0) {
					fout.write(buf, 0, readsize);
				}
				num++;
				fin.close();
			}
			if (fin != null) {
				fin.close();
			}
		}
	}
}
