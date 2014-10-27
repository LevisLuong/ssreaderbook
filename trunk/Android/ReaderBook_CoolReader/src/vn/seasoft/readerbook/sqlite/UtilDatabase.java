package vn.seasoft.readerbook.sqlite;

import vn.seasoft.readerbook.Util.GlobalData;
import vn.seasoft.readerbook.model.Book_Chapter;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * User: XuanTrung
 * Date: 9/30/2014
 * Time: 11:47 AM
 */
public class UtilDatabase {
    public static void updateListBookChapter(final List<Book_Chapter> lstBookchapter) {
        try {
            GlobalData.repo.book_chapter.getDao().callBatchTasks(new Callable<Book_Chapter>() {
                @Override
                public Book_Chapter call() throws Exception {
                    for (Book_Chapter book_chapter : lstBookchapter) {
                        book_chapter.addNewData();
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
