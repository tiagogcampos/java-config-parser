package com.tiagocampos.configparser;

public class Property {
  private String name;
  private String value;

  public Property(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String name() {
    return this.name;
  }

  public String value() {
    return this.value;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("<Property name=\"");
    sb.append(name);
    sb.append("\", value=\"");
    sb.append(value);
    sb.append("\">");

    return sb.toString();
  }
}
