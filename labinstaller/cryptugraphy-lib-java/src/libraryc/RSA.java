package libraryc;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;

public class RSA {
    Cipher cipher;
    int maxBytesEncrypt;
    int maxBytesDecrypt;
    
    public String encrypt(String message, RSAPrivateKey privateKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] plainText = message.getBytes();
        int counter = 0;
        ArrayList<Byte> bytes = new ArrayList<Byte>();

        while (counter < plainText.length) {
            byte[] tmp;

            if (counter + maxBytesEncrypt < plainText.length) {
                tmp = new byte[maxBytesEncrypt];
            } else tmp = new byte[plainText.length - counter];

            int loop = plainText.length - counter;

            for (int i = 0; i < loop; i++) {
                if (i == maxBytesEncrypt) break;
                tmp[i] = plainText[counter];
                counter++;
            }

            byte[] cipherText = cipher.doFinal(tmp);

            for (byte b : cipherText) {
                bytes.add(b);
            }
        }

        byte[] b = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            b[i] = bytes.get(i);
        }
        

        return Base64.getEncoder().encodeToString(b);
    }

    public String decrypt(String decryptedMessage, RSAPublicKey publicKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] decode = Base64.getDecoder().decode(decryptedMessage);
        int counter = 0;
        StringBuilder resultDecrypt = new StringBuilder();

        while (counter < decode.length) {

            byte[] tmp = new byte[maxBytesDecrypt];

            for (int i = 0; i < decode.length; i++) {
                if (i == maxBytesDecrypt) break;
                tmp[i] = decode[counter];
                counter++;
            }

            byte[] cipherText = cipher.doFinal(tmp);
            resultDecrypt.append(new String(cipherText));
        }

        return resultDecrypt.toString();
    }

    public RSA(int keySize) {
        try {
            cipher = Cipher.getInstance("RSA");
            maxBytesDecrypt = (int) Math.floor((keySize/8.0));
            maxBytesEncrypt = (int) Math.floor((keySize/8.0)) - 11;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
