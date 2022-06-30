package net.ifonlygaram.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import net.ifonlygaram.domain.Member;
import net.ifonlygaram.service.MemberService;
import net.ifonlygaram.util.PwdJbcrypt;

@Controller
@AllArgsConstructor @Log4j
public class MemberController {
	MemberService service;
	
	@GetMapping("/login")
	public String loginForm(){
		return "member/login";
	}
	
	@PostMapping("/login")
	public String login(Model model, String id, String pwd, HttpServletRequest req, HttpServletResponse resp){
		String msg = "";
		String redirectUrl = "login";
		
		boolean success = service.login(id, pwd);
		
		if(success) {
			HttpSession session = req.getSession();
			session.setMaxInactiveInterval(1800); //세션 유효시간 1시간으로 설정
			session.setAttribute("member", service.findBy("ID", id));
			msg = "로그인 성공";
			
			// 아이디 저장 체크박스 기능 로직
			Cookie cookie = new Cookie("savedId", id);
			// 삼항연산자를 이용해서 쿠키가 없을땐 유효기간을 0으로 쿠키가 존재할땐 1년으로 시간을 셋
			cookie.setMaxAge(req.getParameter("savedId") == null ? 0 : 60 * 60 * 24 * 365); 
			resp.addCookie(cookie);
			
			redirectUrl = "";
		}
		else {
			msg = "로그인 실패";
		}
		model.addAttribute("msg", msg);
		return "redirect:/" + redirectUrl;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	// 내 정보
	@GetMapping("/info")
	public String info(Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		model.addAttribute("member", member);
		
		// return 값은 WEB-INF/views 뒤에 붙는 경로
		return "member/info";
	}
	
	@GetMapping("/findinfo")
	public String findinfo(){
		return "member/findinfo";
	}
	
	@PostMapping("/findinfo")
	public String moveLogin(){
		return "redirect:/login";
	}
	
	@GetMapping("/getFindId")
	@ResponseBody
	public String getFindId(String email){
		log.info("이메일:" +  email);
		return service.findBy("EMAIL", email).getId();
	}
	
	@GetMapping("/getNewPwd")
	@ResponseBody
	public String getNewPwd(String email){
		Member member = service.findBy("EMAIL", email);
		UUID newPWd = UUID.randomUUID();
		member.setPwd(newPWd.toString());
		service.modify(member);
		return newPWd.toString();
	}
	
	// 내 정보 수정 완료
	@ResponseBody @PostMapping(value="/info", produces="text/plain; charset=utf-8")
	public String info(@RequestBody Member member, HttpSession session) {
		log.info(member);
		Member loginMember = (Member)session.getAttribute("member");
		String id = loginMember.getId();
		String pwd = loginMember.getPwd();
		String email = loginMember.getEmail();
		String name = loginMember.getName();
		
		if(member.getId().isEmpty())		member.setId(id);
		if(member.getPwd().isEmpty())		member.setPwd(pwd);
		if(member.getEmail().isEmpty())		member.setEmail(email);
		if(member.getName().isEmpty())		member.setName(name);
		
		service.modify(member);
		
		// 수정한 정보의 유저로 member어트리뷰트를 다시 설정
		session.setAttribute("member", member); 
		
		return "수정이 완료되었습니다";
	}
	
	// 비밀번호 체크
	@PostMapping("pwdCheck")
	public @ResponseBody Integer pwdCheck(@RequestBody String input, HttpSession session) {
		String userPwd =((Member)session.getAttribute("member")).getPwd();
		return input.equals(userPwd) ? 1 : 0;
	}
	
	// 회원 탈퇴
	// ajax로 얻어온 id로 탈퇴만 하고 반환하는건 없는데, 왜 404 에러가 나지?
	// -> @ResponseBody 추가로 해결 야매로 해결한거같다.
	@GetMapping("signout")
	@ResponseBody
	public String signOut(String id, HttpSession session) {
//		service.signOut(id);
		session.invalidate();
		return "success";
	}
	
	@GetMapping("/contract")
	public String contract(){
		return "member/contract";
	}
	
	@GetMapping("join")
	public String joinForm() {
		return "member/join";
	}
	
	@PostMapping("join")
	public String join(String id, String pwd, String name, String email) {
		pwd = new PwdJbcrypt().pwdJbcrypt(pwd);
		Member member = new Member(id, pwd, email, name);
		service.join(member);
		return "redirect:/login";
	}
	
	//ajax get방식으로 작동
	// 파라미터 : url파라미터 id와 같은 이름 사용
	// 결과 여부 : @ResponseBody 명시하여 결과값을 ajax success 아규먼츠로 보냄
	@GetMapping("idValid")
	public @ResponseBody Integer idValid(String id) {
		return service.findBy("id", id) == null ? 1 : 0;
	}
	
	@GetMapping("emailValid")
	public @ResponseBody Integer emailValid(String email) {
		return service.findBy("EMAIL", email) == null ? 1 : 0;
	}
	
}
