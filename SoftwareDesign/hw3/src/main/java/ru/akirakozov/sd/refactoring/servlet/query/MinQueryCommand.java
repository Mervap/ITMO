package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.HttpBodyResponseBuilder;
import ru.akirakozov.sd.refactoring.SQLiteProductTableManager;
import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;

public class MinQueryCommand extends QueryCommand {
  public MinQueryCommand(SQLiteProductTableManager productTableManager) {
    super(productTableManager);
  }

  @Override
  protected String getQueryResponse() {
    HttpBodyResponseBuilder responseBuilder = new HttpBodyResponseBuilder();
    List<Product> minProduct = productTableManager.selectProducts("ORDER BY PRICE LIMIT 1");
    responseBuilder.appendH1("Product with min price: ");
    responseBuilder.appendProductList(minProduct);
    return responseBuilder.toString();
  }
}
