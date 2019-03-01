package LBMS;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Iterator;
import java.sql.*;

public class Books {
	List<BooksMapping> bookDetails;
	public void readCsvBooks() {
		try {
			File file = new File("C:\\Users\\ramas\\OneDrive\\Desktop\\UTD\\SEM-2\\CS 6360\\Programming Project-1\\books.csv");
			FileReader fr = new FileReader(file);
			CsvToBean<BooksMapping> bookMapBean = new CsvToBeanBuilder(fr)
					.withType(BooksMapping.class)
					.withSkipLines(1)
					.withSeparator('\t')
					.build();
			bookDetails = bookMapBean.parse();							 //parses the csvBean as a list of objects.
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		updateTables();
	 }
	
	public void updateTables() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "My9@passw0rd");
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE Library;");
			updateAuthorsTable(con);
			updateBookAuthorsTable(con);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
		
	public void updateAuthorsTable(Connection con) throws SQLException {
			Iterator<BooksMapping> booksIterator = bookDetails.iterator();
			while(booksIterator.hasNext()) {
				BooksMapping bookobj = booksIterator.next();
				String[] author_names = bookobj.Author.split(",",5);
				int author_count = 0;
				while(author_count < author_names.length) {
					String author_name = author_names[author_count];
					PreparedStatement pstmt1 = con.prepareStatement("SELECT * FROM AUTHORS WHERE NAME = ?");
					pstmt1.setString(1, author_name);
					ResultSet rs = pstmt1.executeQuery();
					rs.last(); 								//move the cursor to last row of result set.
					int count = rs.getRow();				//row# of the last row.
					if(count == 0) {						//IF no rows exist with this author_name.
						PreparedStatement pstmt2 = con.prepareStatement("INSERT INTO AUTHORS(Name) VALUES(?);");
						pstmt2.setString(1,author_name);
						pstmt2.executeUpdate();
					}
					author_count++;
				}				
			}
	}
	
	public void updateBookAuthorsTable(Connection con) throws SQLException {
			Iterator<BooksMapping> booksIterator = bookDetails.iterator();
			while(booksIterator.hasNext()) {
				BooksMapping bookObj = booksIterator.next();
				String ISBN = bookObj.ISBN10;
				String[] author_names = bookObj.Author.split(",",5);
				int author_count = 0;
				while(author_count < author_names.length) {
					PreparedStatement prepSelect = con.prepareStatement("SELECT AUTHOR_ID FROM AUTHORS WHERE NAME = ?;");
			        prepSelect.setString(1, author_names[author_count]);
					ResultSet rs = prepSelect.executeQuery();
					if(rs.next()) {
						int authorID = rs.getInt(1);
						PreparedStatement prepInsert = con.prepareStatement("INSERT INTO BOOK_AUTHORS(AUTHOR_ID,ISBN) VALUES(?,?);");
						prepInsert.setInt(1, authorID);
						prepInsert.setString(2, ISBN);
						prepInsert.executeUpdate();
					}
					author_count++;
				}
			}
		}
	
	public static void main(String args[]) {
		long start_time = System.currentTimeMillis();
		Books b = new Books();
		b.readCsvBooks();
		long end_time = System.currentTimeMillis();
		System.out.println("Total time to populate tables(in secs): " + (end_time-start_time)/60000);
	}	 
}
