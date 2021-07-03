package xyz.crossward.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.springframework.stereotype.Service
import xyz.crossward.config.GoogleAuthConfig
import xyz.crossward.entities.User
import xyz.crossward.exception.UnauthorizedException

@Service
class GoogleAuthService(
    googleAuthConfig: GoogleAuthConfig,
    private var service: UserService
) {
    private val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
        .setAudience(listOf(googleAuthConfig.clientId))
        .build()

    fun isValidated(idToken: String): Boolean =
        verifier.verify(idToken)?.let { true } ?: false

    fun mapTokenToUser(idToken: String): User =
        verifier.verify(idToken)?.let { googleIdToken ->
            val payload: GoogleIdToken.Payload = googleIdToken.payload

            // Print user identifier
            val userId: String = payload.subject

            // Get profile information from payload
            val email: String = payload.email
            val emailVerified: Boolean = java.lang.Boolean.valueOf(payload.emailVerified)
            val name = payload["name"]
            val pictureUrl = payload["picture"]
            val locale = payload["locale"]
            val familyName = payload["family_name"]
            val givenName = payload["given_name"]

            service.findUserByEmail(email)
        } ?: throw UnauthorizedException("User is not authorized")
}