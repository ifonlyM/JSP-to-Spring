package net.ifonlygaram.util;

import java.io.File;
import java.util.UUID;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class MyFileRenamePolicy implements FileRenamePolicy{

	@Override
	public File rename(File arg0) {
		int pos = arg0.getName().lastIndexOf(".");
		String ext = "";
		if(pos != -1) {
			ext = arg0.getName().substring(pos);
		}
		return new File(arg0.getAbsolutePath().replace(arg0.getName(), "") + UUID.randomUUID() + ext);
	}
	
	public static void main(String[] args) {
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);
		uuid = UUID.randomUUID();
		System.out.println(uuid);
		uuid = UUID.randomUUID();
		System.out.println(uuid);
		uuid = UUID.randomUUID();
		System.out.println(uuid);
		uuid = UUID.randomUUID();
		System.out.println(uuid);
		uuid = UUID.randomUUID();
		System.out.println(uuid);
	}
}
