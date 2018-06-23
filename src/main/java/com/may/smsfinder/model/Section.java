package com.may.smsfinder.model;

public class Section {
	private String name;
	private String pattern;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public String toString() {
		return "Section [name=" + name + ", pattern=" + pattern + "]";
	}
}
