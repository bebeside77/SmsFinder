package com.may.smsfinder.model;

public class Sms {
	private String sectionName;
	private long id;
	private long threadId;
	private String address;
	private String person;
	private long date;
	private String body;

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getThreadId() {
		return threadId;
	}
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "Sms [id=" + id + ", threadId=" + threadId + ", address="
				+ address + ", date=" + date + ", body=" + body + "]";
	}
	
}
