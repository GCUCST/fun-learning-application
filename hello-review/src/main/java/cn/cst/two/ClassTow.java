package cn.cst.two;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

public class ClassTow {

    public static void main(String[] args) {
        String str1 = "abc";
        String str2 = "abc";
        System.out.println(str1 == str2);
        System.out.println(str1.equals(str2));
        System.out.println(hashCodeStr.apply("chenst1"));
    }

    public static Function<String, String> hashCodeStr =
            new Function<String, String>() {
                @Override
                public String apply(String str) {
                    String cacheKey;
                    try {
                        final MessageDigest mDigest = MessageDigest.getInstance("MD5");
                        mDigest.update(str.getBytes());
                        byte[] bytes = mDigest.digest(); // 转为bytes数组  如果将文件转为byte数组  也可以获取到该文件的hash值
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < bytes.length; i++) {
                            String hex = Integer.toHexString(0xFF & bytes[i]); // 进制转化
                            if (hex.length() == 1) {
                                sb.append('0');
                            }
                            sb.append(hex);
                        }
                        cacheKey = sb.toString();
                    } catch (NoSuchAlgorithmException e) {
                        cacheKey = String.valueOf(str.hashCode());
                    }
                    return cacheKey;
                }
            };
}
