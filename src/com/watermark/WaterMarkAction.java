package com.watermark;

import java.io.File;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 图片水印处理控制器
 * 
 * @author LiuWei
 * 
 */
public class WaterMarkAction extends ActionSupport {
	private String uploadPath;// 图片上传文件路径

	private File[] image;// 上传图片文件数组
	private String[] imageFileName;// 上传图片名称数组

	private List<PicInfo> picInfos;// 上传图片web访问路径信息

	/**
	 * 图片水印操作
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
