package com.watermark;

import java.io.File;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ͼƬˮӡ���������
 * 
 * @author LiuWei
 * 
 */
public class WaterMarkAction extends ActionSupport {
	private String uploadPath;// ͼƬ�ϴ��ļ�·��

	private File[] image;// �ϴ�ͼƬ�ļ�����
	private String[] imageFileName;// �ϴ�ͼƬ��������

	private List<PicInfo> picInfos;// �ϴ�ͼƬweb����·����Ϣ

	/**
	 * ͼƬˮӡ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public String watermark() throws Exception {
		picInfos = WaterMarkService.uploadAndMark(image, imageFileName,
				uploadPath);
		return SUCCESS;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public File[] getImage() {
		return image;
	}

	public void setImage(File[] image) {
		this.image = image;
	}

	public String[] getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String[] imageFileName) {
		this.imageFileName = imageFileName;
	}

	public List<PicInfo> getPicInfos() {
		return picInfos;
	}

	public void setPicInfos(List<PicInfo> picInfos) {
		this.picInfos = picInfos;
	}

}
