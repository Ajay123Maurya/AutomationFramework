package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author AkashAggarwal
 * @description this class will be used for DBqueries
 *
 */
public class DataBaseActions {

	private static DataBaseActions dataBaseActions;

	Map<Object, Object> elementMap;
	String URL = "";
	String PASSWORD = "";
	String USER = "";

	public String getDataFromDB(String query, int columnIndex) {
		URL = elementMap.get("CONNECTION_URL").toString();
		USER = elementMap.get("DB_USER").toString();
		PASSWORD = elementMap.get("DB_PASSWORD").toString();
		String data = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				data = rs.getString(columnIndex);
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return data;
	}

	public void deleteFromDB(List<String> queries) {
		URL = elementMap.get("CONNECTION_URL").toString();
		USER = elementMap.get("DB_USER").toString();
		PASSWORD = elementMap.get("DB_PASSWORD").toString();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt = con.createStatement();
			for (int i = 0; i < queries.size(); i++) {
				stmt.addBatch(queries.get(i));
			}
			int[] executeBatch = stmt.executeBatch();
			System.out.println(executeBatch);
			System.out.println("done");
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
//	public static void main(String args[]){
//		DataBaseActions data = DataBaseActions.getInstance();
//		data.getDataFromDB("SELECT * from cscart_otp_details", 1);
//	}

}
