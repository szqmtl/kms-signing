package rshu.example;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.SignRequest;
import com.amazonaws.services.kms.model.SignResult;
import com.amazonaws.services.kms.model.SigningAlgorithmSpec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.RsaSignatureValidator;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import rshu.example.jwtUtil.BodyBean;
import rshu.example.jwtUtil.HeaderBean;
import rshu.example.jwtUtil.KeyUtils;
import rshu.example.jwtUtil.PemUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

public class Kms {
    private static String Region = "us-east-1";
    private static String KeyArn = "arn:aws:kms:us-east-1:nnnnnnnnnnnn:key/xxxxxxxx-914c-448c-a2f2-yyyyyyyyyyy";
    private static SigningAlgorithmSpec Alg = SigningAlgorithmSpec.RSASSA_PKCS1_V1_5_SHA_256;

    public static String getToken(HeaderBean header, BodyBean body) throws JsonProcessingException {
        var base64UrlEncoder = Encoders.BASE64URL;
        String headerStr = new ObjectMapper().writeValueAsString(header);
        String encodedHeader = base64UrlEncoder.encode(headerStr.getBytes(StandardCharsets.UTF_8));

        String bodyStr = new ObjectMapper().writeValueAsString(body);
        String encodedBody = base64UrlEncoder.encode(bodyStr.getBytes(StandardCharsets.UTF_8));
        String message = encodedHeader + "." + encodedBody;

        AWSKMS kmsClient = AWSKMSClientBuilder.standard().withRegion(Region).build();

        long startTime = System.currentTimeMillis();
        SignRequest req = new SignRequest().withKeyId(KeyArn).withSigningAlgorithm(Alg).withMessageType("RAW")
                .withMessage(ByteBuffer.wrap(message.getBytes(StandardCharsets.US_ASCII)));
        SignResult result = kmsClient.sign(req);
        long endTime = System.currentTimeMillis();
        System.out.println("sign time: " + (endTime - startTime));
        String signature = base64UrlEncoder.encode(result.getSignature().array());

        return message + "." + signature;
    }

    public static boolean validateToken(String token) throws IOException {
        String[] parts = token.split("\\.");
        var message = parts[0] + "." + parts[1];
        var base64UrlDecoder = Decoders.BASE64URL;
        var signature = base64UrlDecoder.decode(parts[2]);

        PublicKey publicKey = PemUtils.readPublicKeyFromFile(KeyUtils.getPublicKeyFile(), "RSA");
        var signatureValidator = new RsaSignatureValidator(SignatureAlgorithm.RS256, publicKey);

        long startTime = System.nanoTime();
        var result = signatureValidator.isValid(message.getBytes(), signature);
        long endTime = System.nanoTime();
        System.out.println("verify time: " + (endTime - startTime));

        return result;
    }
}
