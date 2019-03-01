package LBMS;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import static javafx.application.Application.launch;

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
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;

public class DisplaySearchList extends Application {

     String searchTerm;                                                    //Input received from search bar.
     BookSearch bookSearch = new BookSearch();
     @FXML
     private StackPane rootPane;
     @FXML
     private JFXTextField searchString;
     @FXML
     private TableView<BookSearch> searchTableView;
     @FXML
     private TableColumn<BookSearch, String> ISBN  = new TableColumn<>("ISBN");
     @FXML
     private TableColumn<BookSearch, String> Title = new TableColumn<>("Title");
     @FXML
     private TableColumn<BookSearch, String> Author = new TableColumn<>("Author");
     @FXML
     private TableColumn<BookSearch, Boolean> Availability = new TableColumn<>("Availability");

    @FXML
    public void handleSearch() throws Exception {
        searchTerm = searchString.getText();
        System.out.println(searchTerm);
        start(new Stage());
    }

    @FXML
    public void handleBorrower(ActionEvent event) throws Exception{
        AddMemberController a = new AddMemberController();
        a.addnewMember();
    }

    private void loadData(String searchTerm) throws Exception {
        ISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        Title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        Author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        Availability.setCellValueFactory(new PropertyValueFactory<>("Availability"));
        List<BookSearch> arraySearchList = bookSearch.searchBookAvailability(searchTerm);
        ObservableList<BookSearch> searchList = FXCollections.observableArrayList(arraySearchList);
        searchTableView = new TableView<>();
        searchTableView.setItems(searchList);
        searchTableView.getColumns().addAll(ISBN,Title,Author,Availability);
        addContextMenu();
    }

    private void addContextMenu() {
        //Context menu for each row
        Callback<TableView<BookSearch>, TableRow<BookSearch>> contextMenuCallback;
        contextMenuCallback = searchTableView -> {
            final TableRow<BookSearch> bookSearchTableRow = new TableRow<>();
            final ContextMenu bookRowMenu = new ContextMenu();
            MenuItem checkOut = new MenuItem("Check Out");
            checkOut.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleCheckOut();
                }
            });
            bookRowMenu.getItems().addAll(checkOut);
            //display context menu only for non-null rows.
            bookSearchTableRow.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(bookSearchTableRow.itemProperty()))
                    .then(bookRowMenu)
                    .otherwise((ContextMenu)null));

            return bookSearchTableRow;
        };
        searchTableView.setRowFactory(contextMenuCallback);
    }

    private void handleCheckOut(){
        System.out.println("Inside handleCheckOut.");
        CheckOutController checkout = new CheckOutController();
        try {
            System.out.println("Inside Try block.");
            checkout.checkOutBook(getSelectedISBN());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCheckIn() throws Exception {
        CheckInController checkIn = new CheckInController();
        checkIn.startCheckIn();
    }

    @FXML
    private void handleRefresh() throws Exception{
        RefreshFines f = new RefreshFines();
        f.fineUpdate();
        Alert refreshAlert = new Alert(Alert.AlertType.INFORMATION);
        refreshAlert.setContentText("Fines have been refreshed.");
        refreshAlert.show();
    }

    @FXML
    private void handleFines() throws Exception{

    }

    public String getSelectedISBN() {
        BookSearch selectedRow = searchTableView.getSelectionModel().getSelectedItem();
        String selectedISBN = selectedRow.ISBN;
        System.out.println(selectedISBN);
        return selectedISBN;
    }

    @Override
    public void start(Stage stage) throws Exception {
        rootPane = new StackPane();
        loadData(searchTerm);
        rootPane.getChildren().addAll(searchTableView);
        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.show();
        }

    public static void main(String[] args) {
        launch(args);
    }
}

