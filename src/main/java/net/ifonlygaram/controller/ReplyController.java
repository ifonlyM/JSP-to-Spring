package net.ifonlygaram.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.ifonlygaram.domain.Reply;
import net.ifonlygaram.service.ReplyService;

@RestController
@RequestMapping("reply")
@Log4j
public class ReplyController {
	@Setter @Autowired
	private ReplyService service;
	private Gson gson = new Gson();
	
	@GetMapping("/get")
	public Reply get(Long rno) {
		return service.get(rno);
	}
	
	@PostMapping("/write")
	public void write(@RequestBody Reply reply) {
		System.out.println(reply);
		service.write(reply);
	}
	
	@PostMapping("/modify")
	public void modify(@RequestBody Reply reply) {
		System.out.println(reply);
		log.info(reply);
		service.modiify(reply);
	}
	
	@DeleteMapping
	public void delete(Long rno) {
		service.remove(rno);
	}
	
	@GetMapping("list")
	public List<Reply> getList(Long bno) {
		return service.list(bno);
	}
	
//	// 댓글 단일 조회
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Long rno = Long.parseLong(req.getParameter("rno"));
//		Reply reply = service.get(rno);
//		resp.setContentType("allication/json");
//		resp.setCharacterEncoding("utf-8");
//		resp.getWriter().print(gson.toJson(reply));
//	}

//	// 댓글 작성
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		String jsonData = req.getParameter("jsonData");
//		Reply reply = gson.fromJson(jsonData, Reply.class);
//		service.write(reply);
//	}

	// 댓글 수정
//	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		BufferedReader br = req.getReader();
//		String jsonData = "";
//		String parseStr = null;
//		while((parseStr = br.readLine()) != null) {
//			jsonData += parseStr;
//		}
//		System.out.println(jsonData);
//
//		
//		Reply reply = gson.fromJson(req.getReader(), Reply.class);
//		System.out.println(reply);
//		Reply reply = gson.fromJson(jsonData, Reply.class);
//		service.modiify(reply);
//	}
	
	// 댓글 삭제
//	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Long rno = Long.parseLong(req.getParameter("rno"));
//		service.remove(rno);
//	}
}
