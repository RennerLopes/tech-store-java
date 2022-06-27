package br.edu.femass.controller;

import br.edu.femass.dao.CashClosingDao;
import br.edu.femass.dao.SaleDao;
import br.edu.femass.model.Purchase;
import br.edu.femass.model.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CashClosingController implements Initializable {
    private final CashClosingDao cashClosingDao = new CashClosingDao();
    private final SaleDao saleDao = new SaleDao();

    @FXML
    private Button BtnCashClose;

    @FXML
    private ListView<Purchase> LstPurchase;

    @FXML
    private ListView<Sale> LstSele;

    @FXML
    private Label TxtTotal;

    @FXML
    void BtnCashClose_Action(ActionEvent event) {
        List<Sale> sales = new ArrayList<>();
        List<Purchase> purchases = new ArrayList<>();
        BigDecimal total = new BigDecimal("0");
        try {
            sales = cashClosingDao.retrieveSalesByDay();
            purchases = cashClosingDao.retrievePurchasesByDay();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ObservableList<Sale> salesOb = FXCollections.observableArrayList(sales);
        LstSele.setItems(salesOb);

        ObservableList<Purchase> purchasesOb = FXCollections.observableArrayList(purchases);
        LstPurchase.setItems(purchasesOb);

        for (Sale s: sales) {
            total = total.add(s.getTotal());
        }

        for (Purchase p: purchases) {
            total = total.subtract(p.getTotal());
        }

        TxtTotal.setText("R$ " + total.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
