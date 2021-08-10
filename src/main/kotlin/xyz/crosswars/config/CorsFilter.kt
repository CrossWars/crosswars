package xyz.crosswars.config

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import xyz.crosswars.exception.UnauthorizedException
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class SimpleCORSFilter : Filter {

    private val allowedOrigins = listOf("http://localhost:3000", "https://crosswars.xyz")

    @Throws(ServletException::class)
    override fun init(fc: FilterConfig?) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        req: ServletRequest,
        resp: ServletResponse,
        chain: FilterChain
    ) {
        val response = resp as HttpServletResponse
        val request = req as HttpServletRequest

        req.getHeader("Origin")?.let { origin ->
            if (origin !in allowedOrigins) {
                throw UnauthorizedException("Unauthorized")
            }
        }

        response.setHeader("Access-Control-Allow-Origin", allowedOrigins.joinToString(", "))
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.setHeader("Access-Control-Max-Age", "3600")
        response.setHeader(
            "Access-Control-Allow-Headers",
            "x-requested-with, authorization, Content-Type, Authorization, credential, Accept, x-internal-access-token"
        )
        if ("OPTIONS".equals(request.method, ignoreCase = true)) {
            response.status = HttpServletResponse.SC_OK
        } else {
            chain.doFilter(req, resp)
        }
    }

    override fun destroy() {}
}