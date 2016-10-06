package com.jin.test;

import java.util.List;

public class User {
	//用户名
	private String userName;
	
	//用户对应的权限列表：这里使用了类的组合关系。
	private List<Authority> authorities;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public User(String userName, List<Authority> authorities) {
		super();
		this.userName = userName;
		this.authorities = authorities;
	}
	
	public User(){
		
	}
	
	
}
