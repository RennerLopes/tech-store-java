package br.edu.femass.controller;

import br.edu.femass.dao.CategoryDao;
import br.edu.femass.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button BtnCategories;

    @FXML
    private Button BtnSuppliers;

    @FXML
    void BtnCategories_Action(ActionEvent event) {
        openScreen("Category", "Autores");
    }

    @FXML
    void BtnSuppliers_Action(ActionEvent event) {
        openScreen("Supplier", "Fornecedores");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void openScreen(String source, String title) {
        try {
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("/fxml/" + source + ".fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            scene.getRoot().setStyle("-fx-font-family: 'serif'");
            Stage stage = new Stage();

            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
