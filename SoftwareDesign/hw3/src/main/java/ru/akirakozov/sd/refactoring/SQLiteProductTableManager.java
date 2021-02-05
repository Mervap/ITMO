package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SQLiteProductTableManager {
  private final Connection databaseConnection;
  private final String tableName;

  public SQLiteProductTableManager(String tableName) throws SQLException {
    this.databaseConnection = DriverManager.getConnection("jdbc:sqlite:test.db");
    this.tableName = tableName;
  }

  public void createProductTableIfNotExists() {
    executeUpdateStatement(
        "CREATE TABLE IF NOT EXISTS " + tableName +
            "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            " NAME           TEXT    NOT NULL, " +
            " PRICE          INT     NOT NULL)");
  }

  public void dropProductTable() {
    executeUpdateStatement("DROP TABLE " + tableName);
  }

  public void insertProduct(Product product) {
    String sqlQuery = "INSERT INTO " + tableName + " (NAME, PRICE) " +
        "VALUES ('" + product.getName() + "'," + product.getPrice() + ")";
    executeUpdateStatement(sqlQuery);
  }

  public List<Product> selectAllProducts() {
    return selectProducts("");
  }

  public List<Product> selectProducts(String queryTail) {
    List<Product> products = new ArrayList<>();
    executeQueryStatement("SELECT * FROM " + tableName + " " + queryTail, resultSet -> {
      try {
        while (resultSet.next()) {
          String name = resultSet.getString("name");
          int price = resultSet.getInt("price");
          products.add(new Product(name, price));
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return products;
  }

  public String getTableName() {
    return tableName;
  }

  public void executeQueryStatement(String sqlQuery, Consumer<ResultSet> consumer) {
    try {
      Statement queryStatement = databaseConnection.createStatement();
      ResultSet resultSet = queryStatement.executeQuery(sqlQuery);
      consumer.accept(resultSet);
      resultSet.close();
      queryStatement.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void executeUpdateStatement(String sqlQuery) {
    try {
      Statement updateStatement = databaseConnection.createStatement();
      updateStatement.executeUpdate(sqlQuery);
      updateStatement.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
