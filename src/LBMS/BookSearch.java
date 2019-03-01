package LBMS;
import java.awt.print.Book;
import java.sql.*;
import java.util.*;
import java.sql.DriverManager;


import java.util.List;
import java.util.ArrayList;


public class BookSearch {
	
	public String ISBN;
	public String Title;
	public String Author;
	public String Availability;

	public BookSearch(){

	}

	public BookSearch(String a, String b, String c, String d){
		ISBN   = a;
		Title  = b;
		Author = c;
		Availability = d;
	}

	
	public List<BookSearch> searchBookAvailability(String searchString) {
		HashMap<String,String> ISBNAuthorHash = new HashMap<String,String>();
		HashMap<String,String> ISBNTitleHash  = new HashMap<String,String>();
		String[] splitStrings = searchString.split(" ",10);
		int totalsubStrings = splitStrings.length;
		
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "My9@passw0rd");
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE LIBRARY;");
			PreparedStatement pstmt = con.prepareStatement("SELECT A.ISBN,A.TITLE,B.NAME FROM BOOK A JOIN AUTHORS B JOIN BOOK_AUTHORS C\r\n" + 
					"ON A.ISBN = C.ISBN AND B.AUTHOR_ID = C.AUTHOR_ID\r\n" + 
					"WHERE A.ISBN LIKE ?" + 
					"OR A.TITLE LIKE ?" + 
					"OR B.NAME LIKE ?" + 
					"order by A.ISBN;") ;
			
			for(int i=0; i<totalsubStrings; i++) {
					splitStrings[i] = "%" + splitStrings[i] + "%";
					//System.out.println(splitStrings[i]);
					pstmt.setString(1, splitStrings[i]);
					pstmt.setString(2, splitStrings[i]);
					pstmt.setString(3, splitStrings[i]);

				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					String currRowISBN = rs.getString(1);			 		 //ISBN of the current row.
					String title	   = rs.getString(2);
					String author_name = rs.getString(3);
					String mappedAuthor = new String();
					if(!ISBNAuthorHash.containsKey(currRowISBN)) {
						ISBNAuthorHash.put(currRowISBN,author_name);
						ISBNTitleHash.put(currRowISBN,title);
					}
					else if(!(mappedAuthor = ISBNAuthorHash.get(currRowISBN)).contains(author_name)) { //Checks for multiple authors for same ISBN and concatenates.
						mappedAuthor = mappedAuthor + ',' + author_name;
						ISBNAuthorHash.put(currRowISBN, mappedAuthor);
					}	
				}
			}

			List<BookSearch> displayList = new ArrayList<>();
			for (Map.Entry<String, String> entry : ISBNAuthorHash.entrySet()) {
			    ISBN  = entry.getKey();
			    Title = ISBNTitleHash.get(entry.getKey());
			    Author= entry.getValue();
			    
			PreparedStatement pstmt2 = con.prepareStatement("SELECT * FROM BOOK_LOANS WHERE ISBN = ? AND DATE_IN IS NULL;");
			pstmt2.setString(1, entry.getKey() );
			ResultSet r = pstmt2.executeQuery();
			if(!r.next()) 
				Availability = "Available";
			else
				Availability = "Checked out";
			displayList.add(new BookSearch(ISBN,Title,Author,Availability));
			}
			return displayList;
		}
		
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getISBN(){
		return ISBN;
	}

	public String getAuthor() {
		return Author;
	}

	public String getTitle(){
		return Title;
	}

	public String getAvailability(){
		return Availability;
	}

	//	public static void main(String args[]) {
	//		BookSearch b = new BookSearch();
	//		Scanner s = new Scanner(System.in);
	//	    System.out.println("Enter your search here: ");
	//	    String search = new String();
	//	    search = search + s.nextLine();
	//	    s.close();
	//		b.searchBookAvailability(search);
	//	}

}
