package com.tiagocampos.configparser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Section {
  private String name;
  private Map<String, Property> properties = new HashMap<>();

  public Section(String name, List<Property> properties) {
    this.name = name;

    for (Property prop : properties) {
      this.properties.put(prop.name(), prop);
    }
  }

  public String get(Property property) {
    return get(property.name());
  }

  public String get(String propertyName) {
    boolean propertyExists = properties.keySet().contains(propertyName);
    if (!propertyExists) {
      throw new IllegalStateException("Property " + propertyName + " not found in section " + name);
    }

    return properties.get(propertyName).value();
  }

  public String name() {
    return this.name;
  }

  public void updateProperty(String propertyName, String newValue) {
    Property newProp = new Property(propertyName, newValue);
    this.properties.put(propertyName, newProp);
  }

  public Collection<Property> properties() {
    return this.properties.values();
  }
}
