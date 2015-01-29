package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import Dao.Database;

public class Comment {
	public static final int DatasOnPage = 10;

	int idcomment;
	String content;
	int iduser;
	int idbook;
	Timestamp datecreated;
	int isdeleted;

	public int getIdcomment() {
		return idcomment;
	}

	public void setIdcomment(int idcomment) {
		this.idcomment = idcomment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIdbook() {
		return idbook;
	}

	public void setIdbook(int idbook) {
		this.idbook = idbook;
	}

	public Timestamp getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Timestamp datecreated) {
		this.datecreated = datecreated;
	}

	public int getIduser() {
		return iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public int getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(int isdeleted) {
		this.isdeleted = isdeleted;
	}

	public int addComment() {
		Database conn = null;
		PreparedStatement stmt = null;
		try {
			conn = new Database();
			String sqlUpdate = "INSERT INTO comment (`content`, `iduser`, `idbook`) VALUES (?, ?,?)";
			stmt = conn.Get_Connection().prepareStatement(sqlUpdate);
			stmt.setString(1, this.content);
			stmt.setInt(2, this.iduser);
			stmt.setInt(3, this.idbook);
			int result = stmt.executeUpdate();
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
			String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbssbook' AND TABLE_NAME = 'comment'";
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

	public int updateComment() {
		Database conn = null;
		PreparedStatement stmt = null;
		try {
			conn = new Database();

			String sqlUpdate = String
					.format("UPDATE comment SET `content`=?, `iduser`=?, `idbook`=? WHERE `idcomment`='%d';",
							this.idcomment);
			stmt = conn.Get_Connection().prepareStatement(sqlUpdate);
			stmt.setString(1, this.content);
			stmt.setInt(2, this.iduser);
			stmt.setInt(3, this.idbook);
			int result = stmt.executeUpdate();
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

	public int deleteComment() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"DELETE FROM comment WHERE `idcomment`='%d';",
					this.idcomment);
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

	public Comment getByID(int idcomment) {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM comment WHERE idcomment = ? ";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, idcomment);
			rs = ps.executeQuery();
			while (rs.next()) {
				this.setIdcomment(rs.getInt("idcomment"));
				this.setContent(rs.getString("content"));
				this.setIdbook(rs.getInt("idbook"));
				this.setIduser(rs.getInt("iduser"));
				this.setDatecreated(rs.getTimestamp("datecreated"));
				return this;
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

	public ArrayList<Comment> getListCommentByIdBook(int idbook, int page)
			throws Exception {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM comment WHERE idbook = ? AND isdeleted = 0 ORDER BY datecreated desc";

			if (page != 0) {
				int offset = (page - 1) * DatasOnPage;
				sql = sql + " LIMIT " + offset + "," + DatasOnPage;
			}
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setInt(1, idbook);
			rs = ps.executeQuery();
			while (rs.next()) {
				Comment cmt = new Comment();
				cmt.setIdcomment(rs.getInt("idcomment"));
				cmt.setContent(rs.getString("content"));
				cmt.setIdbook(rs.getInt("idbook"));
				cmt.setIduser(rs.getInt("iduser"));
				cmt.setDatecreated(rs.getTimestamp("datecreated"));
				comments.add(cmt);
			}
			return comments;
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
}
