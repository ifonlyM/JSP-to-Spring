package net.ifonlygaram.task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.AllArgsConstructor;
import net.ifonlygaram.domain.Attach;
import net.ifonlygaram.service.BoardService;
import net.ifonlygaram.service.BoardServiceImpl;
import net.ifonlygaram.util.CommonConst;

@AllArgsConstructor
public class OrganizeFileJob implements Job{
	private BoardService service;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String dateStr = new SimpleDateFormat("yyMMdd").format(cal.getTime());
		realJob(dateStr);
	}
	
	public void realJob(String dateStr) {
		File file = new File(CommonConst.UPLOAD_PATH + File.separator + dateStr);
		File[] files = file.listFiles();
		
		List<Attach> dbList = service.readAttachByPath(dateStr);
			
		List<File> fileList = new ArrayList<>(Arrays.asList(files));
		List<File> existFile = new ArrayList<File>();
		for(File f : fileList) {
			for(Attach a : dbList) {
				if(f.getAbsolutePath().contains(a.getUuid())) {
					existFile.add(f);
				}
			}
		}
		
		fileList.removeAll(existFile);
		
		System.out.println("/r/n"+ dateStr + "폴더의 삭제된 파일들");
		System.out.println("======================================================fileList");
		int cnt = 0;
		for(File f : fileList) {
			System.out.println(f);
			f.delete(); cnt++;
		}
		System.out.println(cnt + "개의 파일 삭제됨");
	}
}
