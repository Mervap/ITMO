package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.HttpBodyResponseBuilder;
import ru.akirakozov.sd.refactoring.SQLiteProductTableManager;

public class CountQueryCommand extends QueryCommand {
  public CountQueryCommand(SQLiteProductTableManager productTableManager) {
    super(productTableManager);
  }

  @Override
  protected String getQueryResponse() {
    HttpBodyResponseBuilder responseBuilder = new HttpBodyResponseBuilder();
    responseBuilder.appendRow("Number of products: ");
    productTableManager.executeQueryStatement("SELECT COUNT(*) FROM " + productTableManager.getTableName(), resultSet -> {
      try {
        if (resultSet.next()) {
          responseBuilder.appendRow(resultSet.getInt(1));
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return responseBuilder.toString();
  }
}
