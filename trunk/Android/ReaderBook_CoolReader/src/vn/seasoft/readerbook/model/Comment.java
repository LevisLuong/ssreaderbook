package vn.seasoft.readerbook.model;

import java.util.Date;

/**
 * User: XuanTrung
 * Date: 10/14/2014
 * Time: 4:40 PM
 */
public class Comment {
    int idcomment;
    String content;
    String iduserfacebook;
    String username;
    int idbook;
    Date datecomment;

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

    public Date getDatecomment() {
        return datecomment;
    }

    public void setDatecomment(Date datecomment) {
        this.datecomment = datecomment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
