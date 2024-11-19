package com.example;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class StorePublicKey {
    public static void main(String[] args) throws Exception {
        // Generate Key Pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(4096);
        KeyPair keyPair = keyGen.generateKeyPair();
        // Generate a Self-Signed Certificate
        X509Certificate cert = generateSelfSignedCertificate(keyPair);
        System.out.println(cert);

        // Create and load the keystore
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = "keystorepassword".toCharArray();
        keyStore.load(null, password);

        // Store the public key in the keystore
        keyStore.setCertificateEntry("publicKeyAlias", cert);

        // Save the keystore to a file
        try (FileOutputStream fos = new FileOutputStream("newPublicKeyStore.PKCS12")) {
            keyStore.store(fos, password);
        }
    }

    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        X500Name issuer = new X500Name("CN=Test CA Certificate");
        BigInteger serial = new BigInteger(64, new SecureRandom());
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + 365 * 24 * 60 * 60 * 1000L);

        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
            issuer, serial, notBefore, notAfter, issuer, keyPair.getPublic());

        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
            .build(keyPair.getPrivate());

        X509CertificateHolder certHolder = certBuilder.build(signer);
        return new JcaX509CertificateConverter().setProvider("BC")
            .getCertificate(certHolder);
    }
}
