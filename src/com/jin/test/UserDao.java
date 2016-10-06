package com.jin.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {
	
	//存放用户信息
	private static Map<String,User> users;
	
	//模拟数据库中的所有的权限都存放在authorities List中
	private static List<Authority> authorities = null;
	
	static{
		
		authorities = new ArrayList<>();
		authorities.add(new Authority("Article-1","/article-1.jsp"));
		authorities.add(new Authority("Article-2","/article-2.jsp"));
		authorities.add(new Authority("Article-3","/article-3.jsp"));
		authorities.add(new Authority("Article-4","/article-4.jsp"));
		
		users = new HashMap<String, User>();
		
		User user1 = new User("AAA", authorities.subList(0, 2));
		users.put("AAA", user1);
		
		user1 = new User("BBB", authorities.subList(2, 4));
		users.put("BBB", user1);
	}
	
	//method1:查看用户权限
	User get(String userName){
		return users.get(userName);
	}
	
	
	//method2:根据用户名，更新对应的用户权限
	void update(String userName,List<Authority> authorities){
		users.get(userName).setAuthorities(authorities);
	}
	
	//method3:获取“数据库”所有的用户权限List
	public List<Authority> getAuthorities(){
		return authorities;
	}
	
	
	//method4：根据前台JSP页面传递过来的String权限数组，转换为List Atuthority
	public List<Authority> getAuthorities(String[] urls){
		List<Authority> authorities2 = new ArrayList<>();
		
		for(Authority authority : authorities){
			if(urls != null){
				for(String url : urls){
					if(url.equals(authority.getUrl())){
						authorities2.add(authority);
					}
				}
			}
		}
		
		return authorities2;
		
	}
	
	
	
}
