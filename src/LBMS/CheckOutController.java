package LBMS;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.INACTIVE;

import java.awt.print.Book;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class CheckOutController extends Application {
    String ISBN;
    String card_id;

    @FXML
    private JFXTextField Borrower_ID;

    @FXML
    private JFXTextField ISBNVal;

    private BookLoans bookLoan;

    public CheckOutController(){
    }

    @Override
    public void start(Stage stage) throws Exception{
        System.out.println("Inside start");
        String temp = ISBN;
        Parent root = FXMLLoader.load(getClass().getResource("/UI/CheckOut.fxml"));
        Scene checkoutScene = new Scene(root);
        stage.setTitle("CHECK OUT");
        stage.setScene(checkoutScene);
        stage.show();
    }

    public void checkOutBook(String a) throws Exception {
        ISBN = a;
        start(new Stage());
    }

    @FXML
    private void submitCheckOut() {
        String card_id = Borrower_ID.getText();
        String ISBN            = ISBNVal.getText();
        bookLoan = new BookLoans(ISBN,card_id);
        System.out.println("Inside submitCheckOut method.");
        int res = bookLoan.checkOut();                              //result of invoking the checkOut method.
        System.out.println("Checked out.");
        Alert alert;
        if(res == 1){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Book is lent.");
        }
        else if (res == 2){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Book is not available!");
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Maximum limit of books exceeded for this user.");
        }
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}

