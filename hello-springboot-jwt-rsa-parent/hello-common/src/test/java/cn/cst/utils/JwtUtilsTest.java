package cn.cst.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

class JwtUtilsTest {
    private final String privateFilePath =
            "E:\\backend-projects\\fun-learning-application\\" +
                    "hello-springboot-jwt-rsa-parent\\hello-common\\src\\test\\resources\\id_key_rsa";
    private final String publicFilePath = "E:\\backend-projects\\fun-learning-application\\" +
            "hello-springboot-jwt-rsa-parent\\hello-common\\src\\test\\resources\\id_key_rsa.pub";

    @Test
    void generateTokenExpireInMinutes() throws Exception {
        PrivateKey privateKey = RsaUtils.getPrivateKey(privateFilePath); //私钥加密
        PublicKey publicKey = RsaUtils.getPublicKey(publicFilePath); //公钥解密

        User chen_shaotong = User.builder().username("CST").age("34").build();
        String token = JwtUtils.generateTokenExpireInSeconds(chen_shaotong, privateKey, 5);
        System.out.println(token);

        Assertions.assertThrows(Exception.class, () -> {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(2000);
                Payload<User> fromToken = JwtUtils.getInfoFromToken(token, publicKey, User.class);
                System.out.println(fromToken);
            }
        });
    }


}