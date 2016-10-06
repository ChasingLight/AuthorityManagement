package com.jin.test;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		//利用java反射机制，处理多个请求，根据请求中设置的method参数
		
		//获取请求中的method属性的值
		String methodName = request.getParameter("method");
		
		try {
			Method method = getClass().getMethod(methodName, 
					HttpServletRequest.class, HttpServletResponse.class);
			method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private UserDao userDao = new UserDao();
	
	//登录控制器处理
	public void login(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		//1. 获取userName
		String userName = request.getParameter("userName");
		
		//2. 调用UserDao 获取用户信息，把用户信息放入到HttpSession中
		User user = userDao.get(userName);
		request.getSession().setAttribute("user", user);
		
		//3. 重定向到articles.jsp  
		//***登录后，虽然重定向了，但是当前用户的user信息存放到了会话session中***
		response.sendRedirect(request.getContextPath() + "/articles.jsp");
		
		
	}
	
	//登出控制器处理
	public void logout(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		//1. 获取HttpSession
		//2. 使HttpSession失效
		request.getSession().invalidate();
		
		//3. 重定向到/login.jsp
		response.sendRedirect(request.getContextPath() + "/login.jsp");
	}
}
