package com.blog.web.util;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.blog.web.entity.HttpEntity;
import com.blog.web.entity.Record;

/**
 * 2014-09-16
 * 
 * @author WebSOS QQ:644556636 bkkill.com
 */
public class UploadUtil {
	private static final String[] suffix = { "gif", "jpg", "jpeg", "bmp", "png" };

	public static String doUpload(HttpServletRequest request) {

		Record files = UploadUtil.doUploads(request);
		if (StringUtils.isNullOrEmpty(files)) {
			return null;
		}
		for (String key : files.keySet()) {
			return (String) files.get(key);
		}
		return null;
	}

	public static Record doUploads(HttpServletRequest request) {
		try {
			List items = getItems(request);
			Record fileRec = new Record();
			String dir = request.getRealPath("/");
			String path = "upload/" + getPath();
			createDir(dir, path);
			for (Iterator it = items.iterator(); it.hasNext();) {
				try {
					FileItem item = (FileItem) it.next();
					// 判断是否为表单域
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString("UTF-8");
						continue;
					}
					// 获取文件字段名
					String fieldName = item.getFieldName();
					// 获取文件名
					String fileName = item.getName();
					// 获得文件类型
					String suffix = getSuffix(fileName);
					if (!suffix.contains(suffix)) {
						continue;
					}
					path += ("/" + JUUIDUtil.createUuid() + "." + suffix);
					if (writeFile(item, dir, path)) {
						fileRec.put(fieldName, request.getAttribute("basePath")
								+ path.replace("\\", "/"));
					}
				} catch (Exception e) {
				} finally {
				}
			}
			if (StringUtils.isNullOrEmpty(fileRec)) {
				return null;
			}
			return fileRec;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getSuffix(String fileName) {
		if (StringUtils.isNullOrEmpty(fileName)) {
			return null;
		}
		String[] strs = fileName.split("\\.");
		return strs[strs.length - 1].toLowerCase();
	}

	private static void createDir(String dir, String path) {
		try {
			String uri = dir + path;
			uri = uri.replace("\\", "/");
			if (!new File(uri).exists()) {
				System.out.println("创建文件夹：" + uri);
				new File(uri).mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String doDown(String url) throws Exception {
		HttpEntity entity = HttpUtil.Get(url);
		if (entity == null || entity.getBye() == null) {
			throw new Exception("图片下载失败");
		}
		try {
			Image srcImg = ImageIO.read(byte2Input(entity.getBye()));
			if (srcImg == null) {
				throw new Exception("图片下载失败");
			}
			String dir = System.getProperty("tansungWeb.root");
			String path = "upload/" + getPath();
			createDir(dir, path);
			path += ("/" + JUUIDUtil.createUuid() + ".jpg");
			FileUtils.writeFile(dir + path, entity.getBye());
			srcImg = ImageIO.read(new File(dir + path));
			if (srcImg == null) {
				throw new Exception("图片下载失败");
			}
			return "/" + path;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static final InputStream byte2Input(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}

	private static String getPath() {
		Calendar a = Calendar.getInstance();
		return a.get(Calendar.YEAR) + "/" + (a.get(Calendar.MONTH) + 1) + "/"
				+ (a.get(Calendar.DATE));
	}

	private static Boolean writeFile(FileItem item, String webDir, String uri) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(webDir + uri);
			InputStream is = item.getInputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) > 1) {
				fos.write(buffer, 0, len);
			}
			is.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private static List getItems(HttpServletRequest request) {
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 1024 * 20);
			factory.setRepository(new File(request.getRealPath("/")));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(1024 * 1024 * 20);
			List items = upload.parseRequest(request);
			return items;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		try {
			FileInputStream input=new FileInputStream(
					new File("d:/test/6B459B4FC0610246FD834290DE3C8126.png"));
			ImageIO.read(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
