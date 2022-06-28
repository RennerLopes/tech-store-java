package br.edu.femass.controller;

import br.edu.femass.dao.ProductDao;
import br.edu.femass.dao.SaleDao;
import br.edu.femass.dao.ClientDao;
import br.edu.femass.model.Product;
import br.edu.femass.model.Sale;
import br.edu.femass.model.SaleDetail;
import br.edu.femass.model.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SaleController implements Initializable {
    private final SaleDao saleDao = new SaleDao();
    private final ProductDao productDao = new ProductDao();
    private final ClientDao clientDao = new ClientDao();

    private List<SaleDetail> saleDetailList = new ArrayList<>();

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
    private ComboBox<Client> CbClient;

    @FXML
    private ListView<Sale> LstItems;

    @FXML
    private ListView<SaleDetail> LstProductsSelected;

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
        CbClient.setValue(null);
        LstProductsSelected.setItems(null);
        TxtProduct.setText("");
        TxtPrice.setText("");
        TxtQuantity.setText("");
        TxtTotal.setText("");
        saleDetailList = new ArrayList<>();
        total = new BigDecimal("0.00");
    }

    private void enableInterface(Boolean include) {
        CbClient.setDisable(!include);
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
        Sale sale = LstItems.getSelectionModel().getSelectedItem();
        if(sale == null) return;
        TxtId.setText(sale.getId().toString());
        TxtDate.setText(sale.getDate().toString());
        CbClient.setValue(sale.getClient());
        TxtTotal.setText("R$" + sale.getTotal().toString());

        List<SaleDetail> saleDetailList = sale.getSaleDetailList();

        ObservableList<SaleDetail> saleDetailListOb = FXCollections.observableArrayList(saleDetailList);
        LstProductsSelected.setItems(saleDetailListOb);
    }

    private void displayProduct() {
        Product product = LstProducts.getSelectionModel().getSelectedItem();
        if(product == null) return;
        TxtProduct.setText(product.getName());
        TxtPrice.setText("R$" + product.getSalePrice().toString());
    }

    private void refreshList() {
        List<Sale> sales;

        try {
            sales = saleDao.retrieve();
        } catch (Exception e) {
            sales = new ArrayList<>();
        }
        ObservableList<Sale> salesOb = FXCollections.observableArrayList(sales);
        LstItems.setItems(null);
        LstItems.setItems(salesOb);
    }

    private void refreshProductSelected() {
        ObservableList<SaleDetail> saleDetailListOb = FXCollections.observableArrayList(saleDetailList);
        LstProductsSelected.setItems(saleDetailListOb);

        TxtTotal.setText("R$" + total.toString());
    }

    @FXML
    void BtnCancel_Action(ActionEvent event) {
        enableInterface(false);
        cleanScreen();
    }

    @FXML
    void BtnDelete_Action(ActionEvent event) {
        Sale sale = LstItems.getSelectionModel().getSelectedItem();
        if(sale == null) return;
        try {
            saleDao.delete(sale);
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshList();
    }

    @FXML
    void BtnSave_Action(ActionEvent event) {
        Sale sale = new Sale();
        sale.setClient(CbClient.getSelectionModel().getSelectedItem());
        sale.setSaleDetailList(saleDetailList);
        sale.setTotal(total);

        if (CbClient.getValue().equals(new Client()) || sale.getSaleDetailList().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.show();
        } else {
            try {
                saleDao.create(sale);
            } catch (Exception e) {
                e.printStackTrace();
            }
            enableInterface(false);
            refreshList();
        }
    }

    @FXML
    void BtnInclude_Action(ActionEvent event) {
        //refreshList();
        enableInterface(true);
        cleanScreen();
        CbClient.requestFocus();
    }

    @FXML
    void BtnDeleteProductSelected_Action(ActionEvent event) {
        saleDetailList.remove(LstProductsSelected.getSelectionModel().getSelectedItem());
        refreshProductSelected();
    }

    @FXML
    void BtnAddProduct_Action(ActionEvent event) {
        SaleDetail saleDetail = new SaleDetail();
        Sale sale = new Sale();
        sale.setClient(CbClient.getSelectionModel().getSelectedItem());

        saleDetail.setSale(sale);
        saleDetail.setProduct(LstProducts.getSelectionModel().getSelectedItem());
        saleDetail.setQuantity(Integer.parseInt(TxtQuantity.getText()));
        saleDetail.setPrice(LstProducts.getSelectionModel().getSelectedItem().getSalePrice());

        if (saleDetail.getQuantity() > saleDetail.getProduct().getStock()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Quantidade informada do item maior que a quantidade em estoque");
            errorAlert.show();
        } else {
            saleDetailList.add(saleDetail);

            BigDecimal bQuantity = new BigDecimal(saleDetail.getQuantity());
            BigDecimal bResult = bQuantity.multiply(saleDetail.getPrice());
            total = total.add(bResult);

            refreshProductSelected();
        }
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
        List<Client> clients;

        try {
            products = productDao.retrieve();
            clients = clientDao.retrieve();
        } catch (Exception e) {
            products = new ArrayList<>();
            clients = new ArrayList<>();
        }

        ObservableList<Product> productOb = FXCollections.observableArrayList(products);
        LstProducts.setItems(productOb);

        ObservableList<Client> clientOb = FXCollections.observableArrayList(clients);
        CbClient.setItems(clientOb);

        refreshList();
    }
}
