package com.fxml.virtualmemorymanagementsimulation;

import com.main.virtualmemorymanagementsimulation.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    Button Browse;
    @FXML
    TextField BrowseTextField;

    @FXML
    TextField Memorysize;
    @FXML
    TextField  minmumFramesProcess;
    @FXML
    TextField NumberOfProcesses1;
    @FXML
    TextField Time;
    @FXML
    RadioButton fifo;
    @FXML
    RadioButton Second_chanceFIFO;
    @FXML
    RadioButton LRU;
    @FXML
    RadioButton Clock;

   public TableView<TableViewData> result;
   public TableColumn<TableViewData, Integer> PID;
   public TableColumn<TableViewData, Integer> NumberOfFaultsT;
   public TableColumn<TableViewData, Double> arrival;
   public TableColumn<TableViewData, Double> completion;
    public TableColumn<TableViewData, Double> TurnAround;
    public TableColumn<TableViewData, Double> WaitingTime;

    ObservableList<TableViewData> observableListResult = FXCollections.observableArrayList();

    @FXML
    protected void onBrowseButtonClick() throws IOException {
        Stage stage=new Stage();

        Browse.setOnAction(actionEvent -> {

            FileChooser fileChooserShares = new FileChooser();
            fileChooserShares.setTitle("Select daily price file .txt");
            fileChooserShares.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("TXT files (' txt')", "*.txt")
            );
            File selectedFile = fileChooserShares.showOpenDialog(stage);
            if (String.valueOf(selectedFile).equals("null")) {
                return;
            }
            else{
                BrowseTextField.setText(selectedFile.toString());
            }
        });
    }

    @FXML
    protected void onReadButtonClick() throws IOException {

        result.getItems().clear();

        Alert alert=new Alert(Alert.AlertType.ERROR);
        if (BrowseTextField.getText()==null ||BrowseTextField.getText().isBlank() || BrowseTextField.getText().isBlank() ){
            alert.setContentText("Path can't be Empty.");
            alert.show();
            return;
        }else {
            Alert alert2=new Alert(Alert.AlertType.ERROR);
            Alert alert3=new Alert(Alert.AlertType.ERROR);

            if (Time.getText().isBlank() || Time.getText().isEmpty() ||Time.getText()==null){
                alert2.setContentText("Time can't be Empty.");
                alert2.show();
                return;
            }else {
                Main.Time = Integer.parseInt(Time.getText());
            }

            if (fifo.isSelected()){
                Main.read(BrowseTextField.getText(),"FIFOMMU");
                observableListResult.addAll(TableViewData.read());


            }else if (Second_chanceFIFO.isSelected()){
                Main.read(BrowseTextField.getText(),"SecondChanceFIFOMMU");
                observableListResult.addAll(TableViewData.read());

            }else if (LRU.isSelected()){
                Main.read(BrowseTextField.getText(),"LRUMMU");
                observableListResult.addAll(TableViewData.read());

            }else if (Clock.isSelected()){
                Main.read(BrowseTextField.getText(),"ClockMMU");
                observableListResult.addAll(TableViewData.read());

            }else {
                alert3.setContentText("Algorithm can't be Empty.");
                alert3.show();
                return;
            }
            Memorysize.setText(String.valueOf(Main.m));
            minmumFramesProcess.setText(String.valueOf(Main.s));
            NumberOfProcesses1.setText(String.valueOf(Main.n));
        }
        }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PID.setCellValueFactory(new PropertyValueFactory<>("PID"));
        NumberOfFaultsT.setCellValueFactory(new PropertyValueFactory<>("NumberOfFaultsT"));
        arrival.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        completion.setCellValueFactory(new PropertyValueFactory<>("completion"));
        TurnAround.setCellValueFactory(new PropertyValueFactory<>("TurnAround"));
        WaitingTime.setCellValueFactory(new PropertyValueFactory<>("WaitingTime"));
        result.getItems().clear();
        result.setItems(observableListResult);
    }

}