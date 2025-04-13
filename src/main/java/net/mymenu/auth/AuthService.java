package net.mymenu.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import net.mymenu.auth.dto.GoogleTokenDTO;
import net.mymenu.auth.refresh_token.RefreshToken;
import net.mymenu.user.User;
import net.mymenu.user.UserRepository;
import net.mymenu.auth.refresh_token.RefreshTokenRepository;
import net.mymenu.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private CookieService cookieService;

    @Value("${google.oauth.client.secret}")
    private String clientId;

    public ResponseEntity<User> getNewTokensResponseEntity(User user) {
        String jwt = jwtHelper.generateUserToken(user);
        String newRefreshToken = jwtHelper.generateRefreshToken(user);

        RefreshToken newRefreshTokenEntity = RefreshToken
                .builder()
                .token(newRefreshToken)
                .userId(user.getId())
                .build();
        refreshTokenRepository.save(newRefreshTokenEntity);

        ResponseCookie cookieRefreshToken = cookieService.createRefreshTokenCookie(newRefreshToken);
        ResponseCookie cookieAccessToken = cookieService.createAccessTokenCookie(jwt);
        ResponseCookie cookieIsAuthenticated = cookieService.createIsAuthenticatedCookie(true);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookieRefreshToken.toString(), cookieAccessToken.toString(), cookieIsAuthenticated.toString())
                .body(user);
    }

    public User loginOAuthGoogle(GoogleTokenDTO googleTokenDTO) {
        GoogleIdToken idToken = verifyIdToken(googleTokenDTO.getCredential());
        GoogleIdToken.Payload payload = idToken.getPayload();

        return getOrCreateUser(payload);
    }

    private GoogleIdToken verifyIdToken(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = GoogleIdToken.parse(verifier.getJsonFactory(), token);

            verifier.verify(idToken);

            return idToken;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User getOrCreateUser(GoogleIdToken.Payload payload) {
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");
        String email = payload.getEmail();
//            String pictureUrl = (String) payload.get("picture");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = User.builder()
                    .email(email)
                    .name(firstName + " " + lastName)
                    .isVerifiedEmail(true)
                    .build();
            userRepository.save(user);
        }

        return user;
    }
}
