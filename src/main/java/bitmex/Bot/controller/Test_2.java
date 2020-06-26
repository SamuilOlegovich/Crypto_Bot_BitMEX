package bitmex.Bot.controller;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Test_2 {

    public static void main(String[] args) {
    }

    public class Crypt {

        Cipher ecipher;
        Cipher dcipher;

        /**
         * Конструктор
         */
        public Crypt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
            // ВОТ КЛЮЧ, КОТОРЫМ ШИФРУЕМ
            byte[] keyBytes = "paroli14".getBytes();
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        }

        /**
         * Функция шифровнаия
         */
//        public String encrypt(String str) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
//            byte[] utf8 = str.getBytes("UTF8");
//            byte[] enc = ecipher.doFinal(utf8);
//            return new sun.misc.BASE64Encoder().encode(enc);
//        }

        /**
         * Функция расшифрования
         **/
//        public String decrypt(String str) throws IOException, IllegalBlockSizeException, BadPaddingException {
//            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
//            byte[] utf8 = dcipher.doFinal(dec);
//            return new String(utf8, "UTF8");
//        }
    }
}




