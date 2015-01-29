package vn.seasoft.sachcuatui.repocontroller;

import com.j256.ormlite.dao.Dao;
import vn.seasoft.sachcuatui.model.Book_Category;
import vn.seasoft.sachcuatui.sqlite.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Book
 *
 * @author Luong Trung
 */
public class BookCategoryController {

    Dao<Book_Category, Integer> Dao;

    public BookCategoryController(DatabaseHelper db) {
        try {
            Dao = db.getBookcategoryDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
    }

    public Dao<Book_Category, Integer> getDao() {
        return Dao;
    }

    public int create(Book_Category item) {
        try {
            return Dao.create(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Book_Category item) {
        try {
            return Dao.update(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Book_Category item) {
        try {
            return Dao.delete(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public Book_Category getById(int id) {
        try {
            return Dao.queryForId(id);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public List<Book_Category> getAll() {
        try {
            return Dao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
}
