package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Dao.Database;

public class GCM {
	String regID;

	public GCM() {
	}

	public GCM(String _Regid) {
		regID = _Regid;
	}

	public int registerGCM() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"INSERT INTO gcm (`regId`) VALUES ('%s')", this.regID);
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

	public int updateGCM(String newRegId) {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"UPDATE gcm SET `regId`='%s' WHERE `regId`='%s';",
					this.regID, newRegId);
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

	public int unRegisterGCM() {
		Database conn = null;
		Statement stmt = null;
		try {
			conn = new Database();
			stmt = conn.Get_Connection().createStatement();
			String sqlUpdate = String.format(
					"DELETE FROM gcm WHERE regId = '%s'", this.regID);
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

	public ArrayList<String> getAllDevices() throws Exception {
		ArrayList<String> devices = new ArrayList<String>();
		Database conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = new Database();
			String sql = "SELECT * FROM gcm";
			ps = conn.Get_Connection().prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				devices.add(rs.getString("regId"));
			}
			return devices;
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
