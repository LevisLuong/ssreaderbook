package vn.seasoft.readerbook.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import vn.seasoft.readerbook.model.Book;
import vn.seasoft.readerbook.model.Book_Category;
import vn.seasoft.readerbook.model.Book_Chapter;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "dbssbook.db";
    private static final int DATABASE_VERSION = 1;
    /*
     * add a new DAO object with the type of your model
     */
    private Dao<Book, Integer> bookDao = null;
    private Dao<Book_Chapter, Integer> bookchapterDao = null;
    private Dao<Book_Category, Integer> bookcategoryDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // DatabaseInitializer initializer = new DatabaseInitializer(context);
        // try {
        // initializer.createDatabase();
        // initializer.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            System.out.println("Create empty table");
            TableUtils.createTable(connectionSource, Book.class);
            TableUtils.createTable(connectionSource, Book_Category.class);
            TableUtils.createTable(connectionSource, Book_Chapter.class);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Book.class, true);
            TableUtils.dropTable(connectionSource, Book_Category.class, true);
            TableUtils.dropTable(connectionSource, Book_Chapter.class, true);
            onCreate(db, connectionSource);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // ----------Get dao

    public Dao<Book, Integer> getBookDao() throws SQLException {
        if (bookDao == null) {
            bookDao = getDao(Book.class);
        }
        return bookDao;
    }

    public Dao<Book_Category, Integer> getBookcategoryDao() throws SQLException {
        if (bookcategoryDao == null) {
            bookcategoryDao = getDao(Book_Category.class);
        }
        return bookcategoryDao;
    }
    public Dao<Book_Chapter, Integer> getBookChapterDao() throws SQLException {
        if (bookchapterDao == null) {
            bookchapterDao = getDao(Book_Chapter.class);
        }
        return bookchapterDao;
    }

    @Override
    public void close() {
        super.close();
        bookDao = null;
        bookcategoryDao = null;
        bookchapterDao= null;
    }
}
