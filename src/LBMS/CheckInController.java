package LBMS;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CheckInController extends Application {
    @FXML
    private JFXTextField ISBNVal;

    Alert checkInAlert;

    @FXML
    private void submitCheckIn(){
        BookLoans bookLoan = new BookLoans();
        bookLoan.ISBN = ISBNVal.getText();
        boolean isCheckedIn = bookLoan.checkIn();
        if(isCheckedIn){
            checkInAlert = new Alert(Alert.AlertType.INFORMATION);
            checkInAlert.setContentText("Checked-in! Thank you.");
            checkInAlert.show();
        }
        else{
            checkInAlert = new Alert(Alert.AlertType.ERROR);
            checkInAlert.setContentText("Invalid details. Please confirm and try again.");
            checkInAlert.show();
        }
    }

    public void startCheckIn() throws Exception {
        start(new Stage());
    }

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/UI/CheckIn.fxml"));
        Stage stage1 = new Stage();
        stage1.setTitle("CHECK-IN");
        Scene checkInScene = new Scene(root);
        stage1.setScene(checkInScene);
        stage1.show();
    }

}
