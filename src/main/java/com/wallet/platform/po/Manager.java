package com.wallet.platform.po;

import java.io.Serializable;
import java.util.Date;

public class Manager implements Serializable {

	private static final long serialVersionUID = -5502903869226523538L;

	private String managerName; // 用户名

	private String managerPwd; // 密码

	private String realname; // 真实名称

	private String menus; // 菜单

	private Boolean inuse; // 是否使用
	
	private Integer errorTimes; // 登录错误次数
	
	private Date lastLoginTime; // 最近一次登录时间
	
	private Date lastLogoutTime; // 最近一次退出时间

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerPwd() {
		return managerPwd;
	}

	public void setManagerPwd(String managerPwd) {
		this.managerPwd = managerPwd;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMenus() {
		return menus;
	}

	public void setMenus(String menus) {
		this.menus = menus;
	}

	public Boolean getInuse() {
		return inuse;
	}

	public void setInuse(Boolean inuse) {
		this.inuse = inuse;
	}

	public Integer getErrorTimes() {
		return errorTimes;
	}

	public void setErrorTimes(Integer errorTimes) {
		this.errorTimes = errorTimes;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLogoutTime() {
		return lastLogoutTime;
	}

	public void setLastLogoutTime(Date lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}

}
