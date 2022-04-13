package com.simple.activemq;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.security.*;

public class pathCert {
    public static SSLContext createSSLContext(String resourcePath, String pass) throws NoSuchAlgorithmException{
        SSLContext sslc = SSLContext.getInstance("TLS");
        try {
// initialise the keystore
            final char[] password = pass.toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(pathCert.class.getResourceAsStream(resourcePath), password);
// Setup the key manager factory.
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);
// Setup the trust manager factory.
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);
// sslc = SSLContext.getInstance("TLS");
            sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
// return sslc;
        }catch(Exception se){
            se.printStackTrace();
        }
        return sslc;
    }
   public static void main(String[] args) {
        try {
            SSLContext.setDefault(
                    createSSLContext("/resource_path", "Password"));
        }catch(Exception se){se.printStackTrace();}
    }
}