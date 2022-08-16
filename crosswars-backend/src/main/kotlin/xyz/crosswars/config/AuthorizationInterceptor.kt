package xyz.crosswars.config

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import xyz.crosswars.exception.UnauthorizedException
import xyz.crosswars.service.GoogleAuthService
import xyz.crosswars.service.UserService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Intercept all requests and check for the Authorized annotation. If it exists, authorize the request coming in with
 * either auth token or google api token.
 */
@Component
class AuthorizationInterceptor(
    private val authConfig: AuthConfig,
    private val googleAuthService: GoogleAuthService,
) : HandlerInterceptor {

    @Transactional
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var isAuthorized = false
        if (handler is HandlerMethod) {
            val authMethods: Authorized = handler.getMethodAnnotation(Authorized::class.java)
                ?: return true // Authorized annotation is missing, allow the request to proceed

            if (!authConfig.enabled)
            {
                request.setAttribute("auth_user", null)
                isAuthorized = true
            }

            if (authMethods.googleIdToken) {
                request.getHeader("Authorization")?.split(" ")?.get(1).let { idToken ->
                    idToken?.let {
                        val user = googleAuthService.mapTokenToUser(it)
                        request.setAttribute("auth_user", user)
                        isAuthorized = true
                    }
                }
            }

            if (authMethods.authToken) {
                request.getHeader("x-internal-access-token")?.let { authHeader ->
                    //if using auth token, ensure keys match
                    if (authHeader != authConfig.authKey) throw UnauthorizedException("Invalid auth token")
                    request.setAttribute("auth_user", null) // no auth user associated with auth token
                    isAuthorized = true
                }
            }
        }
        if (!isAuthorized) throw UnauthorizedException("Request is Unauthorized ")
        return isAuthorized
    }
}