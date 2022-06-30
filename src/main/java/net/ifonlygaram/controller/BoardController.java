package net.ifonlygaram.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;
import net.ifonlygaram.domain.Attach;
import net.ifonlygaram.domain.Board;
import net.ifonlygaram.domain.Criteria;
import net.ifonlygaram.domain.Member;
import net.ifonlygaram.domain.PageDTO;
import net.ifonlygaram.service.BoardService;
import net.ifonlygaram.service.ReplyService;
import net.ifonlygaram.util.CommonConst;
import net.ifonlygaram.util.MyFileRenamePolicy;

@Controller
@RequestMapping("/board")
@Log4j
public class BoardController {
	@Setter @Autowired
	BoardService service;
	@Setter @Autowired
	ReplyService replyService;
	net.ifonlygaram.util.MutipartParam mp = new net.ifonlygaram.util.MutipartParam();
	
	@GetMapping("list")
	public String list(Model model, Criteria cri){
//		log.info("페이지정보 : " + cri);
		model.addAttribute("list", service.list(cri));
		model.addAttribute("page", new PageDTO(service.getCount(cri), cri));
		if(cri.getCategory() == 2){
			return "redirect:/board/gallList?category=" + cri.getCategory() + "&pageNum=" + cri.getPageNum();
		}
		return null;
	}
	
	@GetMapping("gallList")
	public void gallList(Model model, Criteria cri){
		model.addAttribute("list", service.list(cri));
		model.addAttribute("page", new PageDTO(service.getCount(cri), cri));
	}
	
	@GetMapping("detail")
	public void detail(Model model, Long bno, int category, int pageNum){
//		System.out.println(service);
		model.addAttribute("board", service.read(bno));
		model.addAttribute("category", category);
		// 글이 현재 게시판의 몇페이지에 존재하는지 데이터를 알고있어야한다.
		// 글을 읽고 목록으로 돌아가는 기능작동시 1페이지가 아니라 글이 위치한 페이지로 돌아가야하기 때문.
		model.addAttribute("pageNum", pageNum);
	}
	
	@GetMapping("write")
	public void write(){
		
	}
	
	@PostMapping("write")
	public String write(HttpServletRequest req) throws IOException{
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
			
			if(Long.valueOf(multi.getParameter("category")) == 2){
				// thumbnail
				FileInputStream fis = new FileInputStream(uploadPath + "\\" + uuid);
				FileOutputStream fos = new FileOutputStream(uploadPath + "\\s_" + uuid);
				Thumbnailator.createThumbnail(fis, fos, 250, 250);
			}
			
			attachs.add(attach);
		}
		
		String title = multi.getParameter("title");
		String content = multi.getParameter("content");
		Long category = Long.valueOf(multi.getParameter("category"));
		Member member = ((Member)req.getSession().getAttribute("member"));
		String id = "";
		
		if(member == null)
			return "redirect:/login?timeover=1";
		else {
			id = member.getId(); // 멤버가져올때 널발생 가능
		
			Board board = new Board(null, title, content, null, id, category);
			board.setAttachs(attachs);
			service.write(board);
			
			return "redirect:/board/list?category=" + category;
		}
	}
	
	@GetMapping("modify")
	public void modify(HttpServletRequest req, Long bno, int category, int pageNum) throws ServletException, IOException{
		Board board = service.read(bno);
		req.setAttribute("board", board);
		
		  java.util.List<String> filePathList = new ArrayList<String>();
		  java.util.List<String> fileNameList = new ArrayList<String>();
		  java.util.List<Attach> fileList = board.getAttachs();
		  
		  int idx = 1;
		  String filePath = ""; 
		  
		  for(Attach a : board.getAttachs()) { 
			  if(a == null) continue;
		  	  // filePath = mp.getSaveDirectory();
			  
			  filePath = CommonConst.UPLOAD_PATH.replace("\\", "\\\\"); 
			  filePath += "\\\\" + a.getPath() + "\\\\"+ a.getUuid(); 
			  System.out.println(filePath);
			  // req.setAttribute("file" + String.valueOf(idx) + "-path", filePath); 
			  filePathList.add(filePath);
			  fileNameList.add(a.getOrigin()); 
		  } 
		req.setAttribute("filePathList",filePathList); 
		req.setAttribute("fileNameList", fileNameList);
		req.setAttribute("fileList", fileList);
		req.setAttribute("category", category);
		req.setAttribute("pageNum", pageNum);
	}
	
	@PostMapping("modify")
	public String modified(HttpServletRequest req, @RequestParam("category") int category, @RequestParam("pageNum") int pageNum) throws IOException{
		MultipartRequest multi = new MultipartRequest(
				req, 
				mp.uploadPath().getAbsolutePath(),
				mp.getMaxPostSize(), 
				mp.getEncoding(), 
				mp.getPolicy()
		);
		
		Enumeration<String> files = multi.getFileNames();
		List<Attach> attachs = new ArrayList<>();
		while(files.hasMoreElements()) {
			String file = files.nextElement();
			String uuid = multi.getFilesystemName(file); // renamepolicy로 변경된 이름
			if(uuid == null)continue;
			String origin = multi.getOriginalFileName(file); // 원래 파일 이름
			Long fileSize = multi.getFile(file).length(); // 파일 사이즈
			
			
			Attach attach = new Attach(uuid, origin, null, getPath(), fileSize);
			
			attachs.add(attach);
		}
		
		Long bno = Long.valueOf(multi.getParameter("bno"));
		String title = multi.getParameter("title");
		String content = multi.getParameter("content");
		
		Board board = new Board(bno, title, content);
		board.setAttachs(attachs);
		
		service.modify(board);
		return "redirect:/board/detail?bno=" + bno +"&category=" + category + "&pageNum=" + pageNum;
	}
	
	@GetMapping("remove")
	public void remove(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long bno = Long.valueOf(req.getParameter("bno"));
		// 댓글삭제를 jsp에서 했는데 이곳에서 처리할것
		replyService.removeBy(bno);
		
		// 글 삭제
		service.remove(bno);
	}
	
	@PostMapping("deleteAttachs")
	public @ResponseBody void deleteAttachs(@RequestBody List<String> uuids){
		log.info(uuids);
		uuids.forEach(service::removeAttachBy);
	}
	
	
	private String getPath() {
		return new SimpleDateFormat("yyMMdd").format(new Date());
	}
}
