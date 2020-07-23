package com.stocksScreener.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.stocksScreener.model.User;
import com.stocksScreener.repository.UserRepository;
import com.stocksScreener.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
public class LoginService {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final String GOOGLE_CLIENT_ID;

    /**
     * Constructs a LoginService with specified UserRepository, JWTUtil objects and GOOGLE_CLIENT_ID string
     * @param userService UserService object
     * @param jwtUtil
     * @param GOOGLE_CLIENT_ID
     */
    @Autowired
    public LoginService(UserService userService, JWTUtil jwtUtil,
                        @Value("${google.clientID}") String GOOGLE_CLIENT_ID) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.GOOGLE_CLIENT_ID = GOOGLE_CLIENT_ID;
    }

    /**
     * Method to Verify ID token received after Google Sign In and return JWT token if valid otherwise null
     *
     * @param token Google ID token received after Google Sign In
     * @return String containing JWT token if Google ID token is valid otherwise null
     * @throws GeneralSecurityException Safety for all security related Exceptions
     * @throws IOException              Produced by failed or interrupted I/O operations.
     */
    public String verifyIdToken(String token) throws GeneralSecurityException, IOException {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = Utils.getDefaultJsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(token);
        String jwt = null;

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            final String userId = payload.getSubject();
            final String email = payload.getEmail();
            final String name = (String) payload.get("name");
            final String pictureUrl = (String) payload.get("picture");

            Optional<User> user = userService.findById(userId);

            if (user.isPresent()) {
                jwt = jwtUtil.createToken(user.get());
                userService.saveUser(user.get());
            } else {
                insertUserToDatabase(userId, email, name, pictureUrl);
            }
        }
        return jwt;
    }

    /**
     * Method to insert new user into the database
     * @param userId User ID is the Google assigned numeric ID
     * @param email User Email ID
     * @param name Full name of the user
     * @param pictureUrl Url for the avatar of the user
     */
    private void insertUserToDatabase(String userId, String email, String name, String pictureUrl) {
        userService.saveUser(new User(userId, email, name, pictureUrl, "google"));
    }
}
