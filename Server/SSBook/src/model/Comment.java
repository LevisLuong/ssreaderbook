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
	String iduserfacebook;
	String username;
	int idbook;
	Timestamp datecreated;

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

	public String getIduserfacebook() {
		return iduserfacebook;
	}

	public void setIduserfacebook(String iduserfacebook) {
		this.iduserfacebook = iduserfacebook;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int addComment() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("INSERT INTO comment (`content`, `iduserfacebook`,`username`, `idbook`) VALUES ('%s', '%s','%s', '%d')",
							this.content, this.iduserfacebook, this.username,
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

	public int updateBook() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE comment SET `content`='%s',`username`='%s', `idbook`='%d', `iduserfacebook`='%s' WHERE `idcomment`='%d';",
							this.content, this.username, this.idbook,
							this.iduserfacebook, this.idcomment);
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

	public ArrayList<Comment> getListCommentByIdBook(int idbook, int page)
			throws Exception {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM comment WHERE idbook = ? ORDER BY datecreated desc";

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
				cmt.setIduserfacebook(rs.getString("iduserfacebook"));
				cmt.setUsername(rs.getString("username"));
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
