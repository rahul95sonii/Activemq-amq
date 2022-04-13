package com.simple.activemq.util;



import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public class PathCert {

    public static void main(String... args)  {
        FileInputStream is = null;
        try{
     //   File file = new File("");
       // InputStream is = new FileInputStream(file);
            CertificateFactory fac = CertificateFactory.getInstance("X509");
            is = new FileInputStream("\\path\\to\\file\\cert.crt");
            X509Certificate cert = (X509Certificate) fac.generateCertificate(is);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        String password = "wm6w5SS";
        keystore.load(is, password.toCharArray());


        Enumeration<String> enumeration = keystore.aliases();
        while(enumeration.hasMoreElements()) {
            String alias = enumeration.nextElement();
            System.out.println("alias name: " + alias);
            Certificate certificate = keystore.getCertificate(alias);
            System.out.println(certificate.toString());

        }

    } catch (java.security.cert.CertificateException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }finally {

            if(null != is)
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }/*
        try{
        CertificateFactory fac = CertificateFactory.getInstance("X509");
        FileInputStream is = new FileInputStream("\\path\\to\\file\\cert.crt");
        X509Certificate cert = (X509Certificate) fac.generateCertificate(is);
System.out.println(cert.getPublicKey());

        System.out.println("From: " + cert.getNotBefore());
        System.out.println("Until: " + cert.getNotAfter());
    }
    catch(CertificateException se){
            se.printStackTrace();
        }
        catch (FileNotFoundException se){
            se.printStackTrace();
        }

    }
*/
}}
