package LBMS;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayFines extends Application {
    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<DisplayFines> fineTable;
    @FXML
    private TableColumn<DisplayFines, String> card_id_col = new TableColumn<>("CARD_ID");
    @FXML
    private TableColumn<DisplayFines, Double>  payable_fine_col = new TableColumn<>("Payable Fine amount");
    private ObservableList<DisplayFines> fineObservList;


    String Card_id;
    Double Payable_fine; // fine for only those books that have been turned in.
    Alert alert; //alert displayed after the fine has been paid.

    public DisplayFines(String c, Double p){
        Card_id      = c;
        Payable_fine = p;
        fineTable = new TableView<>();
    }

    public DisplayFines(){

        fineTable = new TableView<>();
    }

    public void getFines() throws Exception {
        List<DisplayFines> finesList = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "My9@passw0rd");
            Statement groupFines = con.createStatement();
            groupFines.executeQuery("USE LIBRARY;");
            ResultSet rs = groupFines.executeQuery("SELECT A.CARD_ID, SUM(B.FINE_AMT) AS TOTAL_FINE FROM BOOK_LOANS A, FINES B " +
                                     "WHERE A.LOAN_ID = B.LOAN_ID AND A.DATE_IN IS NOT NULL" +
                                      " GROUP BY A.CARD_ID;");
            while(rs.next()){
                DisplayFines displayObj = new DisplayFines(rs.getString(1),rs.getDouble(2));
                finesList.add(displayObj);
                System.out.println(displayObj);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        displayFinesTable(finesList);
    }

    public void displayFinesTable(List<DisplayFines> finesList) throws Exception {
        System.out.println(finesList);
        fineObservList  = FXCollections.observableArrayList(finesList);
        System.out.println(fineObservList);
        card_id_col.setCellValueFactory(new PropertyValueFactory<>("cardId"));
        payable_fine_col.setCellValueFactory(new PropertyValueFactory<>("payableFine"));
        fineTable.setItems(fineObservList);
        System.out.println(fineTable);
        fineTable.getColumns().addAll(card_id_col,payable_fine_col);
        addFinesContextMenu();
        start(new Stage());
    }

    private void addFinesContextMenu() {
        //Context menu for each row
        Callback<TableView<DisplayFines>, TableRow<DisplayFines>> contextMenuCallback;
        contextMenuCallback = fineTable -> {
            final TableRow<DisplayFines> fineTableRow = new TableRow<>();
            final ContextMenu fineRowMenu = new ContextMenu();
            MenuItem payFine = new MenuItem("Pay Fine");
            payFine.setOnAction(event -> handlePayment());
            fineRowMenu.getItems().addAll(payFine);
            //display context menu only for non-null rows.
            fineTableRow.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(fineTableRow.itemProperty()))
                    .then(fineRowMenu)
                    .otherwise((ContextMenu)null));

            return fineTableRow;
        };
        fineTable.setRowFactory(contextMenuCallback);
    }

    private void handlePayment() {
        PayFines p =new PayFines();
        boolean isPaid = p.finePayment(getSelectedCardID());
        if(isPaid){
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Thank you. Fine has been paid.");
            alert.show();
        }
    }

    public String getSelectedCardID() {
        DisplayFines selectedRow = fineTable.getSelectionModel().getSelectedItem();
        String card_id_val = selectedRow.Card_id;
        System.out.println(Card_id);
        return card_id_val;
    }


    public String getCardId(){
        return Card_id;
    }

    public Double getPayableFine(){
        return Payable_fine;
    }


    @Override
    public void start(Stage stage) throws Exception {
        rootPane = new StackPane();
        rootPane.getChildren().addAll(fineTable);
        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.show();
    }
}
