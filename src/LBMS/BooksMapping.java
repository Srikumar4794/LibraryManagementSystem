package LBMS;
import com.opencsv.bean.*;

public class BooksMapping {
    @CsvBindByPosition(position = 0)
    public String ISBN10;

    @CsvBindByPosition(position = 1)
    public String ISBN13;

    @CsvBindByPosition(position = 2)
    public String Title;
    
    @CsvBindByPosition(position = 3)
    public String Author;
    
    @CsvBindByPosition(position = 4)
    public String Cover;
    
    @CsvBindByPosition(position = 5)
    public String Publisher;
    
    @CsvBindByPosition(position = 6)
    public int pages;
}
