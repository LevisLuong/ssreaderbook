package model;

import java.sql.Timestamp;

public class CommentJson {
	int idcomment;
	String content;
	String iduserfacebook;
	String username;
	int idbook;
	Timestamp datecomment;

	public int getIdcomment() {
		return idcomment;
	}

	public void setIdcomment(int idcomment) {
		this.idcomment = idcomment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIduserfacebook() {
		return iduserfacebook;
	}

	public void setIduserfacebook(String iduserfacebook) {
		this.iduserfacebook = iduserfacebook;
	}

	public int getIdbook() {
		return idbook;
	}

	public void setIdbook(int idbook) {
		this.idbook = idbook;
	}

	public Timestamp getDatecomment() {
		return datecomment;
	}

	public void setDatecomment(Timestamp datecomment) {
		this.datecomment = datecomment;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
