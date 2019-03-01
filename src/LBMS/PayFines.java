package LBMS;
import java.sql.*;


public class PayFines {
	int loan_id;
	double payment_amount;
	
	public void finePayment() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "My9@passw0rd");
			Statement stmt = con.createStatement();
			double accruedFine=0; 											//to check the accrued fine from table.
			boolean isPaid = true;										//to set boolean field paid in fines table.
			stmt.executeQuery("USE LIBRARY;");
			PreparedStatement checkSelectStmt = con.prepareStatement("SELECT * FROM FINES WHERE LOAN_ID = ? AND EXISTS(SELECT * FROM BOOK_LOANS WHERE LOAN_ID = ? AND DATE_IN IS NOT NULL);");
			checkSelectStmt.setInt(1, loan_id);
			checkSelectStmt.setInt(2, loan_id);
			ResultSet rs = checkSelectStmt.executeQuery();
						
			if(rs.next()) {
				accruedFine = Math.round(rs.getFloat(2) * 100.0)/100.0;
				System.out.println(accruedFine);
			}
			
			else {
				System.out.println("Sorry! You cannot pay fine for books that are not turned in.");
				return ;
			}
						
			//if the user has not paid the full amount, paid will still remain as "False".
			if(accruedFine > payment_amount)
				isPaid = false;
			
			PreparedStatement updateStatement = con.prepareStatement("UPDATE FINES SET FINE_AMT = ?, PAID = ? WHERE LOAN_ID = ?");
			updateStatement.setDouble(1, accruedFine - payment_amount);
			updateStatement.setBoolean(2,isPaid);
			updateStatement.setInt(3,loan_id);
			updateStatement.executeUpdate();
			
			if(accruedFine - payment_amount == 0) {
				System.out.println("Thank you. Your fine has been paid in full");
			}
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		PayFines p = new PayFines();
		p.loan_id = 555569;
		p.payment_amount = 2.99;
		p.finePayment();
	}
}
