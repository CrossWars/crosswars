package xyz.crossward.config

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorized(
    val authToken: Boolean = true,
    val googleIdToken: Boolean = true
)
