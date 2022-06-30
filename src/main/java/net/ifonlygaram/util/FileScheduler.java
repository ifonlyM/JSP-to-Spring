package net.ifonlygaram.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import net.ifonlygaram.domain.Attach;
import net.ifonlygaram.service.BoardService;
import net.ifonlygaram.service.BoardServiceImpl;

@AllArgsConstructor
public class FileScheduler {
	private static BoardService service;
	
	public static void main(String[] args) {
		File file = new File("d:\\upload\\210913");
		File[] files = file.listFiles();
		for(File f : files) {
			System.out.println(f);
		}
		
		System.out.println("====================================================== dbList");
		
		List<Attach> dbList = service.readAttachByPath("210913");
		
		for(Attach a : dbList) {
			System.out.println(a.getUuid());
		}
		System.out.println("======================================================");
		
		List<File> fileList = new ArrayList<>(Arrays.asList(files));
		List<File> existFile = new ArrayList<File>();
		for(File f : fileList) {
			for(Attach a : dbList) {
				if(f.getAbsolutePath().contains(a.getUuid())) {
					existFile.add(f);
				}
			}
//			System.out.println(f.getAbsolutePath() + " "
//					+ (f.getAbsolutePath().contains("06ad4399-e4d1-484d-ad24-d06bc69e8880.jpg") ? "o" : "x"));
		}
		
		System.out.println("======================================================existFile");
		
		for(File f : existFile) {
			System.out.println(f);
		}
		
		fileList.removeAll(existFile);
		
		System.out.println("======================================================fileList");
		
		for(File f : fileList) {
			System.out.println(f);
			f.delete();
		}
	}
}
