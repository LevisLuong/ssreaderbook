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
	int countdownload;
	String uploader;
	int approved;

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

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public int getCountdownload() {
		return countdownload;
	}

	public void setCountdownload(int countdownload) {
		this.countdownload = countdownload;
	}

	public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
	}

	public int addBook() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("INSERT INTO book (`idbook`, `title`, `author`, `summary`, `imagecover`, `idcategory`,`uploader`) VALUES ('%d', '%s', '%s', '%s','%s', '%d','%s')",
							this.idbook, this.title, this.author, this.summary,
							this.imagecover, this.idcategory, this.uploader);
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

	public int updateDateCreated() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"UPDATE book SET `datecreated`='%s' WHERE `idbook`='%d';",
					this.datecreated, this.idbook);
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

	public int updateApproved() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"UPDATE book SET `approved`='%d' WHERE `idbook`='%d';",
					this.approved, this.idbook);
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
					.format("UPDATE book SET `title`='%s', `author`='%s', `summary`='%s', `imagecover`='%s', `idcategory`='%d',`uploader`='%s' WHERE `idbook`='%d';",
							this.title, this.author, this.summary,
							this.imagecover, this.idcategory, this.uploader,
							this.idbook);
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
				book.setCountdownload(rs.getInt("countdownload"));
				book.setUploader(rs.getString("uploader"));
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

	/**
	 * Seach book for backend
	 * 
	 * @param typeSearch
	 * @param keysearch
	 * @param category
	 * @param page
	 * @param typeOrder
	 * @return
	 * @throws Exception
	 */
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
				book.setCountdownload(rs.getInt("countdownload"));
				book.setUploader(rs.getString("uploader"));
				book.setApproved(rs.getInt("approved"));
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

	/**
	 * Seach book for book services
	 * 
	 * @param keysearch
	 * @param category
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Book> searchBook(String keysearch, int category, int page,
			boolean ApproveBook) throws Exception {
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
			if (ApproveBook) {
				sql = sql + " AND approved = 1 ";
			}

			if (!keysearch.equals("")) {
				if (keysearch.contains("d")) {
					String temp = keysearch.replace("d", "đ");
					keysearch = keysearch + "<>" + temp;
				}

				sql = sql + " AND (";
				String[] arrKeySearch = keysearch.split("<>");
				for (int i = 0; i < arrKeySearch.length; i++) {
					sql = sql + "title LIKE '%" + arrKeySearch[i]
							+ "%' OR author LIKE '%" + arrKeySearch[i] + "%'";
					if (i != arrKeySearch.length - 1) {
						sql = sql + " OR ";
					}

				}
				sql = sql + ")";
			}

			int offset = (page - 1) * DatasOnPage;
			sql = sql + " LIMIT " + offset + "," + DatasOnPage;

			System.out.println("Cau truy van tim kiem: " + sql);
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
				book.setCountdownload(rs.getInt("countdownload"));
				book.setUploader(rs.getString("uploader"));
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

	/**
	 * Get all book by category for BookServices
	 * 
	 * @param category
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Book> getAllBookByCategory(int category, int page,
			boolean ApproveBook) throws Exception {
		ArrayList<Book> books = new ArrayList<Book>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM book WHERE idcategory = ? AND isdeleted = 0 ";
			if (ApproveBook) {
				sql = sql + " AND approved = 1 ";
			}
			sql = sql + " ORDER BY title";
			if (page != 0) {
				int offset = (page - 1) * DatasOnPage;
				sql = sql + " LIMIT " + offset + "," + DatasOnPage;
			}
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
				book.setCountdownload(rs.getInt("countdownload"));
				book.setUploader(rs.getString("uploader"));
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

	public ArrayList<Book> getMostRead(int index, boolean ApproveBook)
			throws Exception {
		ArrayList<Book> books = new ArrayList<Book>();
		// kiểm tra số lượng = 20
		if (index == 3) {
			return books;
		}
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM book WHERE isdeleted = 0 ";
			if (ApproveBook) {
				sql = sql + " AND approved = 1 ";
			}
			sql = sql + "ORDER BY countdownload DESC ";
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
				book.setCountdownload(rs.getInt("countdownload"));
				book.setUploader(rs.getString("uploader"));
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

	public synchronized ArrayList<Book> getNewest(int index, boolean ApproveBook)
			throws Exception {
		ArrayList<Book> books = new ArrayList<Book>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM book WHERE isdeleted = 0 AND  (SELECT TIMESTAMPDIFF(DAY,datecreated,(SELECT datecreated from book order by datecreated desc limit 1,1)) < 3)";
			if (ApproveBook) {
				sql = sql + " AND approved = 1 ";
			}
			sql = sql + " ORDER BY datecreated DESC ";

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
				book.setCountdownload(rs.getInt("countdownload"));
				book.setUploader(rs.getString("uploader"));
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

	public int countChapter() {
		int id = 0;
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT count(*) FROM dbssbook.book_chapter where idbook = ? && isdeleted = 0;";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, this.idbook);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
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

	public int updateCountDownload() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE book SET `countdownload`='%d' WHERE `idbook`='%d';",
							this.countdownload + 1, this.idbook);
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
