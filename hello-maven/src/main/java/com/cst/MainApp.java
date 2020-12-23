package com.cst;

import cn.cst.pojos.Entity;
import java.util.UUID;

public class MainApp {
  public static void main(String[] args) {
    Entity build = Entity.builder().id(UUID.randomUUID()).object(null).build();
    System.out.println(build);
  }
}
