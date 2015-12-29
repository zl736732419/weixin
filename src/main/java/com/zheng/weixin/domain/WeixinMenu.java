package com.zheng.weixin.domain;

import java.util.List;

public class WeixinMenu {
	private Integer id;
	private String name;
	private String type;
	private String key;
	private String url;
	private String media_id;
	private Integer parentId;
	private List<WeixinMenu> sub_button;

	public WeixinMenu() {

	}

	public WeixinMenu(Integer id, String name, String type, String key,
			String url, String media_id) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.key = key;
		this.url = url;
		this.media_id = media_id;
	}

	public Integer getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMedia_id() {
		return media_id;
	}

	public String getName() {
		return name;
	}

	public Integer getParentId() {
		return parentId;
	}

	public List<WeixinMenu> getSub_button() {
		return sub_button;
	}

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void subMenu(String key) {
		this.key = key;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public void setSub_button(List<WeixinMenu> sub_button) {
		this.sub_button = sub_button;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
