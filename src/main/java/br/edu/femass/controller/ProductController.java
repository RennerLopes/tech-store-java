package br.edu.femass.controller;

import br.edu.femass.dao.CategoryDao;
import br.edu.femass.dao.ProductDao;
import br.edu.femass.model.Category;
import br.edu.femass.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
    private final ProductDao productDao = new ProductDao();
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
    private ListView<Product> LstItems;

    @FXML
    private TextField TxtId;

    @FXML
    private TextField TxtPurchasePrice;

    @FXML
    private TextField TxtSalePrice;

    @FXML
    private TextField TxtStock;

    @FXML
    private TextField TxtName;

    @FXML
    private ComboBox<Category> CbCategory;

    private void cleanScreen() {
        TxtName.setText("");
        TxtPurchasePrice.setText("");
        TxtSalePrice.setText("");
        TxtStock.setText("");
        CbCategory.setValue(null);
        TxtId.setText("");
    }

    private void enableInterface(Boolean include) {
        TxtName.setDisable(!include);
        TxtPurchasePrice.setDisable(!include);
        TxtSalePrice.setDisable(!include);
        CbCategory.setDisable(!include);
        BtnSave.setDisable(!include);
        BtnCancel.setDisable(!include);
        BtnInclude.setDisable(include);
        BtnDelete.setDisable(include);
        LstItems.setDisable(include);
    }

    private void displayItem() {
        Product product = LstItems.getSelectionModel().getSelectedItem();
        if(product == null) return;
        TxtName.setText(product.getName());
        TxtPurchasePrice.setText(product.getPurchasePrice().toString());
        TxtSalePrice.setText(product.getSalePrice().toString());
        TxtStock.setText(product.getStock().toString());
        CbCategory.setValue(product.getCategory());
        TxtId.setText(product.getId().toString());
    }

    private void refreshList() {
        List<Product> products;

        try {
            products = productDao.retrieve();
        } catch (Exception e) {
            products = new ArrayList<>();
        }
        ObservableList<Product> productsOb = FXCollections.observableArrayList(products);
        LstItems.setItems(productsOb);
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
        Product product = LstItems.getSelectionModel().getSelectedItem();
        if(product == null) return;
        try {
            productDao.delete(product);
        } catch (Exception e) {
            e.printStackTrace();
            displayAlert(e.getMessage());
        }

        refreshList();
    }

    @FXML
    void BtnSave_Action(ActionEvent event) {
        Product product = new Product();
        product.setName(TxtName.getText());
        product.setPurchasePrice(new BigDecimal(TxtPurchasePrice.getText()));
        product.setSalePrice(new BigDecimal(TxtSalePrice.getText()));
        product.setStock(0);
        product.setCategory(CbCategory.getValue());
        if (TxtName.getText().equals("") || TxtPurchasePrice.getText().equals("") || TxtSalePrice.getText().equals("") || CbCategory.getValue() == null) {
            displayAlert("Todos os campos devem ser preenchidos");
        } else {
            try {
                productDao.create(product);
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
        List<Category> categories;

        try {
            categories = categoryDao.retrieve();
        } catch (Exception e) {
            categories = new ArrayList<>();
        }
        ObservableList<Category> categoriesOb = FXCollections.observableArrayList(categories);
        CbCategory.setItems(categoriesOb);

        refreshList();
    }
}
