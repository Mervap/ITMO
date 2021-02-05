package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.SQLiteProductTableManager;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends ProductServlet {

  public AddProductServlet(SQLiteProductTableManager productTableManager) {
    super(productTableManager);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter("name");
    int price = Integer.parseInt(request.getParameter("price"));
    productTableManager.insertProduct(new Product(name, price));
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().println("OK");
  }
}
