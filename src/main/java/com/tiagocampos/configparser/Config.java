package com.tiagocampos.configparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
  private Map<String, Section> sections = new HashMap<>();

  public Config(List<Section> sections) {
    for (Section section : sections) {
      this.sections.put(section.name(), section);
    }
  }

  public static Config read(String filePath) throws IOException {

    List<String> lines;

    lines = Files.readAllLines(Paths.get(filePath));

    Config config = Parser.parse(lines);

    return config;
  }

  public Collection<Section> sections() {
    return this.sections.values();
  }

  public String get(String section, String property) {
    return getProperty(section, property);
  }

  public Integer getInt(String section, String property) {
    String val = getProperty(section, property);

    try {
      return Integer.valueOf(val);
    } catch (NumberFormatException exc) {
      return null;
    }
  }

  public Long getLong(String section, String property) {
    String val = getProperty(section, property);
    try {
      return Long.valueOf(val);
    } catch (NumberFormatException exc) {
      return null;
    }
  }

  public Double getDouble(String section, String property) {
    String val = getProperty(section, property);
    try {
      return Double.valueOf(val);
    } catch (NumberFormatException exc) {
      return null;
    }
  }

  public Float getFloat(String section, String property) {
    String val = getProperty(section, property);
    try {
      return Float.valueOf(val);
    } catch (NumberFormatException exc) {
      return null;
    }
  }

  public Boolean getBoolean(String section, String property) {
    String val = getProperty(section, property);
    try {
      return Boolean.valueOf(val);
    } catch (Exception exc) {
      return null;
    }
  }

  public void printAllProperties() {
    for (Section section : sections.values()) {
      Collection<Property> props = section.properties();

      System.out.println("------------");
      System.out.println("Section: " + section.name());

      for (Property prop : props) {
        System.out.print("\t" + prop.name());
        System.out.println("-> \t" + prop.value());
      }

      System.out.println("------------");
    }
  }

  public String getProperty(String section, String property) {
    boolean sectionExists = sections.keySet().contains(section);

    if (!sectionExists) {
      throw new IllegalStateException("Section " + section + " not found");
    }

    return sections.get(section).get(property);
  }
}
