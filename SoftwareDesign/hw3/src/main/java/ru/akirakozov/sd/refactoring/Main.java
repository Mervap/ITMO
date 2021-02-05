package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.query.QueryServlet;

/**
 * @author akirakozov
 */
public class Main {
  private static final String PRODUCT_TABLE_NAME = "PRODUCT";

  public static void main(String[] args) throws Exception {
    SQLiteProductTableManager productTableManager = new SQLiteProductTableManager(PRODUCT_TABLE_NAME);
    productTableManager.createProductTableIfNotExists();

    ServerManager serverManager = new ServerManager(8081);
    serverManager.addServlet(new AddProductServlet(productTableManager), "/add-product");
    serverManager.addServlet(new GetProductsServlet(productTableManager), "/get-products");
    serverManager.addServlet(new QueryServlet(productTableManager), "/query");
    serverManager.getServer().join();
  }
}
