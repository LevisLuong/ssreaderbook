package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Dao.Database;

public class Book_Chapter {
	int idbook_chapter;
	String chapter;
	int idbook;
	String filename;
	String filesize;
	int approved;

	public int getIdbook_chapter() {
		return idbook_chapter;
	}

	public void setIdbook_chapter(int idbook_chapter) {
		this.idbook_chapter = idbook_chapter;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public int getIdbook() {
		return idbook;
	}

	public void setIdbook(int idbook) {
		this.idbook = idbook;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
	}

	public int addBook_Chapter() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("INSERT INTO book_chapter (`chapter`, `idbook`, `filename`,`filesize`) VALUES ('%s', '%d', '%s','%s')",
							this.chapter, this.idbook, this.filename,
							this.filesize);
			int result = stmt.executeUpdate(sqlUpdate);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (Exception e) { /* ignored */
			}

			try {
				conn.closeConnection();
			} catch (Exception e) { /* ignored */
			}
		}
		return 0;
	}

	public int updateBook_Chapter() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE book_chapter SET `chapter`='%s', `idbook`='%s', `filename`='%s', `filesize`='%s' WHERE `idbook_chapter`=%d;",
							this.chapter, this.idbook, this.filename,
							this.filesize, this.idbook_chapter);
			int result = stmt.executeUpdate(sqlUpdate);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (Exception e) { /* ignored */
			}

			try {
				conn.closeConnection();
			} catch (Exception e) { /* ignored */
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param status
	 *            0: not approve 1: approved
	 * @return
	 */
	public int setApprovedDatabase(int status) {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE book_chapter SET `approved`='%d' WHERE `idbook_chapter`=%d;",
							status, this.idbook_chapter);
			int result = stmt.executeUpdate(sqlUpdate);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (Exception e) { /* ignored */
			}

			try {
				conn.closeConnection();
			} catch (Exception e) { /* ignored */
			}
		}
		return 0;
	}

	public int deleteBook_Chapter() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE book_chapter SET isdeleted = 1 WHERE `idbook_chapter`=%d;",
							this.idbook_chapter);
			int result = stmt.executeUpdate(sqlUpdate);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (Exception e) { /* ignored */
			}

			try {
				conn.closeConnection();
			} catch (Exception e) { /* ignored */
			}
		}
		return 0;
	}

	/**
	 * @param id
	 * @return
	 */
	public Book_Chapter getById(int id) {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM book_chapter WHERE idbook_chapter = ? AND isdeleted = 0 ";
			sql = sql + "ORDER BY idbook_chapter";
			conn = new Database();
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book_Chapter bookchap = new Book_Chapter();
				bookchap.setIdbook_chapter(rs.getInt("idbook_chapter"));
				bookchap.setChapter(rs.getString("chapter"));
				bookchap.setIdbook(rs.getInt("idbook"));
				bookchap.setFilename(rs.getString("filename"));
				bookchap.setFilesize(rs.getString("filesize"));
				return bookchap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) { /* ignored */
			}
			try {
				rs.close();
			} catch (Exception e) { /* ignored */
			}
			try {
				conn.closeConnection();
			} catch (Exception e) { /* ignored */
			}
		}
		return null;
	}

	public ArrayList<Book_Chapter> getByIdBook(int idBook, int page) {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ArrayList<Book_Chapter> lstBookChap = new ArrayList<Book_Chapter>();
			String sql = "SELECT * FROM book_chapter WHERE idbook = ? AND isdeleted = 0 AND approved = 1 ";
			sql = sql + "ORDER BY idbook_chapter";
			if (page != 0) {
				int offset = (page - 1) * Book.DatasOnPage;
				sql = sql + " LIMIT " + offset + "," + Book.DatasOnPage;
			}
			conn = new Database();
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, idBook);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book_Chapter bookchap = new Book_Chapter();
				bookchap.setIdbook_chapter(rs.getInt("idbook_chapter"));
				bookchap.setChapter(rs.getString("chapter"));
				bookchap.setIdbook(rs.getInt("idbook"));
				bookchap.setFilename(rs.getString("filename"));
				bookchap.setFilesize(rs.getString("filesize"));
				lstBookChap.add(bookchap);
			}
			return lstBookChap;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) { /* ignored */
			}
			try {
				rs.close();
			} catch (Exception e) { /* ignored */
			}
			try {
				conn.closeConnection();
			} catch (Exception e) { /* ignored */
			}
		}
		return null;
	}

	public ArrayList<Book_Chapter> getAllByIdBook(int idBook) {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ArrayList<Book_Chapter> lstBookChap = new ArrayList<Book_Chapter>();
			String sql = "SELECT * FROM book_chapter WHERE idbook = ? AND isdeleted = 0 ";
			sql = sql + "ORDER BY idbook_chapter";
			conn = new Database();
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, idBook);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book_Chapter bookchap = new Book_Chapter();
				bookchap.setIdbook_chapter(rs.getInt("idbook_chapter"));
				bookchap.setChapter(rs.getString("chapter"));
				bookchap.setIdbook(rs.getInt("idbook"));
				bookchap.setFilename(rs.getString("filename"));
				bookchap.setFilesize(rs.getString("filesize"));
				bookchap.setApproved(rs.getInt("approved"));
				lstBookChap.add(bookchap);
			}
			return lstBookChap;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) { /* ignored */
			}
			try {
				rs.close();
			} catch (Exception e) { /* ignored */
			}
			try {
				conn.closeConnection();
			} catch (Exception e) { /* ignored */
			}
		}
		return null;
	}

	public int getIdAuto() {
		int id = 0;
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbssbook' AND TABLE_NAME = 'book_chapter'";
			ps = conn.Get_Connection().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt("AUTO_INCREMENT");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (Exception e) { /* ignored */
			}
			try {
				rs.close();
			} catch (Exception e) { /* ignored */
			}
			try {
				conn.closeConnection();
			} catch (Exception e) { /* ignored */
			}
		}
		return id;
	}
}
