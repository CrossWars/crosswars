package xyz.crosswars.config

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorized(
    val authToken: Boolean = true,
    val googleIdToken: Boolean = true
)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthorizedNewUser(
        val authToken: Boolean = true,
        val googleIdToken: Boolean = true
)