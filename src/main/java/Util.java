import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {
    
    public static String convertObjectToString(final Object input) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(input);
        } catch (JsonProcessingException ex) {
            System.out.println("ex = " + ex);
            ex.printStackTrace();
        }
        return "";
    }

}
