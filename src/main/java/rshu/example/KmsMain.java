package rshu.example;

import rshu.example.jwtUtil.BodyBean;
import rshu.example.jwtUtil.HeaderBean;

import java.io.IOException;

public class KmsMain {
    public static void main(String[] args) throws IOException {

        HeaderBean header = new HeaderBean();
        header.alg = "RS256";
        header.typ = "JWT";
        header.kid = "123456";

        BodyBean body = new BodyBean();
        body.exp = 1504699256;
        body.iat = 1504699136;
        body.add("iss", "Authentication Service");
        body.add("sub", "Authorization token");

        String token = Kms.getToken(header, body);

        System.out.println("token: " + token);

        System.out.println("validation result: " + Kms.validateToken(token));
    }
}
