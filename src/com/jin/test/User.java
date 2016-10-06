package com.jin.test;

import java.util.List;

public class User {
	//�û���
	private String userName;
	
	//�û���Ӧ��Ȩ���б�����ʹ���������Ϲ�ϵ��
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
