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
 * ͼƬˮӡ������
 * 
 * @author LiuWei
 * 
 */
public class WaterMarkService {
	/**
	 * ����ˮӡ����
	 */
	private static final String MARK_TEXT = "Ľ����";
	private static final String FONT_NAME = "΢���ź�";
	private static final int FONT_STYLE = Font.BOLD;
	private static final int FONT_SIZE = 120;
	private static final Color FONT_COLOR = Color.BLACK;

	/**
	 * ˮӡ͸����
	 */
	private static final float ALPHA = 0.3F;

	/**
	 * ˮӡͼƬ����
	 */
	private static final String LOGO = "/logo/logo.png";

	/**
	 * ˮӡ����
	 */
	private static int X = 100;// ������
	private static int Y = 100;// ������

	/**
	 * �����ϴ�ͼƬ����������ˮӡ����
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
			String realUploadPath = getRealUploadPath(uploadPath);// ��ȡͼƬ�ϴ�����·��

			for (int i = 0; i < image.length; i++) {
				ret.add(uploadAndMark(image[i], imageFileName[i], uploadPath,
						realUploadPath));// ��ɵ���ͼƬ�ϴ������ˮӡ����
			}
		}

		try {

		} catch (Exception e) {
			System.out.println("�ļ�ˮӡ����ʧ��");
			e.printStackTrace();
		}

		return ret;

	}

	/**
	 * �ϴ�ͼƬ�������ͼƬ���ˮӡ����
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

		InputStream fis = null;// �ϴ�ͼƬ�ļ�������
		OutputStream fos = null;// ͼƬ�ļ������
		OutputStream wfos = null;// ˮӡͼƬ�ļ������

		try {
			String logoFileName = "logo_" + imageFileName;
			fos = new FileOutputStream(realUploadPath + "\\" + imageFileName);// ԭͼ�ļ������
			wfos = new FileOutputStream(realUploadPath + "\\" + logoFileName);// ˮӡͼƬ��������ԭ�ļ���֮ǰ��ǰ׺

			// ����ԭͼ�ļ�
			fis = new FileInputStream(image);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {// ���ԭͼ�ļ��ϴ�����
				fos.write(buffer, 0, len);
			}

			ret.setImgURL(uploadPath + "/" + imageFileName);// ����ԭͼweb����·��

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 1.�������ˮӡ
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////

			// if (markText(image, wfos)) {// ���ͼƬˮӡ�����ɹ�������ˮӡͼƬ����·�����˴�ʹ������ˮӡ����
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 2.�������ˮӡ��Ĭ��ͼƬ���Ҳ�
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// if (markTextRight(image, wfos)) {//
			// ���ͼƬˮӡ�����ɹ�������ˮӡͼƬ����·�����˴�ʹ������ˮӡ����
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 3.��Ӷ������ˮӡ
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////

			if (markTextFull(image, wfos)) {// ���ͼƬˮӡ�����ɹ�������ˮӡͼƬ����·�����˴�ʹ��ͼƬˮӡ����
				ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			}

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 4.���ͼƬˮӡ
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////

			// if (markImage(image, wfos)) {// ���ͼƬˮӡ�����ɹ�������ˮӡͼƬ����·�����˴�ʹ��ͼƬˮӡ����
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 5.���ͼƬˮӡ��Ĭ��ͼƬ�Ҳ�
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////

			// if (markImageRight(image, wfos)) {//
			// ���ͼƬˮӡ�����ɹ�������ˮӡͼƬ����·�����˴�ʹ��ͼƬˮӡ����
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

			// /////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// 6.��Ӷ��ͼƬˮӡ
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// if (markImageFull(image, wfos)) {//
			// ���ͼƬˮӡ�����ɹ�������ˮӡͼƬ����·�����˴�ʹ��ͼƬˮӡ����
			// ret.setMarkImgURL(uploadPath + "/" + logoFileName);
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * ͼƬ�������ˮӡ
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markText(File file, OutputStream fos) {
		boolean ret = false;

		try {
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// ImageIO read��ʹ��ImageReader�����ṩ��File���󣬷���BufferedImage����
			//
			// Iamge ��ʾJavaƽ̨��ʾͼ��ͼ��ĳ����࣬���ض���ƽ̨�ķ�ʽ��ȡͼ��ͨ��Image�࣬��ȡͼ���һЩ������Ϣ��
			//
			// BufferedImage��Image���һ������ʵ�֣�������Ҫ���þ��ǽ�һ��ͼƬ���ص��ڴ��С�
			// BuffferedImage���ɵ�ͼƬ���ڴ�����һ��ͼ�񻺳�����������������������Ժܷ���Ĳ������ͼƬ��
			// ͨ��������ͼƬ�޸Ĳ������С�任��ͼƬ��ҡ�����ͼƬ͸����͸���ȡ�
			//
			// BufferedImage.TYPE_INT_RGB����ʾһ��ͼ�񣬸�ͼ������������ص� 8 λ RGB ��ɫ
			//
			// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			Image image = ImageIO.read(file);// ԭͼͼƬ��Ϣ����
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// Graphics ��������ͼ�������ĵĳ�����࣬����Ӧ�ó�����������Ѿ��ڸ����豸��ʵ�֣��Լ�����ͼ���Ͻ��л��ơ� ͼ����ƹ�����
			// Graphics2D�Ƕ�Graphics�����չ���ṩ�Լ�����״������ת������ɫ������ı����ָ�Ϊ���ӵĿ��ơ�
			// ���������� Java(tm) ƽ̨�ϳ��ֶ�ά��״���ı���ͼ��Ļ����ࡣ
			//
			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Graphics2D g = bufferedImage.createGraphics();// ������ͼ������
			g.drawImage(image, 0, 0, width, height, null);// ����ԭͼ

			// ���û���������Ϣ
			g.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			g.setColor(FONT_COLOR);

			int width1 = FONT_SIZE * getTextLength(MARK_TEXT);// ���ˮӡ���ֿ��
			int height1 = FONT_SIZE;// ���ˮӡ���ָ߶�
			int widthDiff = width - width1;// ͼƬ�����ˮӡ���ֿ��֮��
			int heightDiff = height - height1;// ͼƬ�߶���ˮӡ���ָ߶�֮��

			int x = X;
			int y = Y;
			if (x > widthDiff) {// ���ˮӡ�������곬��ˮӡ������ԭͼ��Ȳ����ˮӡ���ᣬĬ��ΪͼƬ�������Ҳ�
				x = widthDiff;
			}

			if (y > heightDiff) {// ���ˮӡ�������곬��ˮӡ������ԭͼ�߶Ȳ����ˮӡ���ᣬĬ��ΪͼƬ�������²�
				y = heightDiff;
			}

			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// Composite�ӿڣ������û���ͼ��������ϻ�ͼ����Ԫ�صķ�����ʹ�øýӿ��������״���ı�����ͼ������ʹ��������ͼ�����û�ͼ��һЩ����Ԫ�ء�
			// AlphaComposite ��ʵ��һЩ������ alpha �ϳɹ��򣬽�Դɫ��Ŀ��ɫ��ϣ���ͼ�κ�ͼ����ʵ�ֻ�Ϻ�͸��Ч����
			// SRC_ATOP�����úϳɹ���Ϊ��Ŀ��ɫ�е�Դɫ���ֽ����ϳɵ�Ŀ��ɫ�У����ǽ����ǵ�ˮӡͼƬ�ϳɵ�Ŀ��ͼƬ��ALPHAֵ�����ںϳ�֮ǰ����ԭɫ����ָ����alphaֵ��
			// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// ����ˮӡ͸����
			g.drawString(MARK_TEXT, x, y + FONT_SIZE);// ����ˮӡ����
			g.dispose();

			// ////////////////////////////////////////////////////////////////////////////////////////////////////
			//
			// JPEGImageEncoder����ͼƬ���б��봦��
			// createJPEGEncoder������һ����ָ������������������յ������ˮӡ��ͼƬ�ļ����������JPEGImageEncoder����
			//
			// //////////////////////////////////////////////////////////////////////////////////////////////////
			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// ����ˮӡͼƬ
			en.encode(bufferedImage);

			ret = true;
		} catch (Exception e) {
			System.out.println("�������ˮӡʧ��");
			e.printStackTrace();
		}

		return ret;

	}

	/**
	 * ͼƬ�������ˮӡ��Ĭ��ͼƬ���Ҳ�
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markTextRight(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// ԭͼͼƬ��Ϣ����
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// ������ͼ������
			g.drawImage(image, 0, 0, width, height, null);// ����ԭͼ

			// ���û���������Ϣ
			g.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			g.setColor(FONT_COLOR);

			int width1 = FONT_SIZE * getTextLength(MARK_TEXT);// ���ˮӡ���ֿ��
			int height1 = FONT_SIZE;// ���ˮӡ���ָ߶�
			int x = width - width1;// ͼƬ�����ˮӡ���ֿ��֮��
			int y = height - height1;// ͼƬ�߶���ˮӡ���ָ߶�֮��

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// ����ˮӡ͸����
			g.drawString(MARK_TEXT, x, y + FONT_SIZE);// ����ˮӡ����
			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// ����ˮӡͼƬ
			en.encode(bufferedImage);

			ret = true;
		} catch (Exception e) {
			System.out.println("�������ˮӡʧ��");
			e.printStackTrace();
		}

		return ret;

	}

	/**
	 * ͼƬ��Ӷ������ˮӡ
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markTextFull(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// ԭͼͼƬ��Ϣ
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// ������ͼ������
			g.drawImage(image, 0, 0, width, height, null);// ����ԭͼ

			// ���û���������Ϣ
			g.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
			g.setColor(FONT_COLOR);

			int width1 = FONT_SIZE * getTextLength(MARK_TEXT);// ���ˮӡ���ֿ��
			int height1 = FONT_SIZE;// ���ˮӡ���ָ߶�

			// ////////////////////////////////////////////////////////////////////
			//
			// rotate ��תͼƬ����һ������������ת�Ƕȣ�����ĽǶȵĵ�λ�ǻ��ȣ�������ҪtoRadians��������ת����
			// ������������������ѡ�����ģ���������ΪͼƬ�����ġ�
			//
			// ///////////////////////////////////////////////////////////////////
			g.rotate(Math.toRadians(30), (double) bufferedImage.getWidth() / 2,
					(double) bufferedImage.getHeight() / 2);// ת��Ϊ�ȴ�����ȵĽǶȣ��Ի���Ϊ��λ�ĽǶ�,��ͼƬ��������ת
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// ����������Ϲ����࣬����͸���ȡ�

			int x = -width / 2;
			int y = -height / 2;

			while (x < width * 1.5) {// ͨ��ѭ��������ԭͼ����Ӷ��ˮӡͼƬ
				y = -height / 2;
				while (y < height * 1.5) {
					g.drawString(MARK_TEXT, x, y + FONT_SIZE);// ����ˮӡ����
					y = y + height1 + 300;
				}

				x = x + width1 + 300;
			}

			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// ����ˮӡͼƬ
			en.encode(bufferedImage);

			ret = true;

		} catch (Exception e) {
			System.out.println("��ӡͼƬˮӡͼƬ����ʧ��");
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * ͼƬ���ͼƬˮӡ
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markImage(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// ԭͼͼƬ��Ϣ
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// ������ͼ������
			g.drawImage(image, 0, 0, width, height, null);// ����ԭͼ

			File logo = new File(getRealUploadPath(LOGO));// ˮӡͼƬ����
			Image imageLogo = ImageIO.read(logo);
			int width1 = imageLogo.getWidth(null);
			int height1 = imageLogo.getHeight(null);

			int widthDiff = width - width1;// ˮӡͼƬ��ԭͼ��Ȳ�
			int heightDiff = height - height1;// ˮӡͼƬ��ԭͼ�߶Ȳ�

			int x = X;
			int y = Y;
			if (x > widthDiff) {// ���ˮӡ�������곬��ˮӡͼƬ��ԭͼ��Ȳ����ˮӡ���ᣬĬ��ΪͼƬ�������Ҳ�
				x = widthDiff;
			}

			if (y > heightDiff) {// ���ˮӡ�������곬��ˮӡͼƬ��ԭͼ�߶Ȳ����ˮӡ���ᣬĬ��ΪͼƬ�������²�
				y = heightDiff;
			}

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// ����������Ϲ����࣬����͸���ȡ�

			g.drawImage(imageLogo, x, y, null);// ����ˮӡͼƬ
			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// ����ˮӡͼƬ
			en.encode(bufferedImage);

			ret = true;

		} catch (Exception e) {
			System.out.println("��ӡͼƬˮӡ����ʧ��");
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * ͼƬ���ͼƬˮӡ��Ĭ����ͼƬ�����½�
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markImageRight(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// ԭͼͼƬ��Ϣ
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// ������ͼ������
			g.drawImage(image, 0, 0, width, height, null);// ����ԭͼ

			File logo = new File(getRealUploadPath(LOGO));// ˮӡͼƬ����
			Image imageLogo = ImageIO.read(logo);
			int width1 = imageLogo.getWidth(null);
			int height1 = imageLogo.getHeight(null);

			int x = width - width1;// ˮӡͼƬ��ԭͼ��Ȳ�
			int y = height - height1;// ˮӡͼƬ��ԭͼ�߶Ȳ�

			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					ALPHA));// ����������Ϲ����࣬����͸���ȡ�

			g.drawImage(imageLogo, x, y, null);// ����ˮӡͼƬ
			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// ����ˮӡͼƬ
			en.encode(bufferedImage);

			ret = true;

		} catch (Exception e) {
			System.out.println("��ӡͼƬˮӡͼƬ����ʧ��");
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * ͼƬ��Ӷ��ͼƬˮӡ
	 * 
	 * @param file
	 * @param fos
	 * @return
	 */
	public static boolean markImageFull(File file, OutputStream fos) {
		boolean ret = false;

		try {
			Image image = ImageIO.read(file);// ԭͼͼƬ��Ϣ
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics2D g = bufferedImage.createGraphics();// ������ͼ������
			g.drawImage(image, 0, 0, width, height, null);// ����ԭͼ

			File logo = new File(getRealUploadPath(LOGO));// ˮӡͼƬ����
			Image imageLogo = ImageIO.read(logo);
			int width1 = imageLogo.getWidth(null);
			int height1 = imageLogo.getHeight(null);

			g.rotate(Math.toRadians(30), (double) bufferedImage.getWidth() / 2,
					(double) bufferedImage.getHeight() / 2);// ת��Ϊ�ȴ�����ȵĽǶȣ��Ի���Ϊ��λ�ĽǶ�,��ͼƬ��������ת
			// g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
			// ALPHA));// ����������Ϲ����࣬����͸���ȡ�

			int x = -width / 2;
			int y = -height / 2;

			while (x < width * 1.5) {// ͨ��ѭ��������ԭͼ����Ӷ��ˮӡͼƬ
				y = -height / 2;
				while (y < height * 1.5) {
					g.drawImage(imageLogo, x, y, null);// ����ˮӡͼƬ
					y = y + height1 + 200;
				}

				x = x + width1 + 200;
			}

			g.dispose();

			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fos);// ����ˮӡͼƬ
			en.encode(bufferedImage);

			ret = true;

		} catch (Exception e) {
			System.out.println("��ӡͼƬˮӡͼƬ����ʧ��");
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * ���ļ����·��ת��Ϊ����·��
	 * 
	 * @param uploadPath
	 * @return
	 */
	private static String getRealUploadPath(String uploadPath) {
		return ServletActionContext.getServletContext().getRealPath(uploadPath);
	}

	/**
	 * �����ı�����ı����
	 * 
	 * @param text
	 * @return
	 */
	private static int getTextLength(String text) {
		int textLength = text.length();
		int length = textLength;

		for (int i = 0; i < textLength; i++) {// �������ı��Ĵ���һ�������ַ������ֽ�
			if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
				length++;
			}
		}
		return (length % 2 == 0) ? length / 2 : length / 2 + 1;
	}

}
