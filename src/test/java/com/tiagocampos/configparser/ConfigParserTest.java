package com.tiagocampos.configparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigParserTest {

  @Test
  public void testParser() {
    List<String> lines;

    try {
      lines = Files.readAllLines(Paths.get("./src/test/resources/config.properties"));
    } catch (IOException exc) {
      System.err.println(exc);
      return;
    }

    final Config config = Parser.parse(lines);

    config.printAllProperties();

    String string = config.get("test_section_one", "name");
    Boolean bool = config.getBoolean("test_section_one", "boolean");

    Assertions.assertEquals(string, "test");

    Assertions.assertEquals(bool, true);

    Integer integer = config.getInt("test_section_two", "int");

    String stringTwo = config.get("test_section_two", "string");

    Float flt = config.getFloat("test_section_two", "float");

    Assertions.assertEquals(integer, 10);

    Assertions.assertEquals(flt, 10.0f);

    Assertions.assertEquals(stringTwo, "test_string");

    Assertions.assertThrows(
        IllegalStateException.class,
        () -> {
          config.get("test_section_one", "non_existing_property");
        });
  }
}
