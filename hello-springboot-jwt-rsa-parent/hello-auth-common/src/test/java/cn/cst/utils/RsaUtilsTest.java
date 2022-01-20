package cn.cst.utils;

import org.junit.jupiter.api.Test;

class RsaUtilsTest {

  private final String privateFilePath =
      "E:\\backend-projects\\fun-learning-application\\"
          + "hello-springboot-jwt-rsa-parent\\hello-common\\src\\test\\resources\\id_key_rsa";
  private final String publicFilePath =
      "E:\\backend-projects\\fun-learning-application\\"
          + "hello-springboot-jwt-rsa-parent\\hello-common\\src\\test\\resources\\id_key_rsa.pub";

  @Test
  void getPublicKey() throws Exception {
    RsaUtils.generateKey(publicFilePath, privateFilePath, "salt", 2048);
  }

  @Test
  void getPrivateKey() throws Exception {
    System.out.println(RsaUtils.getPrivateKey(privateFilePath));
  }

  @Test
  void generateKey() throws Exception {
    System.out.println(RsaUtils.getPublicKey(publicFilePath));
  }
}
