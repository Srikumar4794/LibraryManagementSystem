package LBMS;
import java.sql.*;


public class PayFines {

	public boolean finePayment(String card_id) {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "My9@passw0rd");
			Statement stmt = con.createStatement();
//			double accruedFine=0; 											//to check the accrued fine from table.
//			boolean isPaid = true;										//to set boolean field paid in fines table.
			stmt.executeQuery("USE LIBRARY;");
			PreparedStatement checkSelectStmt = con.prepareStatement("SELECT LOAN_ID FROM BOOK_LOANS WHERE CARD_ID = ? AND DATE_IN IS NOT NULL;");
			checkSelectStmt.setString(1,card_id);
			ResultSet rs = checkSelectStmt.executeQuery();
						
//			if(rs.next())
//				accruedFine = Math.round(rs.getFloat(2) * 100.0)/100.0;
//				System.out.println(accruedFine);
//			}

			//if the user has not paid the full amount, paid will still remain as "False".
//			if(accruedFine > payment_amount)
//				isPaid = false;


			while(rs.next()){
				PreparedStatement updateStatement = con.prepareStatement("UPDATE FINES SET FINE_AMT = 0, PAID = ? WHERE LOAN_ID = ?");
				updateStatement.setBoolean(1,true);
				updateStatement.setString(2,rs.getString(1));
				updateStatement.executeUpdate();
			}

//			updateStatement.executeUpdate();
//
//			if(accruedFine - payment_amount == 0) {
//				System.out.println("Thank you. Your fine has been paid in full");
//			}
			return true;
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
//	public static void main(String args[]) {
//		PayFines p = new PayFines();
//		p.loan_id = 555569;
//		p.payment_amount = 2.99;
//		p.finePayment();
//	}
}
