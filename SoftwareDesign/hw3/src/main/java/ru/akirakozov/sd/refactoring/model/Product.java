package ru.akirakozov.sd.refactoring.model;

public class Product {
  private String name;
  private int price;

  public Product(String name, int price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  @Override
  public int hashCode() {
    return 31 * name.hashCode() + price;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Product)) return false;
    Product other = (Product) obj;
    return name.equals(other.name) && price == other.price;
  }

  @Override
  public String toString() {
    return "Product(name='" + name + "', price=" + price + ")";
  }
}
