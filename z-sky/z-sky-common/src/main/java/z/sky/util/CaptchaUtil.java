package z.sky.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码生成工具
 *
 */
public class CaptchaUtil {

	/** 验证码图片的宽度 */
	private static int width = 70;

	/** 验证码图片的高度 */
	private static int height = 30;

	/** 验证码字符个数 */
	private static int codeCount = 4;

	/** 生成随机数的水平距离 */
	private static int codeX = width / (codeCount + 2);
	
	/** 生成随机数的垂直距离 */
	private static int codeY = height - 8;

	/** 字体高度 */
	private static int fontHeight = height - 12;

	/** 字符序列 */
	private static String codeSequence = "abcdefghijklmnopqrstuvwxyZABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";

	/**
	 * 生成验证码字符
	 * @return
	 */
	public static String genCode() {
		Random random = new Random();
		StringBuffer randCode = new StringBuffer();
		for (int i = 0; i < codeCount; i++) {
			int nextInt = random.nextInt(codeSequence.length());
			String str = String.valueOf(codeSequence.charAt(nextInt));
			randCode.append(str);
		}
		return randCode.toString();
	}
	
	/**
	 * 生成验证码图片
	 * @param randCode
	 * @return
	 */
	public static BufferedImage genImage(String randCode) {
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D grap = buffImg.createGraphics();

		// 填充背景
		grap.setColor(Color.WHITE);
		grap.fillRect(0, 0, width, height);

		// 字体
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		grap.setFont(font);

		// 边框
		grap.setColor(Color.BLACK);
		grap.drawRect(0, 0, width - 1, height - 1);

		Random random = new Random();
		
		// 产生随机干扰线
		grap.setColor(Color.BLACK);
		for (int i = 0; i < 16; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			grap.drawLine(x, y, x + xl, y + yl);
		}
		
		// 绘制验证码
		for (int i = 0; i < codeCount; i++) {
			int red = random.nextInt(125);
			int green = random.nextInt(255);
			int blue = random.nextInt(200);

			grap.setColor(new Color(red, green, blue));
			grap.drawString(String.valueOf(randCode.charAt(i)), (i + 1) * codeX, codeY);
		}
		
		return buffImg;
	}

}
