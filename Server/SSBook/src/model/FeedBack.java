package model;

import java.sql.SQLException;
import java.sql.Statement;

import Dao.Database;

public class FeedBack {
	int idfeedback;
	String titlebook;
	String authorbook;
	String feedback;
	/*
	 * status
	 * 0: chua xu ly
	 * 1: da xu ly
	 */
	int status;

	public int getIdfeedback() {
		return idfeedback;
	}

	public void setIdfeedback(int idfeedback) {
		this.idfeedback = idfeedback;
	}

	public String getTitlebook() {
		return titlebook;
	}

	public void setTitlebook(String titlebook) {
		this.titlebook = titlebook;
	}

	public String getAuthorbook() {
		return authorbook;
	}

	public void setAuthorbook(String authorbook) {
		this.authorbook = authorbook;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int addFeedback() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("INSERT INTO feedback (`titlebook`, `authorbook`, `feedback`) VALUES ('%s', '%s', '%s')",
							this.titlebook, this.authorbook, this.feedback);
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

	public int updateFeedback() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE feedback SET `titlebook`='%s', `authorbook`='%s', `feedback`='%s' WHERE `idfeedback`='%d';",
							this.titlebook, this.authorbook, this.feedback,
							this.idfeedback);
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

	public int deleteFeedback() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"DELETE FROM feedback WHERE `idfeedback`='%d';",
					this.idfeedback);
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

	public int updateStatusFeedback() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"UPDATE feedback SET `status'= 1 WHERE `idfeedback`='%d';",
					this.idfeedback);
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

}
