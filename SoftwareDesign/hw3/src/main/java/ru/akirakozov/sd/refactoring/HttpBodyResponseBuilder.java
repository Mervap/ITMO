package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;

public class HttpBodyResponseBuilder {
  StringBuilder body = new StringBuilder();

  public HttpBodyResponseBuilder() {
    body.append("<html><body>\n");
  }

  public void appendH1(String text) {
    body.append("<h1>").append(text).append("</h1>\n");
  }

  public void appendProductList(List<Product> products) {
    for (Product product : products) {
      body.append(product.getName()).append("\t").append(product.getPrice())
          .append("</br>").append("\n");
    }
  }

  public void appendRow(String text) {
    body.append(text).append("\n");
  }

  public void appendRow(int text) {
    body.append(text).append("\n");
  }

  @Override
  public String toString() {
    return body.toString() + "</body></html>\n";
  }
}
