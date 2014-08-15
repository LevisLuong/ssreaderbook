package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import Dao.Database;

import com.google.gson.annotations.Expose;

public class Book {

	public static final int DatasOnPage = 10;

	int idbook;
	String title;
	String author;
	String summary;
	int idcategory;
	String imagecover;
	Timestamp datecreated;
	int countview;

	@Expose(deserialize = false)
	protected int booksCount;

	public int getIdBook() {
		return idbook;
	}

	public void setIdBook(int idBook) {
		this.idbook = idBook;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getIdcategory() {
		return idcategory;
	}

	public void setIdcategory(int idcategory) {
		this.idcategory = idcategory;
	}

	public String getImagecover() {
		return imagecover;
	}

	public void setImagecover(String imagecover) {
		this.imagecover = imagecover;
	}

	public int getBooksCount() {
		return booksCount;
	}

	public void setBooksCount(int booksCount) {
		this.booksCount = booksCount;
	}

	public Timestamp getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Timestamp datecreated) {
		this.datecreated = datecreated;
	}

	public int getCountview() {
		return countview;
	}

	public void setCountview(int countview) {
		this.countview = countview;
	}

	public int addBook() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("INSERT INTO book (`idbook`, `title`, `author`, `summary`, `imagecover`, `idcategory`) VALUES ('%d', '%s', '%s', '%s','%s', '%d')",
							this.idbook, this.title, this.author, this.summary,
							this.imagecover, this.idcategory);
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

	public int updateBook() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE book SET `title`='%s', `author`='%s', `summary`='%s', `imagecover`='%s', `idcategory`='%d' WHERE `idbook`='%d';",
							this.title, this.author, this.summary,
							this.imagecover, this.idcategory, this.idbook);
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

	public int deleteBook() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"UPDATE book SET isdeleted = 1 WHERE `idbook`='%d';",
					this.idbook);
			int result = stmt.executeUpdate(sqlUpdate);
			conn.closeConnection();
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

	public Book getById(int id) {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM book WHERE idbook = ? AND isdeleted = 0 ";
			sql = sql + "ORDER BY idbook";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIdBook(rs.getInt("idbook"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setIdcategory(rs.getInt("idcategory"));
				book.setImagecover(rs.getString("imagecover"));
				book.setDatecreated(rs.getTimestamp("datecreated"));
				book.setCountview(rs.getInt("countview"));
				return book;
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

	public ArrayList<Book> searchBook(int typeSearch, String keysearch,
			int category, int page, int typeOrder) throws Exception {
		ArrayList<Book> books = new ArrayList<Book>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT SQL_CALC_FOUND_ROWS  * FROM book WHERE isdeleted = 0 ";
			if (category != 0) {
				sql = sql + "AND idcategory = ?";
			}
			if (!keysearch.equals("")) {
				switch (typeSearch) {
				case 1:
					sql = sql + " AND title LIKE '%" + keysearch + "%'";
					break;
				case 2:
					sql = sql + " AND idcategory=" + keysearch;
					break;
				case 3:
					sql = sql + " AND idbook=" + keysearch;
					break;
				}
			}
			switch (typeOrder) {
			case 1:
				sql = sql + " ORDER BY idbook";
				break;
			case 2:
				sql = sql + " ORDER BY title";
				break;
			case 3:
				sql = sql + " ORDER BY author";
				break;
			case 4:
				sql = sql + " ORDER BY summary";
				break;
			case 5:
				sql = sql + " ORDER BY imagecover";
				break;
			case 6:
				sql = sql + " ORDER BY idcategory";
				break;
			}
			int offset = (page - 1) * DatasOnPage;
			sql = sql + " LIMIT " + offset + "," + DatasOnPage;
			ps = conn.Get_Connection().prepareStatement(sql);
			if (category != 0) {
				ps.setInt(1, category);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIdBook(rs.getInt("idbook"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setIdcategory(rs.getInt("idcategory"));
				book.setImagecover(rs.getString("imagecover"));
				book.setDatecreated(rs.getTimestamp("datecreated"));
				book.setCountview(rs.getInt("countview"));
				books.add(book);
			}
			rs.close();
			rs = conn.Get_Connection().prepareStatement("SELECT FOUND_ROWS()")
					.executeQuery();
			if (rs.next()) {
				this.booksCount = rs.getInt(1);
			}
			return books;
		} catch (Exception e) {
			throw e;
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
	}

	public ArrayList<Book> searchBook(String keysearch, int category, int page)
			throws Exception {
		ArrayList<Book> books = new ArrayList<Book>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT SQL_CALC_FOUND_ROWS  * FROM book WHERE isdeleted = 0 ";
			if (category != 0) {
				sql = sql + "AND idcategory = ?";
			}
			if (!keysearch.equals("")) {
				sql = sql + " AND (title LIKE '%" + keysearch
						+ "%' OR author LIKE '%" + keysearch + "%')";
			}

			int offset = (page - 1) * DatasOnPage;
			sql = sql + " LIMIT " + offset + "," + DatasOnPage;
			Connection con = conn.Get_Connection();
			ps = con.prepareStatement(sql);
			if (category != 0) {
				ps.setInt(1, category);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIdBook(rs.getInt("idbook"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setIdcategory(rs.getInt("idcategory"));
				book.setImagecover(rs.getString("imagecover"));
				book.setDatecreated(rs.getTimestamp("datecreated"));
				book.setCountview(rs.getInt("countview"));
				books.add(book);
			}
			rs.close();
			rs = con.prepareStatement("SELECT FOUND_ROWS()").executeQuery();
			if (rs.next()) {
				this.booksCount = rs.getInt(1);
			}
			return books;
		} catch (Exception e) {
			throw e;
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
	}

	public ArrayList<Book> getAllBookByCategory(int category) throws Exception {
		ArrayList<Book> books = new ArrayList<Book>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM book WHERE idcategory = ? AND isdeleted = 0 ";
			sql = sql + " ORDER BY title";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, category);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIdBook(rs.getInt("idbook"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setIdcategory(rs.getInt("idcategory"));
				book.setImagecover(rs.getString("imagecover"));
				book.setDatecreated(rs.getTimestamp("datecreated"));
				book.setCountview(rs.getInt("countview"));
				books.add(book);
			}
			return books;
		} catch (Exception e) {
			throw e;
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
	}

	public ArrayList<Book> getMostRead(int index) throws Exception {
		ArrayList<Book> books = new ArrayList<Book>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM book WHERE isdeleted = 0 ORDER BY countview DESC ";
			int offset = (index - 1) * DatasOnPage;
			sql = sql + " LIMIT " + offset + "," + DatasOnPage;
			ps = conn.Get_Connection().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIdBook(rs.getInt("idbook"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setIdcategory(rs.getInt("idcategory"));
				book.setImagecover(rs.getString("imagecover"));
				book.setDatecreated(rs.getTimestamp("datecreated"));
				book.setCountview(rs.getInt("countview"));
				books.add(book);
			}
			return books;
		} catch (Exception e) {
			throw e;
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
	}

	public synchronized ArrayList<Book> getNewest(int index) throws Exception {
		ArrayList<Book> books = new ArrayList<Book>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM book WHERE isdeleted = 0 ORDER BY datecreated DESC ";
			int offset = (index - 1) * DatasOnPage;
			sql = sql + " LIMIT " + offset + "," + DatasOnPage;
			ps = conn.Get_Connection().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIdBook(rs.getInt("idbook"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setSummary(rs.getString("summary"));
				book.setIdcategory(rs.getInt("idcategory"));
				book.setImagecover(rs.getString("imagecover"));
				book.setDatecreated(rs.getTimestamp("datecreated"));
				book.setCountview(rs.getInt("countview"));
				books.add(book);
			}
			return books;
		} catch (Exception e) {
			throw e;
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
	}

	public int updateCountView() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"UPDATE book SET `countview`='%d' WHERE `idbook`='%d';",
					this.countview + 1, this.idbook);
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

	public int getIdAuto() {
		int id = 0;
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbssbook' AND TABLE_NAME = 'book'";
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
