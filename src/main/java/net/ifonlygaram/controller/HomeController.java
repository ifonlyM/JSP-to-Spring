package net.ifonlygaram.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.AllArgsConstructor;
import net.ifonlygaram.domain.Criteria;
import net.ifonlygaram.service.BoardService;
import net.ifonlygaram.service.BoardServiceImpl;

@Controller
@AllArgsConstructor
public class HomeController {
	private BoardService service;
	
	@GetMapping("/")
	public String home(Model model) throws UnsupportedEncodingException {
		
		model.addAttribute("list", service.list(new Criteria(1, 8, 1)));
		model.addAttribute("listCri", new Criteria(1, 8, 1));
		
		model.addAttribute("gallList", service.list(new Criteria(1, 6, 2)));
		model.addAttribute("gallListCri", new Criteria(1, 6, 2));
		
		return "common/index";
	}
	
	/*@GetMapping("/common/index")
	public void index(Model model){
		model.addAttribute("list", service.list(new Criteria(1, 8, 1)));
		model.addAttribute("listCri", new Criteria(1, 8, 1));
		
		model.addAttribute("gallList", service.list(new Criteria(1, 6, 2)));
		model.addAttribute("gallListCri", new Criteria(1, 6, 2));
	}*/
	
}
