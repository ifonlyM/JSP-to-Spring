package net.ifonlygaram.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;

import net.ifonlygaram.service.BoardService;

@WebServlet("/FileDownload")
public class FileDownload extends HttpServlet {
	//빈에 직접 접근하여 빈객체가져오기
	WebApplicationContext context = org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext();
	BoardService service = (BoardService) context.getBean("BoardService");
	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
//		Long bno = (Long) req.getAttribute("bno");
//		BoardService bs = new BoardServiceImpl();
//		Board board=  bs.read(bno);
//		String uuid = "";
//		
//		for(Attach a : board.getAttachs()) {
//			if(req.getParameter("uuid").equals(a.getUuid())) {
//					uuid = a.getUuid();
//			}
//		}
//		String fileName = req.getParameter(uuid);
		
		// 파일 인풋을 위한 단계
		String fileName = req.getParameter("filename");
		String saveDirectory = CommonConst.UPLOAD_PATH;
		String filePath = saveDirectory + File.separator + fileName;
		
		System.out.println(filePath);
		
		FileInputStream fis = new FileInputStream(filePath);
		String mimeType = getServletContext().getMimeType(filePath);
		
		if(mimeType == null) {
			mimeType = "application/octet-stream";
		}
		
		System.out.println(mimeType);
		
		// 사용자의 브라우저
		String userAgent = req.getHeader("User-Agent");
		System.out.println(userAgent);
		
		
		//파일 아웃풋을위한 단계
//		fileName = ""?		
//		BoardService service = new BoardServiceImpl();
		System.out.println(service);
		fileName = service.findOriginBy(fileName.substring(fileName.indexOf("/")+1));
//		fileName = service.findOriginBy(fileName);
		
		// 인터넷 익스플로러일 경우
		if(userAgent.toLowerCase().contains("trident")) {
			fileName = URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20");
		}
		// 다른 브라우저일경우 에따른 파일이름 변경
		else {
			fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
		}
		
		System.out.println(fileName);
		
		// 응답헤더 설정
		resp.setContentType(mimeType);
		resp.setHeader("Content-DisPosition", "attachment; filename=" + fileName);
		
		// 출력 스트림 지정
		ServletOutputStream sos = resp.getOutputStream();
		int b;
		
		while((b = fis.read()) != -1) {
			sos.write(b);
		}
		
		fis.close();
		sos.close();
	}
	
}
