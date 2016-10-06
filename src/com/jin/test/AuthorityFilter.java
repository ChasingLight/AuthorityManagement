package com.jin.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter("*.jsp")
public class AuthorityFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//获取servletPath,类似于/article-1.jsp
		String servletPath = request.getServletPath();
		
		//不需要拦截的 url 列表
		List<String> uncheckedUrls = Arrays.asList("/403.jsp",
				"/articles.jsp","/authority-manager.jsp",
				"/login.jsp","/logout.jsp");
		
		if(uncheckedUrls.contains(servletPath)){
			filterChain.doFilter(request, response);
			return;
		}
		
		//在用户已经登录的情况下，获取用户信息: session.getAttriute("user");
		User user = (User)request.getSession().getAttribute("user");
		if(user == null){
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		
		//再获取用户所具有的权限信息: List<Authority>
		List<Authority> authorities = user.getAuthorities();
		
		//检验用户是否具有请求 servletPath 的权限
		Authority authority = new Authority(null,servletPath);
		//若有权限： 响应
		if(authorities.contains(authority)){
			filterChain.doFilter(request, response);
			return;
		}
		
		//若无权限： 重定向到403.jsp
		response.sendRedirect(request.getContextPath() + "/403.jsp");
		return;
		
	}


}
