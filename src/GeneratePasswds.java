//import org.apache.commons.codec.binary.Hex;
//import org.apache.commons.lang.RandomStringUtils;
//
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.PBEKeySpec;
//import java.io.*;
//import java.nio.charset.Charset;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.security.spec.InvalidKeySpecException;
//import java.util.Random;
//
//public class GeneratePasswds {
//    public static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {
//
//        try {
//            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
//            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
//            SecretKey key = skf.generateSecret( spec );
//            byte[] res = key.getEncoded( );
//            return res;
//        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
//            throw new RuntimeException( e );
//        }
//    }
//    private static byte[] getSalt() throws NoSuchAlgorithmException
//    {
////        //Always use a SecureRandom generator
//////        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//////        //Create array for salt
//////        byte[] salt = new byte[16];
//////        //Get a random salt
//////        sr.nextBytes(salt);
//////        //return salt
//        int length = 20;
//        boolean useLetters = true;
//        boolean useNumbers = true;
//        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
//
//        System.out.println(generatedString);
//
//        return generatedString.getBytes();
//    }
//    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//
//        String password = "aBcxYz123QWERTY";
//
//        int iterations = 10000;
//        int keyLength = 512;
//        char[] passwordChars = password.toCharArray();
//        byte[] saltBytes = getSalt();
//
//        byte[] hashedBytes = hashPassword(passwordChars, saltBytes, iterations, keyLength);
//        String hashedString = Hex.encodeHexString(hashedBytes);
//
//        System.out.println(hashedString);
//
//    }
//}
