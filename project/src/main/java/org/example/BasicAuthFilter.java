package org.example;

import java.util.Base64;

/**
 *
 */
public class BasicAuthFilter {

    // extract Credentials from Header
    public String[] extractCredentials(String encodedHeader) {
        if (encodedHeader != null) {
            String decodedHeader = new String(Base64.getDecoder().decode(encodedHeader));
            return decodedHeader.split(":");
        } else {
            return null;
        }

    }
}
