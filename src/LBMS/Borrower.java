package LBMS;
import java.sql.*;
import java.text.DecimalFormat;

public class Borrower {
	String card_id;
	String ssn;
	String B_first_name;
	String B_last_name;
	String address;
	String city;
	String state;
	String phone;

	public Borrower(){

	}

	public Borrower(String a, String b, String c, String d, String e, String f, String g){
		ssn = a;
		B_first_name = b;
		B_last_name = c;
		address = d;
		city = e;
		state = f;
		phone = g;
	}

	public boolean insertNewBorrower() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "My9@passw0rd");
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE LIBRARY;");
			ResultSet rs = stmt.executeQuery("SELECT MAX(CARD_ID) AS MAX_CARD_ID FROM BORROWER;");
			if(rs.next())			
				card_id = rs.getString("MAX_CARD_ID");
			DecimalFormat df = new DecimalFormat("000000");
			card_id = df.format(Integer.parseInt(card_id)+1);
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO BORROWER (CARD_ID,SSN,FIRST_NAME,LAST_NAME,ADDRESS,CITY,STATE,PHONE) VALUES(?,?,?,?,?,?,?,?);");
			pstmt.setString(1, card_id);
			pstmt.setString(2, ssn);
			pstmt.setString(3, B_first_name);
			pstmt.setString(4, B_last_name);
			pstmt.setString(5, address);
			pstmt.setString(6, city);
			pstmt.setString(7, state);
			pstmt.setString(8, phone);
			pstmt.executeUpdate();
		}
		catch(SQLException e) {
			if(e.getErrorCode() == 1062){
				return false;
			}
		}
		return true;
	}
	
//	public static void main(String args[]) {
//		Borrower borrower = new Borrower();
//		borrower.ssn = "444-44-4444";
//		borrower.B_first_name = "Srikumar";
//		borrower.B_last_name = "Ramaswamy";
//		borrower.address = "7421 Frankford Road";
//		borrower.city = "Dallas";
//		borrower.state = "Texas";
//		borrower.phone = "469-380-4514";
//		borrower.insertNewBorrower();
//	}
	
	
}
