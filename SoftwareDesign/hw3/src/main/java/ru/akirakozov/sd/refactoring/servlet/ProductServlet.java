package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.SQLiteProductTableManager;

import javax.servlet.http.HttpServlet;

public abstract class ProductServlet extends HttpServlet {
  protected final SQLiteProductTableManager productTableManager;

  public ProductServlet(SQLiteProductTableManager productTableManager) {
    this.productTableManager = productTableManager;
  }
}
