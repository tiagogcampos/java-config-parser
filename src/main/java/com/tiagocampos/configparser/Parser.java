package com.tiagocampos.configparser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
  private static List<Section> sections = new ArrayList<>();
  private static Pattern sectionPattern = Pattern.compile("^\\[.*\\]$");
  private static Pattern sectionNamePattern = Pattern.compile("^\\[(.*?)\\]$");

  private static int currentLine = 0;

  public static Config parse(List<String> lines) {
    int numberOfLines = lines.size();
    while (currentLine < numberOfLines) {
      String line = lines.get(currentLine).trim();
      if (line.length() == 0 || line.startsWith("#") || line.startsWith("//")) {
        currentLine++;
        continue;
      }

      boolean isSection = isSection(line);

      if (!isSection) {
        throw new IllegalStateException("Cannot declare property outside of section");
      } else {
        String sectionName = getSectionName(line);
        currentLine++;
        List<Property> properties = parseSectionProperties(lines);

        Section section = new Section(sectionName, properties);
        sections.add(section);
      }
    }

    Interpolator interpolator = new Interpolator(sections);
    interpolator.interpolate();

    currentLine = 0;

    return new Config(sections);
  }

  private static String getSectionName(String line) {
    Matcher matcher = sectionNamePattern.matcher(line);
    if (!matcher.find()) {
      throw new IllegalStateException("Could not parse section name from line " + line);
    }

    return matcher.group(1);
  }

  private static boolean isSection(String line) {
    return sectionPattern.matcher(line).matches();
  }

  private static List<Property> parseSectionProperties(List<String> contents) {
    List<Property> properties = new ArrayList<>();
    for (; currentLine < contents.size() && !isSection(contents.get(currentLine)); currentLine++) {
      String lineContent = contents.get(currentLine).trim();

      if (lineContent.length() == 0
          || lineContent.startsWith("#")
          || lineContent.startsWith("//")) {
        continue;
      }

      String[] propertyKV = lineContent.split("=");

      if (propertyKV.length == 0) {
        throw new IllegalStateException("Property " + lineContent + " is invalid");
      }

      String propertyName = propertyKV[0].trim();

      String propertyValue;
      try {
        propertyValue = propertyKV[1].trim();
      } catch (IndexOutOfBoundsException | NullPointerException exc) {
        propertyValue = null;
      }

      if (propertyName == "") {
        throw new IllegalStateException("Property name cannot be empty");
      }

      Property property = new Property(propertyName, propertyValue);

      properties.add(property);
    }

    return properties;
  }
}
