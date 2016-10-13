package com.hsd.zkoss.todo.model;

public class TodoItem {

	private String id;
	private Boolean isActive;
	private String title;
	private Integer order;

	public TodoItem(String id, Boolean isActive, String title, Integer order) {
		super();
		this.id = id;
		this.isActive = isActive;
		this.title = title;
		this.order = order;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}
