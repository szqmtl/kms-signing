package rshu.example.jwtUtil;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyUtils {
    private static String PrivateKeyFile = "privateKey.pem";
    private static String PublicKeyFile = "publicKey.pem";

    public static String getPrivateKeyFile() {
        return PrivateKeyFile;
    }

    public static String getPublicKeyFile() {
        return PublicKeyFile;
    }

    public static KeyPair getRsaKeyPair() {
        try {
            KeyPair kp = null;
            PublicKey publicKey = null;
            PrivateKey privateKey = null;
            try {
                publicKey = PemUtils.readPublicKeyFromFile(PublicKeyFile, "RSA");
                privateKey = PemUtils.readPrivateKeyFromFile(PrivateKeyFile, "RSA");
            }catch (FileNotFoundException fe){}
            if (publicKey == null || privateKey == null) {
                kp = Keys.keyPairFor(SignatureAlgorithm.RS256);
                PemUtils.writePublicKeyToFile(PublicKeyFile, kp.getPublic());
                PemUtils.writePrivateKeyToFile(PrivateKeyFile, kp.getPrivate());
            } else {
                kp = new KeyPair(publicKey, privateKey);
            }
            return kp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
