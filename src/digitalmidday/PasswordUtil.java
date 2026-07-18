package digitalmidday;
import java.security.*;

public class PasswordUtil {
    public static String hashPassword(String password){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for(byte b : hash){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }catch(Exception e){
            return null;
        }
    }
}