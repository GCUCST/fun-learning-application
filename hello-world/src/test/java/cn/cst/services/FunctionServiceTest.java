package cn.cst.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FunctionServiceTest {

  @Test
  void testGetEntity() {
    Assertions.assertDoesNotThrow(
        () -> {
          FunctionService service = new FunctionService();
          service.getEntity();
        });
  }
}
