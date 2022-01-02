package xyz.crosswars.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.stereotype.Service
import xyz.crosswars.config.AuthConfig
import xyz.crosswars.entities.User
import xyz.crosswars.exception.UnauthorizedException

@Service
class GoogleAuthService(
    authConfig: AuthConfig,
    private val service: UserService
) {
    private val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(authConfig.googleClientId))
        .build()

    fun isValidToken(idToken: String): Boolean =
        verifier.verify(idToken)?.let { true } ?: false

    /**
     * Uses the email in idToken to find the user in the database
     *
     * @param idToken the google oauth idToken
     * @return a User object
     */
    fun mapTokenToUser(idToken: String): User =
        // TODO: see if there's a way to handle token expired exceptions
        verifier.verify(idToken)?.let { googleIdToken ->
            val payload: GoogleIdToken.Payload = googleIdToken.payload

            // Print user identifier
//            val userId: String = payload.subject

            // Get profile information from payload
            val email: String = payload.email
//            val emailVerified: Boolean = java.lang.Boolean.valueOf(payload.emailVerified)
//            val name = payload["name"]
//            val pictureUrl = payload["picture"]
//            val locale = payload["locale"]
//            val familyName = payload["family_name"]
//            val givenName = payload["given_name"]
            service.findUserByEmail(email)
        } ?: throw UnauthorizedException("Google id token is invalid")

    fun createUserFromToken(idToken: String): User =
            // TODO: see if there's a way to handle token expired exceptions
            verifier.verify(idToken)?.let { googleIdToken ->
                val payload: GoogleIdToken.Payload = googleIdToken.payload
                val userId: String = payload.subject
                val email: String = payload.email
                val givenName: String = payload["given_name"] as String
                User(userId = userId, name=givenName, email=email, remind=false)
            } ?: throw UnauthorizedException("Google id token is invalid")
}