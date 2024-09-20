package com.tiagocampos.configparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpolator {
  private Pattern pattern = Pattern.compile("\\$\\{([^\\}]+)\\}");

  private Map<String, Section> sections;

  public Interpolator(Collection<Section> sections) {
    this.sections = new HashMap<>();
    for (Section s : sections) {
      this.sections.put(s.name(), s);
    }
  }

  private List<String> getPropertiesToInterpolate(String propertyValue) {
    Matcher m = pattern.matcher(propertyValue);

    List<String> matches = new ArrayList<>();

    while (m.find()) {
      String property = m.group(1);
      matches.add(property);
    }

    return matches;
  }

  private String interpolateProperty(String section, String property) {
    String propertyValue = sections.get(section).get(property);
    if (propertyValue == null) return "";
    List<String> propertiesToInterpolate = getPropertiesToInterpolate(propertyValue);
    boolean isDefined = propertiesToInterpolate.size() == 0;

    System.out.println("-------------");
    System.out.println(section + ":" + property);
    System.out.println(propertyValue);
    System.out.println(isDefined);
    System.out.println(propertiesToInterpolate);
    System.out.println("-------------");

    if (!isDefined) {
      for (String prop : propertiesToInterpolate) {
        String[] nameValueKV = prop.split(":");
        String sectionName;
        String propertyName;

        if (nameValueKV.length < 2) {
          sectionName = section;
          propertyName = nameValueKV[0];
        } else {
          sectionName = nameValueKV[0];
          propertyName = nameValueKV[1];
        }

        String r = interpolateProperty(sectionName, propertyName);
        propertyValue = propertyValue.replace("${" + prop + "}", r);
      }
    }
    return propertyValue;
  }

  public void interpolate() {
    for (Section section : sections.values()) {
      Collection<Property> properties = section.properties();
      for (Property property : properties) {
        String result = interpolateProperty(section.name(), property.name());
        section.updateProperty(property.name(), result);
      }
    }
  }
}
