package br.edu.femass.controller;

import br.edu.femass.dao.SupplierDao;
import br.edu.femass.model.Supplier;
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
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {
    private final SupplierDao supplierDao = new SupplierDao();

    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnInclude;

    @FXML
    private ListView<Supplier> LstItems;

    @FXML
    private TextField TxtId;

    @FXML
    private TextField TxtAddress;

    @FXML
    private TextField TxtCnpj;

    @FXML
    private TextField TxtName;

    @FXML
    private TextField TxtPhone;

    private void cleanScreen() {
        TxtName.setText("");
        TxtPhone.setText("");
        TxtCnpj.setText("");
        TxtAddress.setText("");
        TxtId.setText("");
    }

    private void enableInterface(Boolean include) {
        TxtName.setDisable(!include);
        TxtAddress.setDisable(!include);
        TxtCnpj.setDisable(!include);
        TxtPhone.setDisable(!include);
        BtnSave.setDisable(!include);
        BtnCancel.setDisable(!include);
        BtnInclude.setDisable(include);
        BtnDelete.setDisable(include);
        LstItems.setDisable(include);
    }

    private void displayItem() {
        Supplier supplier = LstItems.getSelectionModel().getSelectedItem();
        if(supplier == null) return;
        TxtName.setText(supplier.getName());
        TxtAddress.setText(supplier.getAddress());
        TxtCnpj.setText(supplier.getCnpj());
        TxtPhone.setText(supplier.getPhone());
        TxtId.setText(supplier.getId().toString());
    }

    private void refreshList() {
        List<Supplier> suppliers;

        try {
            suppliers = supplierDao.retrieve();
        } catch (Exception e) {
            suppliers = new ArrayList<>();
        }
        ObservableList<Supplier> suppliersOb = FXCollections.observableArrayList(suppliers);
        LstItems.setItems(suppliersOb);
    }

    private void displayAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setResizable(true);
        errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        errorAlert.setContentText(message);
        errorAlert.show();
    }

    @FXML
    void BtnCancel_Action(ActionEvent event) {
        enableInterface(false);
    }

    @FXML
    void BtnDelete_Action(ActionEvent event) {
        Supplier supplier = LstItems.getSelectionModel().getSelectedItem();
        if(supplier == null) return;
        try {
            supplierDao.delete(supplier);
        } catch (Exception e) {
            e.printStackTrace();
            displayAlert(e.getMessage());

        }

        refreshList();
    }

    @FXML
    void BtnSave_Action(ActionEvent event) {
        Supplier supplier = new Supplier();
        supplier.setName(TxtName.getText());
        supplier.setPhone(TxtPhone.getText());
        supplier.setCnpj(TxtCnpj.getText());
        supplier.setAddress(TxtAddress.getText());
        if (TxtName.getText().equals("") || TxtPhone.getText().equals("") || TxtCnpj.getText().equals("") || TxtAddress.getText().equals("")) {
            displayAlert("Todos os campos devem ser preenchidos");

        } else {
            try {
                supplierDao.create(supplier);
            } catch (Exception e) {
                e.printStackTrace();
                displayAlert(e.getMessage());
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
