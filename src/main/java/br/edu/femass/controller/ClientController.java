package br.edu.femass.controller;

import br.edu.femass.dao.ClientDao;
import br.edu.femass.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private final ClientDao clientDao = new ClientDao();

    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnInclude;

    @FXML
    private ListView<Client> LstItems;

    @FXML
    private TextField TxtId;

    @FXML
    private TextField TxtAddress;

    @FXML
    private TextField TxtCpf;

    @FXML
    private TextField TxtName;

    @FXML
    private TextField TxtPhone;

    private void cleanScreen() {
        TxtName.setText("");
        TxtPhone.setText("");
        TxtCpf.setText("");
        TxtAddress.setText("");
        TxtId.setText("");
    }

    private void enableInterface(Boolean include) {
        TxtName.setDisable(!include);
        TxtAddress.setDisable(!include);
        TxtCpf.setDisable(!include);
        TxtPhone.setDisable(!include);
        BtnSave.setDisable(!include);
        BtnCancel.setDisable(!include);
        BtnInclude.setDisable(include);
        BtnDelete.setDisable(include);
        LstItems.setDisable(include);
    }

    private void displayItem() {
        Client client = LstItems.getSelectionModel().getSelectedItem();
        if(client == null) return;
        TxtName.setText(client.getName());
        TxtAddress.setText(client.getAddress());
        TxtCpf.setText(client.getCpf());
        TxtPhone.setText(client.getPhone());
        TxtId.setText(client.getId().toString());
    }

    private void refreshList() {
        List<Client> clients;

        try {
            clients = clientDao.retrieve();
        } catch (Exception e) {
            clients = new ArrayList<>();
        }
        ObservableList<Client> clientsOb = FXCollections.observableArrayList(clients);
        LstItems.setItems(clientsOb);
    }

    @FXML
    void BtnCancel_Action(ActionEvent event) {
        enableInterface(false);
    }

    @FXML
    void BtnDelete_Action(ActionEvent event) {
        Client client = LstItems.getSelectionModel().getSelectedItem();
        if(client == null) return;
        try {
            clientDao.delete(client);
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshList();
    }

    @FXML
    void BtnSave_Action(ActionEvent event) {
        Client client = new Client();
        client.setName(TxtName.getText());
        client.setPhone(TxtPhone.getText());
        client.setCpf(TxtCpf.getText());
        client.setAddress(TxtAddress.getText());
        if (TxtName.getText().equals("") || TxtPhone.getText().equals("") || TxtCpf.getText().equals("") || TxtAddress.getText().equals("")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.show();
        } else {
            try {
                clientDao.create(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
            refreshList();
            enableInterface(false);
        }
    }

    @FXML
    void BtnInclude_Action(ActionEvent event) {
        refreshList();
        enableInterface(true);
        cleanScreen();
        TxtName.requestFocus();
    }

    @FXML
    void LstItems_KeyPressed(KeyEvent event) {
        displayItem();
    }

    @FXML
    void LstItems_MouseClicked(MouseEvent event) {
        displayItem();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshList();
    }
}
