package bitmex.Bot.controller;



import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOError;
import java.util.Base64;

public class Test {

    public static void main(String[] args) {

        Test test = new Test();
        System.out.println(test.generateSignature("Hello", "World"));

    }

        public String generateSignature(String key, String string) {
            //String keyString =  "1";

            try {
                Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
                sha256_HMAC.init(secret_key);
                String hash = DatatypeConverter.printHexBinary(sha256_HMAC.doFinal(string.getBytes()));

                return hash;
            } catch (Exception e) {
                throw new IOError(e);
            }
        }

//    public static String decodeString(String encodeText)
//            throws Exception {
//        byte[] decodeBytes = Base64.decode(encodeText);
//        String str = new String(decodeBytes, "UTF-8");
//        return str;
//    }







}
