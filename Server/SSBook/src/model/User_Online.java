package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import Dao.Database;

public class User_Online {
	int iduser_online;
	String ipuser;
	Timestamp time_access;
	int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIduser_online() {
		return iduser_online;
	}

	public void setIduser_online(int iduser_online) {
		this.iduser_online = iduser_online;
	}

	public String getIpuser() {
		return ipuser;
	}

	public void setIpuser(String ipuser) {
		this.ipuser = ipuser;
	}

	public Timestamp getTime_access() {
		return time_access;
	}

	public void setTime_access(Timestamp time_access) {
		this.time_access = time_access;
	}

	public int addUserOnline() {
		// check remove invalid user..
		deleteUserOnline();
		// Add or update user
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"INSERT INTO user_online (`ipuser`) VALUES ('%s')",
					this.ipuser);
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

	public int deleteUserByIP(String ip) {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE user_online SET status = 0 WHERE ipuser = '"
							+ ip + "'");
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

	public int updateUserOnline() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE user_online SET `ipuser`='%s',`status`='%d' WHERE `iduser_online`='%d';",
							this.ipuser, this.status, this.iduser_online);
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

	private int deleteUserOnline() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE user_online db SET status = 0 WHERE (SELECT TIMESTAMPDIFF(MINUTE,db.time_access,(SELECT NOW())) > 180)");
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

	public User_Online getUserOnlineByIP(String ip) {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM user_online WHERE ipuser = ? Order By time_access desc ";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setString(1, ip);
			rs = ps.executeQuery();
			while (rs.next()) {
				User_Online uo = new User_Online();
				uo.setIduser_online(rs.getInt("iduser_online"));
				uo.setIpuser(rs.getString("ipuser"));
				uo.setTime_access(rs.getTimestamp("time_access"));
				uo.setStatus(rs.getInt("status"));
				return uo;
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

	public int countUser_Online() {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT COUNT(distinct ipuser) FROM user_online where status = 1";
			ps = conn.Get_Connection().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				int count = rs.getInt(1);
				return count;
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
		return 0;
	}
}
