package com.wallet.platform.bean;

public class ParamBean {

	public ParamBean() {

	}

	public ParamBean(String name, Object value) {
		this(name, value, false);
	}

	public ParamBean(String name, Object value, boolean required) {
		this.name = name;
		this.value = value;
		this.required = required;
	}

	public ParamBean required() {
		this.required = true;
		return this;
	}

	private String name;

	private Object value;

	private boolean required = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
