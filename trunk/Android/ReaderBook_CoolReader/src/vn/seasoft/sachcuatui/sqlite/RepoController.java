package vn.seasoft.sachcuatui.sqlite;

import android.content.Context;
import vn.seasoft.sachcuatui.repocontroller.BookCategoryController;
import vn.seasoft.sachcuatui.repocontroller.BookChapterController;
import vn.seasoft.sachcuatui.repocontroller.BookController;

public class RepoController {

    public BookController book;
    public BookCategoryController book_category;
    public BookChapterController book_chapter;
    DatabaseHelper db;
    DatabaseManager<DatabaseHelper> manager;

    public RepoController(Context context) {
        manager = new DatabaseManager<DatabaseHelper>();
        db = manager.getHelper(context);
        book = new BookController(db);
        book_category = new BookCategoryController(db);
        book_chapter = new BookChapterController(db);
    }

    public void Destroy() {
        manager.releaseHelper(db);
    }

}
