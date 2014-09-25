package vn.seasoft.readerbook.repocontroller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import vn.seasoft.readerbook.model.Book_Chapter;
import vn.seasoft.readerbook.sqlite.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Book
 *
 * @author Luong Trung
 */
public class BookChapterController {

    Dao<Book_Chapter, Integer> Dao;

    public BookChapterController(DatabaseHelper db) {
        try {
            Dao = db.getBookChapterDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
    }

    public int create(Book_Chapter item) {
        try {
            return Dao.create(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Book_Chapter item) {
        try {
            return Dao.update(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(Book_Chapter item) {
        try {
            return Dao.delete(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public Book_Chapter getById(int id) {
        try {
            return Dao.queryForId(id);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public Book_Chapter getNextChapter(int idbook_chapter, int idbook) {
        try {
            QueryBuilder<Book_Chapter, Integer> qb = Dao.queryBuilder();
            qb.where().eq("idbook", idbook).and().gt("idbook_chapter", idbook_chapter);
            PreparedQuery<Book_Chapter> pq = qb.prepare();
            return Dao.queryForFirst(pq);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Book_Chapter> getByIdBook(int idbook) {
        try {
            return Dao.queryForEq("idbook", idbook);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List<Book_Chapter> getAll() {
        try {
            return Dao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
}
