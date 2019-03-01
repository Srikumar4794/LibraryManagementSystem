package LBMS;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookLoans {
	String ISBN;
	String card_id;
	java.sql.Date date;

	public BookLoans(){

	}

	public BookLoans(String a, String b){
		ISBN = a;
		card_id = b;
	}
	
	public int checkOut() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "My9@passw0rd");
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE LIBRARY;");
			
			PreparedStatement pstmt1 = con.prepareStatement("SELECT * FROM BOOK_LOANS WHERE CARD_ID = ? AND DATE_IN IS NULL;");
			pstmt1.setString(1, card_id);
			ResultSet rs1 = pstmt1.executeQuery();
			int userbookLoans = 0;					//number of book loans availed by this particular user.
			while(rs1.next())
				userbookLoans++;
			
			PreparedStatement pstmt2 = con.prepareStatement("SELECT * FROM BOOK_LOANS WHERE ISBN = ? AND DATE_IN IS NULL;");
			pstmt2.setString(1, ISBN);
			ResultSet rs2 = pstmt2.executeQuery();
			int bookLoanCheck = 0;					//Is this book already lent to someone?
			while(rs2.next())
				bookLoanCheck++;
			
			if(userbookLoans <= 3 && bookLoanCheck == 0) {
				PreparedStatement pstmt3 = con.prepareStatement("INSERT INTO BOOK_LOANS(ISBN,CARD_ID,DATE_OUT,DUE_DATE) VALUES(?,?,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 14 DAY));");
				pstmt3.setString(1,ISBN);
				pstmt3.setString(2,card_id);
				System.out.println(card_id);
				pstmt3.executeUpdate();
				//System.out.println("Thank you. Book lent.");
			}	
			else if(bookLoanCheck > 0){
				//System.out.println("Sorry, book is not available.");
				return 2;
			}
			else {
				//System.out.println("Sorry, you have exceeded the maximum amount of book loans.");
				return 3;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	public boolean checkIn() {
		int count = 0;
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "My9@passw0rd");
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE LIBRARY;");
			PreparedStatement pstmt4 = con.prepareStatement("UPDATE BOOK_LOANS SET DATE_IN = CURDATE() WHERE ISBN = ? AND DATE_IN IS NULL;");
			pstmt4.setString(1, ISBN);
			count = pstmt4.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}

		if(count > 0)
			return true;
		else
			return false;
	}
	
//	public static void main(String args[]) {
//		BookLoans b = new BookLoans();
//		b.ISBN = "344272578X";
//		b.card_id = "0000000001";
////	b.checkOut();
//		b.checkIn();
//	}
}