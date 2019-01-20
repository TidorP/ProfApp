package security;

import domain.Student;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.RandomStringUtils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Enc {
    private String fName;
    private List<String> passwords=new ArrayList<>();

    public Enc(String fName) {
        this.fName=fName;
        try {
            readFromFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void readFromFile() throws FileNotFoundException {
        File file = new File(fName);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            passwords.add(sc.nextLine());
        }
    }
    private void writeToFile()
    {
        try {
                PrintWriter pw = new PrintWriter(new File(fName));
                for (String st : passwords) {
                    pw.println(st);
                }
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

    public static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        int length = 20;
        boolean useLetters = true;
        boolean useNumbers = true;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

        return generatedString.getBytes();
    }
    public boolean check(String user,String pass)
    {
        for(String s:passwords)
        {
            if(s.contains(user))
            {
                String[] splited=s.split(" ");
                return encode(pass, splited[1]).equals(splited[2]);
            }
        }
        return false;
    }
    public String encode(String pass,String salt)
    {

        int iterations = 10000;
        int keyLength = 512;
        char[] passwordChars = pass.toCharArray();
        byte[] saltBytes = salt.getBytes();

        byte[] hashedBytes = hashPassword(passwordChars, saltBytes, iterations, keyLength);
        return Hex.encodeHexString(hashedBytes);
    }
    public void register(String username,String password)
    {
        deleteAccount(username);
        int iterations = 10000;
        int keyLength = 512;
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = new byte[0];
        try {
            saltBytes = getSalt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashedBytes = hashPassword(passwordChars, saltBytes, iterations, keyLength);
        String hashedString = Hex.encodeHexString(hashedBytes);

        String s=username + " " + new String(saltBytes) + " " + hashedString;
        passwords.add(s);
        writeToFile();
    }
    public boolean deleteAccount(String user)
    {
        for(String p:passwords)
        {
            String[] splited=p.split(" ");
            if(splited[0].equals(user)) {
                passwords.remove(p);
                writeToFile();
                return true;
            }
        }

        return false;
    }
}
