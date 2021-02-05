package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.HttpBodyResponseBuilder;
import ru.akirakozov.sd.refactoring.SQLiteProductTableManager;
import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;

public class MaxQueryCommand extends QueryCommand {
  public MaxQueryCommand(SQLiteProductTableManager productTableManager) {
    super(productTableManager);
  }

  @Override
  protected String getQueryResponse() {
    HttpBodyResponseBuilder responseBuilder = new HttpBodyResponseBuilder();
    List<Product> maxProduct = productTableManager.selectProducts("ORDER BY PRICE DESC LIMIT 1");
    responseBuilder.appendH1("Product with max price: ");
    responseBuilder.appendProductList(maxProduct);
    return responseBuilder.toString();
  }
}
