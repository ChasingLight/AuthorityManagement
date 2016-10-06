package com.jin.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AuthorityServlet")
public class AuthorityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		String methodName = request.getParameter("method");
		
		try {
			Method method = getClass().getMethod(methodName, 
					HttpServletRequest.class, HttpServletResponse.class);
			method.invoke(this, request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private UserDao userDao = new UserDao();
	
	//控制器：根据请求中的“用户名”参数，调用UserDao的get方法，获得用户的权限。
	public void getAuthorities(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException{
		String userName = request.getParameter("userName");
		User user = userDao.get(userName);
		
		request.setAttribute("user", user);
		request.setAttribute("authorities", userDao.getAuthorities());
		
		
		request.getRequestDispatcher("/authority-manager.jsp").forward(request, response);
	}
	
	
	//控制器：根据请求中的“用户名”和“更新后的新权限”参数，调用UserDao的update方法，更新用户的权限。
	public void updateAuthorities(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException{
		String userName = request.getParameter("userName");
		String[] authorities = request.getParameterValues("authority");
		
		List<Authority> authorityList = userDao.getAuthorities(authorities);
		userDao.update(userName, authorityList);
		
		//请求重定向(会丢失请求request中的所有信息)
		response.sendRedirect(request.getContextPath() + "/authority-manager.jsp");
	}

}
