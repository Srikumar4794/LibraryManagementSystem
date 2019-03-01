package LBMS;

import LBMS.Borrower;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddMemberController extends Application {
    @FXML
    JFXTextField B_SSN;
    @FXML
    JFXTextField B_First_name;
    @FXML
    JFXTextField B_Last_name;
    @FXML
    JFXTextField B_Street_Address;
    @FXML
    JFXTextField B_City;
    @FXML
    JFXTextField B_State;
    @FXML
    JFXTextField B_PhoneNum;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/UI/AddMember.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Add new borrower");
        stage.show();
    }

    public void addnewMember() throws Exception {
        Stage window = new Stage();
        start(window);
    }

    @FXML
    private void handleBdetails(ActionEvent event) {

        String ssn            = B_SSN.getText();
        String first_name     = B_First_name.getText();
        String last_name      = B_Last_name.getText();
        String street_Address = B_Street_Address.getText();
        String city           = B_City.getText();
        String state          = B_State.getText();
        String phone_num      = B_PhoneNum.getText();

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        Alert failAlert    = new Alert(Alert.AlertType.ERROR);

        if(ssn.isEmpty() || first_name.isEmpty() || last_name.isEmpty() || street_Address.isEmpty() || city.isEmpty() || state.isEmpty() || phone_num.isEmpty()){
            failAlert.setContentText("Please fill all the details and submit");
            failAlert.show();
            return ;
        }

        Borrower borrower = new Borrower(ssn,first_name,last_name,street_Address,city,state,phone_num);
        boolean validBorrowerDetails = borrower.insertNewBorrower();
        if(validBorrowerDetails) {
            successAlert.setContentText("Borrower has been added.");
            successAlert.show();
        }
        else{
            failAlert.setContentText("Borrower already exists. Please check and try again.");
            failAlert.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
