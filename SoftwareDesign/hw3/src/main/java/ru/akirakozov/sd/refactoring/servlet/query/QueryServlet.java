package ru.akirakozov.sd.refactoring.servlet.query;

import ru.akirakozov.sd.refactoring.SQLiteProductTableManager;
import ru.akirakozov.sd.refactoring.servlet.ProductServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends ProductServlet {
  public QueryServlet(SQLiteProductTableManager productTableManager) {
    super(productTableManager);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String command = request.getParameter("command");
    QueryCommand queryCommand = getQueryCommandByName(command);
    if (queryCommand == null) {
      response.getWriter().println("Unknown command: " + command);
    } else {
      queryCommand.writeResponse(response);
    }
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
  }

  public QueryCommand getQueryCommandByName(String queryCommandName) {
    switch (queryCommandName) {
      case "max":
        return new MaxQueryCommand(productTableManager);
      case "min":
        return new MinQueryCommand(productTableManager);
      case "sum":
        return new SumQueryCommand(productTableManager);
      case "count":
        return new CountQueryCommand(productTableManager);
      default:
        return null;
    }
  }
}
