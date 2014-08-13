package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Dao.Database;

public class Book_Category {
	int idcategory;
	String category;

	public int getIdCategory() {
		return idcategory;
	}

	public void setIdCategory(int idCategory) {
		this.idcategory = idCategory;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int addBookCategory() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("INSERT INTO book_category (`idcategory`, `category`) VALUES ('%d', '%s')",
							this.idcategory, this.category);
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

	public int delete() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE book_category SET isdeleted = 1 WHERE `idcategory`='%d';",
							this.idcategory);
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
					.format("UPDATE book_category SET `category`='%s' WHERE `idcategory`='%d';",
							this.category, this.idcategory);
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

	public ArrayList<Book_Category> getAllCategory() throws Exception {
		ArrayList<Book_Category> bcs = new ArrayList<Book_Category>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM book_category WHERE isdeleted = 0";
			ps = conn.Get_Connection().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book_Category bc = new Book_Category();
				bc.setIdCategory(rs.getInt("idcategory"));
				bc.setCategory(rs.getString("category"));
				bcs.add(bc);
			}
			return bcs;
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

	public int getNumBookByCate(int idcategory) {
		int id = 0;
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT COUNT(*) FROM book WHERE idcategory=?";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, idcategory);
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

	public Book_Category getById(int id) {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM book_category WHERE idcategory = ? AND isdeleted = 0";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				Book_Category bc = new Book_Category();
				bc.setIdCategory(rs.getInt("idcategory"));
				bc.setCategory(rs.getString("category"));
				return bc;
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
}
