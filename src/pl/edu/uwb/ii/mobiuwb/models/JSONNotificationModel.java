package pl.edu.uwb.ii.mobiuwb.models;

import java.util.Date;

public class JSONNotificationModel {
	private Date date;

	public Date getDate() {
		return date;
	}

	private void setDate(Date date) {
		this.date = date;
	}

	private String content;

	public String getContent() {
		return content;
	}

	private void setContent(String content) {
		this.content = content;
	}

	private String title;

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public JSONNotificationModel(String title, String content, Date date) {
		setTitle(title);
		setDate(date);
		setContent(content);
	}

	public JSONNotificationModel(String content, Date date) {
		setDate(date);
		setContent(content);
		setTitle(null);
	}
}
