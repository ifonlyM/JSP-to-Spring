package net.ifonlygaram.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import net.coobird.thumbnailator.Thumbnailator;
import net.ifonlygaram.domain.Attach;

public class FileUpload {
	public List<Attach> upload(HttpServletRequest req) throws IOException {
		
		String saveDirectory = CommonConst.UPLOAD_PATH;
		
		int maxPostSize = 10 * 1024 * 1024;
		String encoding = "utf-8";
		FileRenamePolicy policy = new MyFileRenamePolicy();
		MultipartRequest multi = new MultipartRequest(req, saveDirectory, maxPostSize, encoding, policy);
		
		Enumeration<String> files = multi.getFileNames();
		List<Attach> attachs = new ArrayList<>();
		while(files.hasMoreElements()) {
			String file = files.nextElement();
			String uuid = multi.getFilesystemName(file); // renamepolicy로 변경된 이름
			if(uuid == null) continue;
			String origin = multi.getOriginalFileName(file); // 원래 파일 이름
			
//			Attach attach = new Attach(uuid, origin, null);
//			attachs.add(attach);
			
			// thumbnail
			FileInputStream fis = new FileInputStream(saveDirectory + "\\" + uuid);
			FileOutputStream fos = new FileOutputStream(saveDirectory + "\\s_" + uuid);
			Thumbnailator.createThumbnail(fis, fos, 250, 250);
		}
		return attachs;
	}
}
