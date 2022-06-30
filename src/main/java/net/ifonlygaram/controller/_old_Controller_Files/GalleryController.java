package net.ifonlygaram.controller._old_Controller_Files;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import lombok.AllArgsConstructor;
import net.ifonlygaram.domain.Attach;
import net.ifonlygaram.domain.Board;
import net.ifonlygaram.domain.Criteria;
import net.ifonlygaram.domain.Member;
import net.ifonlygaram.domain.PageDTO;
import net.ifonlygaram.service.BoardService;
import net.ifonlygaram.service.BoardServiceImpl;
import net.ifonlygaram.util.CommonConst;
import net.ifonlygaram.util.MyFileRenamePolicy;

@Controller
@RequestMapping("/Gallery")
@AllArgsConstructor
public class GalleryController {
	private BoardService service;
	
	@GetMapping("list")
	public void list(Model model, Criteria cri){
		model.addAttribute("list", service.list(cri));
		model.addAttribute("page", new PageDTO(service.getCount(cri), cri));
	}
	
	@GetMapping("detail")
	public void detail(Model model, Long bno){
		model.addAttribute("board", service.read(bno));
	}
	
	@GetMapping("write")
	public void writeForm(){
	}
	
	@PostMapping("write")
	public String write(HttpServletRequest req, HttpSession session) throws IOException{
		// 글쓰기 후 프로세스
		String url = req.getContextPath();
		String saveDirectory = CommonConst.UPLOAD_PATH;
		String path = getPath();
		File uploadPath = new File(saveDirectory + File.separator + path);
		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		int maxPostSize = 10 * 1024 * 1024;
		String encoding = "utf-8";
		FileRenamePolicy policy = new MyFileRenamePolicy();
		
		MultipartRequest multi = new MultipartRequest(req, uploadPath.getAbsolutePath(), maxPostSize, encoding, policy);
		
		Enumeration<String> files = multi.getFileNames();
		List<Attach> attachs = new ArrayList<>();
		while(files.hasMoreElements()) {
			String file = files.nextElement();
			String uuid = multi.getFilesystemName(file); // renamepolicy로 변경된 이름
			if(uuid == null) continue;
			String origin = multi.getOriginalFileName(file); // 원래 파일 이름
			Long fileSize = multi.getFile(file).length(); // 파일 사이즈
			
			Attach attach = new Attach(uuid, origin, null, path, fileSize);
			
			attachs.add(attach);
		}
		
		String title = multi.getParameter("title");
		String content = multi.getParameter("content");
		Long category = Long.valueOf(multi.getParameter("category"));
		Member member = ((Member)req.getSession().getAttribute("member"));
		String id = "";
		
		if(member == null) {
//			session.getWriter().print("<script>alert('로그인 세션이 만료되었습니다.')</script>");
//			if(category == 0L) url += "/notice/list";		
//			else if(category == 1L) url += "/board/list";
//			else if(category == 2L) url += "/gallery/list";
//			else url += "index.html";
//			resp.sendRedirect(url);
			return "redirect:/member/login";
		}
		else {
			id = member.getId(); // 멤버가져올때 널발생 가능
		
			Board board = new Board(null, title, content, null, id, category);
			board.setAttachs(attachs);
			service.write(board);
			
			if(category == 0L) url += "/notice/list";		
			else if(category == 1L) url += "/board/list";
			else if(category == 2L) url += "/gallery/list";
			else url += "index.html";
			return "redirect:/board/list";
		}
	}
	
	// 글쓰기 폼
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String menu = "";
			Long getCategory = Long.valueOf(req.getParameter("category"));
			
			if(getCategory == 0L) menu = "notice";
			else if(getCategory == 1L) menu = "board";
			else if(getCategory == 2L) menu = "gallery";
			else menu = "board";
			
			req.getRequestDispatcher("/WEB-INF/jsp/" + menu +"/write.jsp").forward(req, resp);
		}

		
		
		private String getPath() {
			return new SimpleDateFormat("yyMMdd").format(new Date());
		}
}
