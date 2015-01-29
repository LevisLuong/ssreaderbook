package vn.seasoft.sachcuatui.repocontroller;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import vn.seasoft.sachcuatui.model.Book_Chapter;
import vn.seasoft.sachcuatui.sqlite.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

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

    public Dao<Book_Chapter, Integer> getDao() {
        return Dao;
    }

    public int create(final Book_Chapter item) {
        try {
        	 return Dao.create(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(final Book_Chapter item) {

        try {
        	return Dao.update(item);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(final Book_Chapter item) {
        try {
            Dao.callBatchTasks(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return Dao.delete(item);
                }
            });
            return 1;
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        } catch (Exception e) {
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
