package net.ifonlygaram.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;

import lombok.Data;

@Data
public class MutipartParam {
	private String saveDirectory = CommonConst.UPLOAD_PATH;
	private int maxPostSize = 10 * 1024 * 1024;
	private String encoding = "utf-8";
	private FileRenamePolicy policy = new MyFileRenamePolicy();
	
	public File uploadPath() {
		File uploadPath =new File(saveDirectory + File.separator + getPath()); 
		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		return uploadPath;
	}
	
	private String getPath() {
		return new SimpleDateFormat("yyMMdd").format(new Date());
	}
}
