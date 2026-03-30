package go.seni.java.heplers;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringTokenizer;


public class TokenCredentialHelper {

    public static String[] parseCredentials(String authCredentials) {
        StringTokenizer tokenizer = new StringTokenizer(authCredentials, " ");
        if (!tokenizer.hasMoreTokens()) {
            //  throw new BasicException(MessageCode.FORBIDDEN);
        }
        final String key = tokenizer.nextToken().toUpperCase();
        if (!tokenizer.hasMoreTokens()) {
            //   throw new BasicException(MessageCode.FORBIDDEN);
        }
        final String value = tokenizer.nextToken();

        if (key.isEmpty() || value.isEmpty()) {
            //   throw new BasicException(MessageCode.FORBIDDEN);
        }

        String usernameAndPassword;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(value);
            usernameAndPassword = new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ///   throw new BasicException(MessageCode.FORBIDDEN);
            return null;
        }
        tokenizer = new StringTokenizer(usernameAndPassword, ":");

        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        return new String[]{username, password};
    }
}
