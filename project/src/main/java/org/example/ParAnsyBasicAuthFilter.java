package prgws2223;

import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

import static spark.Spark.halt;

public class ParAnsyBasicAuthFilter {
    private String username = null;
    private String password = null;

    public ParAnsyBasicAuthFilter(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // exam if request authenticated.
    public void handle(Request request, Response response){
        if (!request.headers().contains("Authorization") || !authenticated(request)) {
            halt(401, "Not Authenticate");
        }
    }
    // exam credentials.
    private Boolean authenticated(Request request) {
        String encodedHeader = request.headers("Authorization");
        encodedHeader.substring(encodedHeader.lastIndexOf("Basic") + 1);
        String[] submittedCredentials = extractCredentials(encodedHeader);
        if (submittedCredentials != null && submittedCredentials.length == 2){
            String submittedUser = submittedCredentials[0];
            String submittedPassword = submittedCredentials[1];
            return Objects.equals(username, submittedUser) && Objects.equals(password, submittedPassword);
        }
        return false;
    }
    // extract Credentials from Header
    private String[] extractCredentials(String encodedHeader){
        if (encodedHeader != null){
            String decodedHeader = new String(Base64.getDecoder().decode(encodedHeader));
            return decodedHeader.split(":");
        }
        else{return null;}

    }

}
