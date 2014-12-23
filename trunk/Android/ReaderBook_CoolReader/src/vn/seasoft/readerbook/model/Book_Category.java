package vn.seasoft.readerbook.model;

import com.j256.ormlite.field.DatabaseField;
import vn.seasoft.readerbook.Util.GlobalData;

import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/9/2014
 * Time: 9:32 AM
 */
public class Book_Category {
    @DatabaseField(id = true)
    Integer idcategory;
    @DatabaseField
    String category;

    public Book_Category() {
    }
    public Book_Category(Integer idcategory, String category) {
        this.idcategory = idcategory;
        this.category = category;
    }

    public Integer getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(Integer idcategory) {
        this.idcategory = idcategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /*
        Method for book
    */
    public int addNewData() {
        Book_Category b = getByID(this.idcategory);
        if (b == null) {
            return GlobalData.repo.book_category.create(this);
        } else {
            return GlobalData.repo.book_category.update(this);
        }
    }

    public int updateData() {
        return GlobalData.repo.book_category.update(this);
    }

    public int deleteData() {
        return GlobalData.repo.book_category.delete(this);
    }

    public Book_Category getByID(int idbook) {
        return GlobalData.repo.book_category.getById(idbook);
    }

    public List<Book_Category> getAllData() {
        return GlobalData.repo.book_category.getAll();
    }
}
