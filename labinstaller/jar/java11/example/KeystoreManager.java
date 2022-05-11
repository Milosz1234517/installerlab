package example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeystoreManager {

    public KeyStore ks;

    public void loadKeystore(String name, String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{
    	ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(name), password.toCharArray());
    }

    public void createKeystore(String name, String password){
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] pwdArray = password.toCharArray();
            ks.load(null, pwdArray);

            FileOutputStream fos = new FileOutputStream(name + ".jks");
            ks.store(fos, pwdArray);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
