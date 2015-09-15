package com.watermark;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片水印服务类
 * 
 * @author LiuWei
 * 
 */
public class WaterMarkService {
	/**
	 * 文字水印参数
	 */
	private static final String MARK_TEXT = "慕课网";
	private static final String FONT_NAME = "微软雅黑";
	private static final int FONT_STYLE = Font.BOLD;
	private static final int FONT_SIZE = 120;
	private static final Color FONT_COLOR = Color.BLACK;

	/**
	 * 水印透明度
	 */
	private static final float ALPHA = 0.3F;

	/**
	 * 水印图片参数
	 */
	private static final String LOGO = "/logo/logo.png";

	/**
	 * 水印坐标
	 */
	private static int X = 100;// 横坐标
	private static int Y = 100;// 纵坐标

	/**
	 * 批量上传图片，并完成添加水印处理
	 * 
	 * @param image
	 * @param imageFileName
	 * @param uploadPath
	 * @return
	 */
	public static List<PicInfo> uploadAndMark(File[] image,
			String[] imageFileName, String uploadPath) {

		List<PicInfo> ret = new ArrayList<PicInfo>();

		if (image != null && image.length > 0) {
			String realUploadPath = getRealUploadPath(uploadPath);// 获取图片上传绝对路径

			for (int i = 0; i < image.length; i++) {
				ret.add(uploadAndMark(image[i], imageFileName[i], uploadPath,
						realUploadPath));// 完成单张图片上传与添加水印操作
			}
		}

		try {

		} catch (Exception e) {
			System.out.println("文件水印操作失败");
			e.printStackTrace();
		}

		return ret;

	}

	/**
	 * 上传图片，并完成图片添加水印操作
	 * 
	 * @param image
	 * @param imageFileName
	 * @param uploadPath
	 * @param realUploadPath
	 * @return
	 */
	private static PicInfo uploadAndMark(File image, String imageFileName,
			String uploadPath, String realUploadPath) {
		PicInfo ret = new PicInfo();

		InputStream fis = null;// 上传图片文件输入流
		OutputStream fos = null;// 图片文件输出流
		OutputStream wfos = null;// 水印图片文件输出流

		try {
			String logoFileName = "logo_" + imageFileName;
			fos = new FileOutputStream(realUploadPath + "\\" + imageFileName);// 原图文件输出流
			wfos = new FileOutputStream(realUploadPath + "\\" + logoFileName);// 水印图片输入流，原文件名之前加前缀

			// 保存原图文件
			fis = new FileInputStream(image);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {// 完成原图文件上传保存
				fos.write(buffer, 0, len);
			}

			ret.setImgURL(uploadPath + "/" + imageFileName);// 设置原图web访问路径

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 1.添加文字水印
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////

			// if (markText(image, wfos)) {// 如果图片水印操作成功，设置水印图片访问路径，此处使用文字水印操作
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 2.添加文字水印。默认图片最右侧
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// if (markTextRight(image, wfos)) {//
			// 如果图片水印操作成功，设置水印图片访问路径，此处使用文字水印操作
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 3.添加多个文字水印
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////

			if (markTextFull(image, wfos)) {// 如果图片水印操作成功，设置水印图片访问路径，此处使用图片水印操作
				ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			}

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 4.添加图片水印
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////

			// if (markImage(image, wfos)) {// 如果图片水印操作成功，设置水印图片访问路径，此处使用图片水印操作
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 5.添加图片水印，默认图片右侧
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////

			// if (markImageRight(image, wfos)) {//
			// 如果图片水印操作成功，设置水印图片访问路径，此处使用图片水印操作
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 6.添加多个图片水印
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// if (markImageFull(image, wfos)) {//
			// 如果图片水印操作成功，设置水印图片访问路径，此处使用图片水印操作
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 图片添加文字水印
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markText(File file, OutputStream fos) {
		boolean ret = false;

		try {
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// ImageIO read，使用ImageReader解码提供的File对象，返回BufferedImage对象。
			//
			// Iamge 表示Java平台表示图形图像的抽象类，以特定于平台的方式获取图像，通过Image类，获取图像的一些参数信息。
			//
			// BufferedImage是Image类的一个具体实现，该类主要作用就是将一幅图片加载到内存中。
			// BuffferedImage生成的图片在内存中有一个图像缓冲区，利用这个缓冲区，可以很方便的操作这个图片。
			// 通常用来做图片修改操作如大小变换、图片变灰、设置图片透明或不透明等。
			//
			// BufferedImage.TYPE_INT_RGB，表示一个图像，该图像具有整数像素的 8 位 RGB 颜色
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			Image image = ImageIO.read(file);// 原图图片信息处理
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// Graphics 类是所有图形上下文的抽象基类，允许应用程序在组件（已经在各种设备上实现）以及闭屏图像上进行绘制。 图像绘制工具类
			// Graphics2D是对Graphics类的扩展，提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制。
			// 它是用于在 Java(tm) 平台上呈现二维形状、文本和图像的基础类。
			//
			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Graphics2D g = bufferedImage.createGraphics();// 创建绘图工具类
			g.drawImage(image, 0, 0, width, height, null);// 绘制原图

			// 设置绘制文字信息
			g.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			g.setColor(FONT_COLOR);

			int width1 = FONT_SIZE * getTextLength(MARK_TEXT);// 获得水印文字宽度
			int height1 = FONT_SIZE;// 获得水印文字高度
			int widthDiff = width - width1;// 图片宽度与水印文字宽度之差
			int heightDiff = height - height1;// 图片高度与水印文字高度之差

			int x = X;
			int y = Y;
			if (x > widthDiff) {// 如果水印横轴坐标超过水印文字与原图宽度差，调整水印横轴，默认为图片横轴最右侧
				x = widthDiff;
			}

			if (y > heightDiff) {// 如果水印纵轴坐标超过水印文字与原图高度差，调整水印纵轴，默认为图片纵轴最下侧
				y = heightDiff;
			}

			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// Composite接口，定义用基础图形区域组合绘图基本元素的方法。使用该接口来组合形状，文本或者图像。我们使用它来对图像设置绘图的一些基本元素。
			// AlphaComposite 类实现一些基本的 alpha 合成规则，将源色与目标色组合，在图形和图像中实现混合和透明效果。
			// SRC_ATOP，设置合成规则为，目标色中的源色部分将被合成到目标色中，就是将我们的水印图片合成到目标图片。ALPHA值，是在合成之前，将原色乘以指定的alpha值。
			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// 设置水印透明度
			g.drawString(MARK_TEXT, x, y + FONT_SIZE);// 绘制水印文字
			g.dispose();

			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// JPEGImageEncoder，对图片进行编码处理。
			// createJPEGEncoder，创建一个和指定输出流关联（和最终的添加了水印的图片文件输出流）的JPEGImageEncoder对象。
			//
			// //////////////////////////////////////////////////////////////////////////////////////////////////
			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// 生成水印图片
			en.encode(bufferedImage);

			ret = true;
		} catch (Exception e) {
			System.out.println("添加文字水印失败");
			e.printStackTrace();
		}

		return ret;

	}

	/**
	 * 图片添加文字水印，默认图片最右侧
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markTextRight(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// 原图图片信息处理
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// 创建绘图工具类
			g.drawImage(image, 0, 0, width, height, null);// 绘制原图

			// 设置绘制文字信息
			g.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			g.setColor(FONT_COLOR);

			int width1 = FONT_SIZE * getTextLength(MARK_TEXT);// 获得水印文字宽度
			int height1 = FONT_SIZE;// 获得水印文字高度
			int x = width - width1;// 图片宽度与水印文字宽度之差
			int y = height - height1;// 图片高度与水印文字高度之差

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// 设置水印透明度
			g.drawString(MARK_TEXT, x, y + FONT_SIZE);// 绘制水印文字
			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// 生成水印图片
			en.encode(bufferedImage);

			ret = true;
		} catch (Exception e) {
			System.out.println("添加文字水印失败");
			e.printStackTrace();
		}

		return ret;

	}

	/**
	 * 图片添加多个文字水印
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markTextFull(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// 原图图片信息
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// 创建绘图工具类
			g.drawImage(image, 0, 0, width, height, null);// 绘制原图

			// 设置绘制文字信息
			g.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			g.setColor(FONT_COLOR);

			int width1 = FONT_SIZE * getTextLength(MARK_TEXT);// 获得水印文字宽度
			int height1 = FONT_SIZE;// 获得水印文字高度

			// ////////////////////////////////////////////////////////////////////
			//
			// rotate 旋转图片，第一个参数，是旋转角度，这里的角度的单位是弧度，所以需要toRadians方法进行转换。
			// 后面两个参数，设置选择中心，这里设置为图片的中心。
			//
			// ///////////////////////////////////////////////////////////////////
			g.rotate(Math.toRadians(30), (double) bufferedImage.getWidth() / 2,
					(double) bufferedImage.getHeight() / 2);// 转换为度大致相等的角度，以弧度为单位的角度,以图片的中心旋转
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// 设置像素组合工具类，设置透明度。

			int x = -width / 2;
			int y = -height / 2;

			while (x < width * 1.5) {// 通过循环处理，在原图上添加多个水印图片
				y = -height / 2;
				while (y < height * 1.5) {
					g.drawString(MARK_TEXT, x, y + FONT_SIZE);// 绘制水印文字
					y = y + height1 + 300;
				}

				x = x + width1 + 300;
			}

			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// 生成水印图片
			en.encode(bufferedImage);

			ret = true;

		} catch (Exception e) {
			System.out.println("打印图片水印图片操作失败");
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 图片添加图片水印
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markImage(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// 原图图片信息
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// 创建绘图工具类
			g.drawImage(image, 0, 0, width, height, null);// 绘制原图

			File logo = new File(getRealUploadPath(LOGO));// 水印图片处理
			Image imageLogo = ImageIO.read(logo);
			int width1 = imageLogo.getWidth(null);
			int height1 = imageLogo.getHeight(null);

			int widthDiff = width - width1;// 水印图片与原图宽度差
			int heightDiff = height - height1;// 水印图片与原图高度差

			int x = X;
			int y = Y;
			if (x > widthDiff) {// 如果水印横轴坐标超过水印图片与原图宽度差，调整水印横轴，默认为图片横轴最右侧
				x = widthDiff;
			}

			if (y > heightDiff) {// 如果水印纵轴坐标超过水印图片与原图高度差，调整水印纵轴，默认为图片纵轴最下侧
				y = heightDiff;
			}

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// 设置像素组合工具类，设置透明度。

			g.drawImage(imageLogo, x, y, null);// 绘制水印图片
			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// 生成水印图片
			en.encode(bufferedImage);

			ret = true;

		} catch (Exception e) {
			System.out.println("打印图片水印操作失败");
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 图片添加图片水印，默认在图片的右下角
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markImageRight(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// 原图图片信息
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// 创建绘图工具类
			g.drawImage(image, 0, 0, width, height, null);// 绘制原图

			File logo = new File(getRealUploadPath(LOGO));// 水印图片处理
			Image imageLogo = ImageIO.read(logo);
			int width1 = imageLogo.getWidth(null);
			int height1 = imageLogo.getHeight(null);

			int x = width - width1;// 水印图片与原图宽度差
			int y = height - height1;// 水印图片与原图高度差

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// 设置像素组合工具类，设置透明度。

			g.drawImage(imageLogo, x, y, null);// 绘制水印图片
			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// 生成水印图片
			en.encode(bufferedImage);

			ret = true;

		} catch (Exception e) {
			System.out.println("打印图片水印图片操作失败");
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 图片添加多个图片水印
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markImageFull(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// 原图图片信息
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// 创建绘图工具类
			g.drawImage(image, 0, 0, width, height, null);// 绘制原图

			File logo = new File(getRealUploadPath(LOGO));// 水印图片处理
			Image imageLogo = ImageIO.read(logo);
			int width1 = imageLogo.getWidth(null);
			int height1 = imageLogo.getHeight(null);

			g.rotate(Math.toRadians(30), (double) bufferedImage.getWidth() / 2,
					(double) bufferedImage.getHeight() / 2);// 转换为度大致相等的角度，以弧度为单位的角度,以图片的中心旋转
			// g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
			// ALPHA));// 设置像素组合工具类，设置透明度。

			int x = -width / 2;
			int y = -height / 2;

			while (x < width * 1.5) {// 通过循环处理，在原图上添加多个水印图片
				y = -height / 2;
				while (y < height * 1.5) {
					g.drawImage(imageLogo, x, y, null);// 绘制水印图片
					y = y + height1 + 200;
				}

				x = x + width1 + 200;
			}

			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// 生成水印图片
			en.encode(bufferedImage);

			ret = true;

		} catch (Exception e) {
			System.out.println("打印图片水印图片操作失败");
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 将文件相对路径转化为绝对路径
	 * 
	 * @param uploadPath
	 * @return
	 */
	private static String getRealUploadPath(String uploadPath) {
		return ServletActionContext.getServletContext().getRealPath(uploadPath);
	}

	/**
	 * 根据文本获得文本宽度
	 * 
	 * @param text
	 * @return
	 */
	private static int getTextLength(String text) {
		int textLength = text.length();
		int length = textLength;

		for (int i = 0; i < textLength; i++) {// 对中文文本的处理，一个中文字符两个字节
			if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
				length++;
			}
		}
		return (length % 2 == 0) ? length / 2 : length / 2 + 1;
	}

}
