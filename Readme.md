# AWS KMS JWS example

This code is to demo signing a JWT with AWS KMS(RS256).

Notes:
  1. Configure your environment variable _AWS_ACCESS_KEY_ID_** and _AWS_SECRET_ACCESS_KEY_**.
  2. Setup an RSA key pair for signing in KMS.
  3. Copy the public key in **publicKey.pem**.
  4. Configure _Region_** and _KeyArn_** in **Kms.java** with your corresponding value.
  5. Configure _Alg_** with your expecting algorithm.
  

