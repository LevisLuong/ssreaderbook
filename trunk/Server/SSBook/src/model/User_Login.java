package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import Dao.Database;
import Others.Status;

public class User_Login {
	int iduser;
	String idfacebook;
	String displayname;
	String email;
	Timestamp lastlogin;

	public int getIduser() {
		return iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Timestamp lastlogin) {
		this.lastlogin = lastlogin;
	}

	public String getIdfacebook() {
		return idfacebook;
	}

	public void setIdfacebook(String idfacebook) {
		this.idfacebook = idfacebook;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public int addUser() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("INSERT INTO user_login (`idfacebook`,`displayname`,`email`,`lastlogin`) VALUES ('%s','%s','%s','%s')",
							this.idfacebook, this.displayname, this.email,
							this.lastlogin);
			int result = stmt.executeUpdate(sqlUpdate);
			if (result == 1) {
				return Status.STATUS_CREATED;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error code sql: " + e.getErrorCode());
			if (e.getErrorCode() == 1062) {
				return Status.STATUS_DUPLICATE;
			}
			return Status.ERROR_INTERNALSERVERERROR;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return Status.ERROR_INTERNALSERVERERROR;
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
		return Status.ERROR_INTERNALSERVERERROR;
	}

	public int updateUser() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE user_login SET `displayname`='%s', `email`='%s' WHERE `iduser`='%d';",
							this.displayname, this.email, this.iduser);
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

	public int updateLastloginDisplayName() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String
					.format("UPDATE user_login SET `lastlogin`='%s',`displayname`='%s' WHERE `iduser`='%d';",
							this.lastlogin, this.displayname, this.iduser);
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

	public int deleteUser() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"DELETE FROM user_login WHERE `iduser`='%d';", this.iduser);
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

	public User_Login getByUsername(String username) {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM user_login WHERE username = ? ";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			while (rs.next()) {
				this.setIduser(rs.getInt("iduser"));
				this.setEmail(rs.getString("email"));
				this.setLastlogin(rs.getTimestamp("lastlogin"));
				this.setIdfacebook(rs.getString("idfacebook"));
				this.setDisplayname(rs.getString("displayname"));
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

	public User_Login getByEmail(String email) {
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM user_login WHERE email = ? ";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();
			while (rs.next()) {
				this.setIduser(rs.getInt("iduser"));
				this.setEmail(rs.getString("email"));
				this.setLastlogin(rs.getTimestamp("lastlogin"));
				this.setIdfacebook(rs.getString("idfacebook"));
				this.setDisplayname(rs.getString("displayname"));
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

	public User_Login loginByFacebook(String idfacebook) {
		User_Login usr = null;
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM user_login WHERE idfacebook = ? ";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setString(1, idfacebook);
			rs = ps.executeQuery();
			while (rs.next()) {
				usr = new User_Login();
				usr.setIduser(rs.getInt("iduser"));
				usr.setEmail(rs.getString("email"));
				usr.setLastlogin(rs.getTimestamp("lastlogin"));
				usr.setDisplayname(rs.getString("displayname"));
				usr.setIdfacebook(idfacebook);
				return usr;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		return usr;
	}

	public int getIdAuto() {
		int id = 0;
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbssbook' AND TABLE_NAME = 'user_login'";
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

	public User_Login login(String username, String password) {
		User_Login usr = null;
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM user_login WHERE (username = ? or email = ?) and password = ? ";
			ps = conn.Get_Connection().prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, username);
			ps.setString(3, password);
			rs = ps.executeQuery();
			while (rs.next()) {
				usr = new User_Login();
				usr.setIduser(rs.getInt("iduser"));
				usr.setEmail(rs.getString("email"));
				usr.setLastlogin(rs.getTimestamp("lastlogin"));
				usr.setDisplayname(rs.getString("displayname"));
				return usr;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		return usr;
	}
}
