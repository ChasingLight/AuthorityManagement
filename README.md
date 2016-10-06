# AuthorityManagement
1.项目结构：

2.权限管理设计思路：
2.1用户-权限的 查看 和 修改

   2.1.1对应项目文件
     实体：User.java、Authority.java
     数据访问对象(DAO):UserDao.java

     权限查看、修改页面：authority-manager.jsp
     权限控制器: AuthorityServlet.java 

   2.1.2 请求逻辑分析
     1.查看userName对应的权限
      首先由authority-manager.jsp页面发出request请求（请求参数userName=AAA），请求到达对应的权限控制器AuthorityServlet.java，控制器调用UserDao.java中对应的方法，获取userName为AAA的用户所具有的权限。然后将查询到的数据通过request.setAttribute,放到当前请求request中。
      利用请求转发forward到authority-manager.jsp页面。最后页面使用JSTL处理request中的AAA用户的权限数据，加以显示。
2.更新userName对应的权限
 同上
     
2.2 使用过滤器拦截，使用户-权限发挥作用

  2.2.1对应项目文件
     数据访问对象(DAO):UserDao.java

     登录页面：login.jsp
     主页：articles.jsp  (登录成功后，进入的主页页面)
     示例页面: article-1.jsp至article-5.jsp， 
     跳转页面：403.jsp  (用户没有对应权限跳转至此页面)
     
     登入、登出控制器：LoginServlet.java
     权限过滤器：AuthorityFilter.java
     过滤器不进行拦截的urls："/403.jsp",
				"/articles.jsp","/authority-manager.jsp",
				"/login.jsp","/logout.jsp"

  2.2.2 请求逻辑分析
     1.AAA用户登录
      首先由login.jsp发出request请求（请求参数userName=AAA）。请求先被权限过滤器：AuthorityFilter.java处理：由于login.jsp请求路径是不拦截的，所以直接使用filterChain.doFilter(request, response);放行此请求。然后请求正常到达 登入、登出控制器LoginServlet.java，然后控制器调用UserDao.java中对应的方法:获取userName为AAA的用户-权限信息。同时将此User user信息放到HttpSession中。

     然后使用request重定向到主页页面articles.jsp。

     主页页面有很多的链接：article-1.jsp至article-5.jsp，当用户点击对应的链接，如果有相应的权限访问对应的页面，如果没有权限跳转至403.jsp页面。
     判断过程：比如用户AAA进入到主页articles.jsp后，点击了article-1.jsp超链接。伴随着点击动作的发生，一个访问article-1.jsp页面的request请求发出，请求首先被权限过滤器拦截处理：article-1.jsp请求路径需要拦截，首先获得HttpSession中的用户-权限信息，判断“article-1.jsp”这个servletPath，AAA用户是否有权限访问：如果有权限使用filterChain.doFilter(request, response)正常跳转，没有权限重定向到403.jsp。

3.此项目巧妙之处：
   3.1在一个Servlet使用 java反射机制 使其具有处理多个请求的能力：
       见于：权限控制器: AuthorityServlet.java
             登入、登出控制器：LoginServlet.java
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		String methodName = request.getParameter("method");
		
		try {
			Method method = getClass().getMethod(methodName, 
					HttpServletRequest.class,    HttpServletResponse.class);
			method.invoke(this, request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

   3.2 判断两个对象是否相等，重写对象的equals和hashCode方法：

    //Authority对象之间的比较，重写对象的equals()方法
	//此处的equals方法只用比较对象的String url属性 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authority other = (Authority) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	

   3.2 退出当前登录logout时，触发两个操作：1.使此次请求的HttpSession失效；2.重定向到登录页面

   //登出控制器处理
	public void logout(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		//1. 获取HttpSession
		//2. 使HttpSession失效
		request.getSession().invalidate();
		
		//3. 重定向到/login.jsp
		response.sendRedirect(request.getContextPath() + "/login.jsp");
	}
