package br.edu.femass.controller;

import br.edu.femass.dao.CategoryDao;
import br.edu.femass.model.Category;
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

public class CategoryController implements Initializable {
    private final CategoryDao categoryDao = new CategoryDao();

    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnDelete;

    @FXML
    private Button BtnSave;

    @FXML
    private Button BtnInclude;

    @FXML
    private ListView<Category> LstItems;

    @FXML
    private TextField TxtId;

    @FXML
    private TextField TxtName;

    private void cleanScreen() {
        TxtName.setText("");
        TxtId.setText("");
    }

    private void enableInterface(Boolean include) {
        TxtName.setDisable(!include);
        BtnSave.setDisable(!include);
        BtnCancel.setDisable(!include);
        BtnInclude.setDisable(include);
        BtnDelete.setDisable(include);
        LstItems.setDisable(include);
    }

    private void displayItem() {
        Category category = LstItems.getSelectionModel().getSelectedItem();
        if(category == null) return;
        TxtName.setText(category.getName());
        TxtId.setText(category.getId().toString());
    }

    private void refreshList() {
        List<Category> categories;

        try {
            categories = categoryDao.retrieve();
        } catch (Exception e) {
            categories = new ArrayList<>();
        }
        ObservableList<Category> categoriesOb = FXCollections.observableArrayList(categories);
        LstItems.setItems(categoriesOb);
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
        Category category = LstItems.getSelectionModel().getSelectedItem();

        if(category == null) return;
        try {
            categoryDao.delete(category);
        } catch (Exception e) {
            e.printStackTrace();
            displayAlert(e.getMessage());
        }

        refreshList();
    }

    @FXML
    void BtnSave_Action(ActionEvent event) {
        Category category = new Category();
        category.setName(TxtName.getText());
        if (TxtName.getText().equals("")) {
            displayAlert("Todos os campos devem ser preenchidos");
        } else {
            try {
                categoryDao.create(category);
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
