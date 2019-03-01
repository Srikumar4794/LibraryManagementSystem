package LBMS;

import java.sql.*;

public class RefreshFines {

	public void fineUpdate() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "My9@passw0rd");
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE LIBRARY;");

			//Update fines for existing rows in fines table.
			ResultSet rs = stmt.executeQuery("SELECT LOAN_ID, DATEDIFF(CURDATE(),DUE_DATE) * 0.25 FROM BOOK_LOANS WHERE DATE_IN IS NULL;");

			while(rs.next()) {
				String updatesql = " UPDATE FINES" +
						" SET FINE_AMT = ?" +
						" WHERE PAID = FALSE AND LOAN_ID = ?";

				PreparedStatement updateStmt = con.prepareStatement(updatesql);

				updateStmt.setFloat(1, rs.getFloat(2));
				updateStmt.setInt(2,rs.getInt(1));
				updateStmt.executeUpdate();
			}

			//Scenario-1- Insert fines for books that have been turned in.
			String sqlstmt1 = "INSERT INTO FINES(LOAN_ID,PAID,FINE_AMT)" + 
							  "(SELECT LOAN_ID,FALSE, DATEDIFF(DATE_IN,DUE_DATE)*0.25 FROM BOOK_LOANS" +
							  " WHERE  DUE_DATE < DATE_IN AND DATE_IN IS NOT NULL" + 
							  " AND LOAN_ID NOT IN(SELECT LOAN_ID FROM FINES) ) ;";
			
			PreparedStatement insertStmt1 = con.prepareStatement(sqlstmt1);
			insertStmt1.executeUpdate();
			
			//Scenario-2 - Insert fines for books that have not been turned in.
			String sqlstmt2 = "INSERT INTO FINES(LOAN_ID,PAID,FINE_AMT)" + 
							  "(SELECT LOAN_ID,FALSE, DATEDIFF(curdate(),DUE_DATE)*0.25 FROM BOOK_LOANS" +
							  " WHERE  DATE_IN is NULL AND DUE_DATE < curdate()" + 
							  " AND LOAN_ID NOT IN(SELECT LOAN_ID FROM FINES) ) ;" ;
			PreparedStatement insertStmt2 = con.prepareStatement(sqlstmt2);
			insertStmt2.executeUpdate();
			}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
//
//
//	public static void main(String args[]) {
//		RefreshFines f = new RefreshFines();
//		f.fineUpdate();
//	}
}
