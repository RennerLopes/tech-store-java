package br.edu.femass.controller;

import br.edu.femass.dao.ProductDao;
import br.edu.femass.dao.PurchaseDao;
import br.edu.femass.dao.SupplierDao;
import br.edu.femass.model.*;
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

public class PurchaseController implements Initializable {
    private final PurchaseDao purchaseDao = new PurchaseDao();
    private final ProductDao productDao = new ProductDao();
    private final SupplierDao supplierDao = new SupplierDao();

    private List<PurchaseDetail> purchaseDetailList = new ArrayList<>();

    private BigDecimal total = new BigDecimal("0");

    @FXML
    private Button BtnAddProduct;

    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnDeleteItems;

    @FXML
    private Button BtnDeleteProductSelected;

    @FXML
    private Button BtnIncludeItems;

    @FXML
    private Button BtnSave;

    @FXML
    private ComboBox<Supplier> CbSupplier;

    @FXML
    private ListView<Purchase> LstItems;

    @FXML
    private ListView<PurchaseDetail> LstProductsSelected;

    @FXML
    private ListView<Product> LstProducts;

    @FXML
    private TextField TxtDate;

    @FXML
    private TextField TxtId;

    @FXML
    private TextField TxtPrice;

    @FXML
    private TextField TxtProduct;

    @FXML
    private TextField TxtQuantity;

    @FXML
    private Label TxtTotal;

    private void cleanScreen() {
        TxtId.setText("");
        TxtDate.setText("");
        CbSupplier.setValue(null);
        LstProductsSelected.setItems(null);
        TxtProduct.setText("");
        TxtPrice.setText("");
        TxtQuantity.setText("");
        TxtTotal.setText("");
        purchaseDetailList = new ArrayList<>();
        total = new BigDecimal("0.00");
    }

    private void enableInterface(Boolean include) {
        CbSupplier.setDisable(!include);
        TxtQuantity.setDisable(!include);
        LstProducts.setDisable(!include);
        BtnAddProduct.setDisable(!include);
        BtnSave.setDisable(!include);
        BtnCancel.setDisable(!include);
        BtnDeleteProductSelected.setDisable(!include);
        BtnIncludeItems.setDisable(include);
        BtnDeleteItems.setDisable(include);
        LstItems.setDisable(include);
    }

    private void displayItem() {
        Purchase purchase = LstItems.getSelectionModel().getSelectedItem();
        if(purchase == null) return;
        TxtId.setText(purchase.getId().toString());
        TxtDate.setText(purchase.getDate().toString());
        CbSupplier.setValue(purchase.getSupplier());
        TxtTotal.setText("R$" + purchase.getTotal().toString());

        List<PurchaseDetail> purchaseDetailList = purchase.getPurchaseDetailList();

        ObservableList<PurchaseDetail> purchaseDetailListOb = FXCollections.observableArrayList(purchaseDetailList);
        LstProductsSelected.setItems(purchaseDetailListOb);
    }

    private void displayProduct() {
        Product product = LstProducts.getSelectionModel().getSelectedItem();
        if(product == null) return;
        TxtProduct.setText(product.getName());
        TxtPrice.setText("R$" + product.getPurchasePrice().toString());
    }

    private void refreshList() {
        List<Purchase> purchases;

        try {
            purchases = purchaseDao.retrieve();
        } catch (Exception e) {
            purchases = new ArrayList<>();
        }

        ObservableList<Purchase> purchasesOb = FXCollections.observableArrayList(purchases);
        LstItems.setItems(null);
        LstItems.setItems(purchasesOb);
    }

    private void displayAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setResizable(true);
        errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        errorAlert.setContentText(message);
        errorAlert.show();
    }

    private void refreshProductSelected() {
        ObservableList<PurchaseDetail> purchaseDetailListOb = FXCollections.observableArrayList(purchaseDetailList);
        LstProductsSelected.setItems(purchaseDetailListOb);

        TxtTotal.setText("R$" + total.toString());
    }

    @FXML
    void BtnCancel_Action(ActionEvent event) {
        enableInterface(false);
        cleanScreen();
    }

    @FXML
    void BtnDelete_Action(ActionEvent event) {
        Purchase purchase = LstItems.getSelectionModel().getSelectedItem();
        if(purchase == null) return;
        try {
            purchaseDao.delete(purchase);
        } catch (Exception e) {
            e.printStackTrace();
            displayAlert(e.getMessage());
        }
        cleanScreen();
        refreshList();
    }

    @FXML
    void BtnSave_Action(ActionEvent event) {
        Purchase purchase = new Purchase();
        purchase.setSupplier(CbSupplier.getSelectionModel().getSelectedItem());
        purchase.setPurchaseDetailList(purchaseDetailList);
        purchase.setTotal(total);

        if (CbSupplier.getValue() == null || purchase.getPurchaseDetailList().isEmpty()) {
            displayAlert("Todos os campos devem ser preenchidos");
        } else {
            try {
                purchaseDao.create(purchase);
            } catch (Exception e) {
                e.printStackTrace();
                displayAlert(e.getMessage());
            }
            enableInterface(false);
            refreshList();
        }
    }

    @FXML
    void BtnInclude_Action(ActionEvent event) {
        refreshList();
        enableInterface(true);
        cleanScreen();
        CbSupplier.requestFocus();
    }

    @FXML
    void BtnDeleteProductSelected_Action(ActionEvent event) {
        purchaseDetailList.remove(LstProductsSelected.getSelectionModel().getSelectedItem());
        refreshProductSelected();
    }

    @FXML
    void BtnAddProduct_Action(ActionEvent event) {
        PurchaseDetail purchaseDetail = new PurchaseDetail();
        Purchase purchase = new Purchase();
        purchase.setSupplier(CbSupplier.getSelectionModel().getSelectedItem());

        purchaseDetail.setPurchase(purchase);
        purchaseDetail.setProduct(LstProducts.getSelectionModel().getSelectedItem());
        purchaseDetail.setQuantity(Integer.parseInt(TxtQuantity.getText()));
        purchaseDetail.setPrice(LstProducts.getSelectionModel().getSelectedItem().getPurchasePrice());
        purchaseDetailList.add(purchaseDetail);

        BigDecimal bQuantity = new BigDecimal(purchaseDetail.getQuantity());
        BigDecimal bResult = bQuantity.multiply(purchaseDetail.getPrice());
        total = total.add(bResult);

        refreshProductSelected();
    }

    @FXML
    void LstItems_KeyPressed(KeyEvent event) {
        displayItem();
    }

    @FXML
    void LstItems_MouseClicked(MouseEvent event) {
        displayItem();
    }

    @FXML
    void LstProducts_KeyPressed(KeyEvent event) {
        displayProduct();
    }

    @FXML
    void LstProducts_MouseClicked(MouseEvent event) {
        displayProduct();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Product> products;
        List<Supplier> suppliers;

        try {
            products = productDao.retrieve();
            suppliers = supplierDao.retrieve();
        } catch (Exception e) {
            products = new ArrayList<>();
            suppliers = new ArrayList<>();
        }

        ObservableList<Product> productOb = FXCollections.observableArrayList(products);
        LstProducts.setItems(productOb);

        ObservableList<Supplier> supplierOb = FXCollections.observableArrayList(suppliers);
        CbSupplier.setItems(supplierOb);

        refreshList();
    }
}
