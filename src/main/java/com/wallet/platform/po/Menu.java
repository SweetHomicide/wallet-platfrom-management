package com.wallet.platform.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Menu implements Serializable {

	private static final long serialVersionUID = -9207295871513972390L;

	private String id; // 菜单ID

	private String parentId; // 父节点ID

	private String menuName; // 菜单名称

	private String menuHref; // 菜单链接

	private Integer serno; // 排序号

	private List<Menu> subMenus = new ArrayList<Menu>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuHref() {
		return menuHref;
	}

	public void setMenuHref(String menuHref) {
		this.menuHref = menuHref;
	}

	public Integer getSerno() {
		return serno;
	}

	public void setSerno(Integer serno) {
		this.serno = serno;
	}

	public List<Menu> getSubMenus() {
		return subMenus;
	}

	public void addSubMenu(Menu subMenu) {
		this.subMenus.add(subMenu);
	}

}
