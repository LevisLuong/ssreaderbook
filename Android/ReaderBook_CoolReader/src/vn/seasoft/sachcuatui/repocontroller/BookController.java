package vn.seasoft.sachcuatui.repocontroller;

import com.j256.ormlite.dao.Dao;
import vn.seasoft.sachcuatui.model.Book;
import vn.seasoft.sachcuatui.sqlite.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Book
 *
 * @author Luong Trung
 */
public class BookController {

    Dao<Book, Integer> Dao;

    public BookController(DatabaseHelper db) {
        try {
            Dao = db.getBookDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
    }

    public int create(Book item) {
        try {
            return Dao.create(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Book item) {
        try {
            return Dao.update(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Book item) {
        try {
            return Dao.delete(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public Book getById(int id) {
        try {
            return Dao.queryForId(id);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public List<Book> getAll() {
        try {
            return Dao.queryBuilder().orderBy("date_created", false).query();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> getSearchBook(String keysearch) {
        try {
            String subKey = keysearch;
            if (keysearch.contains("d")) {
                subKey = keysearch.replace("d", "đ");
            }
            return Dao.queryBuilder().where().like("title", "%" + keysearch + "%").or().like("author", "%" + keysearch + "%")
                    .or().like("title", "%" + subKey + "%").or().like("author", "%" + subKey + "%")
                    .query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Book>();
    }
}
