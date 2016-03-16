package com.yitong.appsdk.core.encrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * AES加密工具类
 * Created by zhuzengpeng on 2016/2/26.
 */
public class AESHelper {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     * @method encrypt
     * @param data   需要加密的内容
     * @param password  加密密码
     * @return
     * @throws
     * @since v1.0
     */
    public static byte[] encrypt(String data, String password){
        try {
            SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), KEY_ALGORITHM);
            // 实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            // 执行操作
            return cipher.doFinal(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @method decrypt
     * @param data   待解密内容
     * @param password  解密密钥
     * @return
     * @throws
     * @since v1.0
     */
    public static byte[] decrypt(byte[] data, String password){

        try {
            SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), KEY_ALGORITHM);
            // 实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            // 执行操作
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成一个随机的AESKEY,格式：16位的消息ID+16位UUID
     * @return
     */
    public static String generateAeskey() {
        String msgId = "000" + System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0,16);
        return msgId + uuid;
    }

    /**
     * @method main
     * @param args
     * @throws
     * @since v1.0
     */

    public static void main(String[] args) {
        String content = "{name:'zhangsan', sex:'16', school:'李四'}";
        String password = "20141212112740.27c521a72-0817-45";
        byte[] encryptResult = encrypt(content, password);//加密
        byte[] decryptResult = decrypt(encryptResult,password);//解密
        /*然后，我们再修订以上测试代码*/
        System.out.println("***********************************************");
        String encryptResultStr = Base64.encode(encryptResult);
        System.out.println("先AES然后BASE64加密后：" + encryptResultStr);
        byte[] decryptFrom = Base64.decode(encryptResultStr);
        decryptResult = decrypt(decryptFrom,password);//解码
        System.out.println("解密后：" + new String(decryptResult));
        /****----------**/
        System.out.println("-----***********************************************");
        String eee = "9EYxDjsB56fveZi2cczmHwLBYw0W0By+7Ms6GEfsqzy0yla6wPonUf6qeu7AkO72pve+XrAFjxK5mglZ2Lb9iYorb+cqcEuCDwY+rruOexg=";
        String eeePasswd = "0001457960739325f396b316f8f0464f";
        System.out.println("--------------" + decrypt(Base64.decode(eee), eeePasswd));
    }
}
