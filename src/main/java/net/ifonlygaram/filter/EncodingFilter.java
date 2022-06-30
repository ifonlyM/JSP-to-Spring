package net.ifonlygaram.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter("/*")
public class EncodingFilter implements Filter{
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		//전처리
		request.setCharacterEncoding("utf-8");
		chain.doFilter(request, response); // 실제 서블릿이 해야할 행동
		
		//후처리
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
