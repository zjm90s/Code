package z.sky.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片压缩工具
 * @author jianming.zhou
 *
 */
public class ImageUtil {

	private static final int w = 1000;
	private static final int h = 800;

	public static void compress(String srcFilePath) throws IOException {
		compress(srcFilePath, srcFilePath);
	}

	public static void compress(String srcFile, String descFile) throws IOException {
		File file = new File(srcFile);
		Image img = ImageIO.read(file);
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		if (width / height > w / h) {
			resizeByWidth(img, descFile, width, height, w);
		} else {
			resizeByHeight(img, descFile, width, height, h);
		}
	}

	private static void resizeByWidth(Image img, String descFile, int width, int height, int w) throws IOException {
		int h = height * w / width;
		resize(img, descFile, w, h);
	}

	private static void resizeByHeight(Image img, String descFile, int width, int height, int h) throws IOException {
		int w = width * h / height;
		resize(img, descFile, w, h);
	}

	private static void resize(Image img, String descFile, int w, int h) throws IOException  {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().drawImage(img, 0, 0, w, h, null);
		File destFile = new File(descFile);
		try (FileOutputStream out = new FileOutputStream(destFile)) {
			ImageIO.write(image, "jpeg", out);
		} catch (IOException e) {
			throw e;
		}
	}

}
