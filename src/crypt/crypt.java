package crypt;

import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class crypt {

    enum State {
        KEY_GENERATE_ERROR, KEY_GENERATE;
    }

    private IvParameterSpec iv;
    private SecretKeySpec skeySpec;
    private State state;
    private Cipher cipher;

    private static final int maxPow = 16;
    private int pow;
    private Random r;

    public crypt(int g, int p, int clientKey, String saltString) {
        r = new Random();
    }

    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }

    public crypt generateKey(String key, String initVector, int p) {
        try {
            pow = 1 + r.nextInt(maxPow);
            iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            skeySpec = new SecretKeySpec(keyLength(String.valueOf(Math.pow(Integer.valueOf(key), pow) % p)).getBytes("UTF-8"),
                    "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            setState(State.KEY_GENERATE);
        } catch (Exception e) {
            System.out.println(State.KEY_GENERATE_ERROR + "\t" + e.getMessage());
            setState(State.KEY_GENERATE_ERROR);
        }
        return this;
    }
    public int getFirstKey(int g, int p){
        return ((int)Math.pow(g, pow)) % p;
    }
    public String keyLength(String key){
        while(key.length() < 16){
            key = key + "q";
        }
        if(key.length() > 16){
            key = key.substring(0, 16);
        }
        return key;
    }

    public cryptAnswer encode(String strToEncrypt){
        try{
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return new cryptAnswer(cryptAnswer.State.READY, Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes())));
        }
        catch (Exception e) {
            System.out.println(cryptAnswer.State.ENCODE_ERROR + "\t" + e.getMessage());
            return new cryptAnswer(cryptAnswer.State.ENCODE_ERROR,"");
        }
    }
    public cryptAnswer decode(String strToDecrypt){
        try{
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return new cryptAnswer(cryptAnswer.State.READY,new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt))));
        }
        catch (Exception e) {
            System.out.println(cryptAnswer.State.DECODE_ERROR + "\t" + e.getMessage());
            return new cryptAnswer(cryptAnswer.State.DECODE_ERROR,"");
        }
    }

}