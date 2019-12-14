import java.util.Base64;
import java.util.Base64.Encoder;

public class Encode {
    
    public static void main(String ... arg) {
        if (arg == null || arg.length != 1) {
            System.out.println("Format is: java Encode <string to encode>");
            return;
        }
        Encoder encoder = Base64.getEncoder();
        String val = new String(encoder.encode(encoder.encode(arg[0].getBytes())));
        System.out.println("Encoded value:\""+val+"\"");
    }
}