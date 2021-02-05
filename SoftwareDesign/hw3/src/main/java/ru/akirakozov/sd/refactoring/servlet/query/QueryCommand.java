package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.SQLiteProductTableManager;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class QueryCommand {
  protected SQLiteProductTableManager productTableManager;

  public QueryCommand(SQLiteProductTableManager productTableManager) {
    this.productTableManager = productTableManager;
  }

  protected abstract String getQueryResponse();

  public void writeResponse(HttpServletResponse response) throws IOException {
    response.getWriter().print(getQueryResponse());
  }
}
