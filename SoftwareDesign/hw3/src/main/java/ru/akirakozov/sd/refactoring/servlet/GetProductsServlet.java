package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.HttpBodyResponseBuilder;
import ru.akirakozov.sd.refactoring.SQLiteProductTableManager;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends ProductServlet {

  public GetProductsServlet(SQLiteProductTableManager productTableManager) {
    super(productTableManager);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Product> allProducts = productTableManager.selectAllProducts();
    HttpBodyResponseBuilder responseBuilder = new HttpBodyResponseBuilder();
    responseBuilder.appendProductList(allProducts);
    response.getWriter().print(responseBuilder.toString());
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
  }
}
