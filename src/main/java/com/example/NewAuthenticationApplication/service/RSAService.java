package com.example.NewAuthenticationApplication.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NewAuthenticationApplication.repository.RSAKeyRepository;

import javax.crypto.Cipher;

import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.Base64;

@Service
public class RSAService {

    @SuppressWarnings("unused")
    @Autowired
    private RSAKeyRepository rsaKeyRepository;
    
    public PublicKey returnkey() throws KeyException {
        try {
            // Load the keystore from the file
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            try (FileInputStream fis = new FileInputStream("newPublicKeyStore.PKCS12")) {
                keystore.load(fis, "keystorepassword".toCharArray());
            }

            // Retrieve the public key from the certificate
            Certificate cert = keystore.getCertificate("publicKeyAlias");
            PublicKey publicKey = cert.getPublicKey();
            return publicKey;

        } catch (Exception e) {
            throw new KeyException(e.getMessage());
        }
    }

    public String encrypt(String input) throws Exception {
        PublicKey storedKey = returnkey();
        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, storedKey);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}


