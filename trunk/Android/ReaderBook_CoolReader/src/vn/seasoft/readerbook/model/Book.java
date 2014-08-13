package vn.seasoft.readerbook.model;

import com.j256.ormlite.field.DatabaseField;
import vn.seasoft.readerbook.Util.GlobalData;

import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/9/2014
 * Time: 9:32 AM
 */
public class Book {
    public Book() {
    }

    @DatabaseField(id = true)
    Integer idbook;
    @DatabaseField
    String title;
    @DatabaseField
    String author;
    @DatabaseField
    String summary;
    @DatabaseField
    Integer idcategory;
    @DatabaseField
    Boolean isReaded;
    @DatabaseField
    String imagecover;
    @DatabaseField
    Integer countview;
    @DatabaseField
    Boolean isnew;

    public void setIdbook(Integer idbook) {
        this.idbook = idbook;
    }

    public Integer getIdbook() {
        return idbook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(Integer idcategory) {
        this.idcategory = idcategory;
    }

    public Boolean getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(Boolean isReaded) {
        this.isReaded = isReaded;
    }

    public String getImagecover() {
        return imagecover;
    }

    public void setImagecover(String imagecover) {
        this.imagecover = imagecover;
    }

    public Integer getCountview() {
        return countview;
    }

    public void setCountview(Integer countview) {
        this.countview = countview;
    }

    public Boolean getIsnew() {
        return isnew;
    }

    public void setIsnew(Boolean isnew) {
        this.isnew = isnew;
    }

    /*Method for book*/
    public int addNewData() {
        Book b = getByID(this.idbook);
        if (b == null) {
            return GlobalData.repo.book.create(this);
        } else {
            return GlobalData.repo.book.update(this);
        }
    }

    public int updateData() {
        return GlobalData.repo.book.update(this);
    }

    public int deleteData() {
        return GlobalData.repo.book.delete(this);
    }

    public Book getByID(int idbook) {
        return GlobalData.repo.book.getById(idbook);
    }

    public List<Book> getAllData() {
        return GlobalData.repo.book.getAll();
    }

    public List<Book> getSearchBook(String key) {
        return GlobalData.repo.book.getSearchBook(key);
    }
}
