package vn.seasoft.sachcuatui.model;

import com.j256.ormlite.field.DatabaseField;
import vn.seasoft.sachcuatui.Util.GlobalData;

import java.util.List;

/**
 * User: XuanTrung
 * Date: 7/9/2014
 * Time: 9:32 AM
 */
public class Book_Chapter {
    @DatabaseField(id = true)
    Integer idbook_chapter;
    @DatabaseField
    String chapter;
    @DatabaseField
    String filename;
    @DatabaseField
    Integer idbook;
    @DatabaseField
    String filesize;
    @DatabaseField(defaultValue = "false")
    Boolean currentread;
    @DatabaseField(defaultValue = "false")
    Boolean isDownloaded;
    @DatabaseField
    Integer readposition;
    public Book_Chapter() {
        currentread = false;
        isDownloaded = false;
    }

    public Integer getIdbook_chapter() {
        return idbook_chapter;
    }

    public void setIdbook_chapter(Integer idbook_chapter) {
        this.idbook_chapter = idbook_chapter;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getIdbook() {
        return idbook;
    }

    public void setIdbook(int idbook) {
        this.idbook = idbook;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public Boolean getCurrentread() {
        return currentread;
    }

    public void setCurrentread(Boolean currentread) {
        this.currentread = currentread;
    }

    public Boolean getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(Boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public Integer getReadposition() {
        return readposition;
    }

    public void setReadposition(Integer readposition) {
        this.readposition = readposition;
    }

    public int addNewData() {
        Book_Chapter b = getByID(this.idbook_chapter);
        if (b == null) {
            return GlobalData.repo.book_chapter.create(this);
        } else {
            this.currentread = b.currentread;
            this.isDownloaded = b.isDownloaded;
            this.readposition = b.readposition;
            return GlobalData.repo.book_chapter.update(this);
        }
    }

    public int updateData() {
        return GlobalData.repo.book_chapter.update(this);
    }

    public int deleteData() {
        return GlobalData.repo.book_chapter.delete(this);
    }

    public Book_Chapter getByID(int idbook) {
        return GlobalData.repo.book_chapter.getById(idbook);
    }

    public List<Book_Chapter> getAllData() {
        return GlobalData.repo.book_chapter.getAll();
    }

    public Book_Chapter getNextChapter() {
        return GlobalData.repo.book_chapter.getNextChapter(this.idbook_chapter, this.idbook);
    }

    public List<Book_Chapter> getByidBook(int idbook) {
        return GlobalData.repo.book_chapter.getByIdBook(idbook);
    }
}
