package example;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

public class AES {
    Cipher cipher;

    public AES() {
        try{
            cipher = Cipher.getInstance("AES");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String encrypt(String message, SecretKey key){
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String message, SecretKey key){
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(message));
            return new String(plainText);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
