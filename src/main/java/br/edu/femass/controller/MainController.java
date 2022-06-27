package br.edu.femass.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button BtnCategories;

    @FXML
    private Button BtnSuppliers;

    @FXML
    private Button BtnClients;

    @FXML
    private Button BtnProducts;

    @FXML
    private Button BtnPurchases;

    @FXML
    void BtnPurchases_Action(ActionEvent event) {
        openScreen("Purchase", "Compras");
    }

    @FXML
    void BtnProducts_Action(ActionEvent event) {
        openScreen("Product", "Produtos");
    }

    @FXML
    void BtnClients_Action(ActionEvent event) {
        openScreen("Client", "Clientes");
    }

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
