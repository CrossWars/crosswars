package xyz.crossward.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(
        ex: BadRequestException,
        request: HttpServletRequest
    ): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(
                message = ex.message ?: "",
                path = request.servletPath,
                responseCode = 400,
                timestamp = Instant.now()
            ),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(NoContentException::class)
    fun handleNoContentException(
        ex: NoContentException,
        request: HttpServletRequest
    ): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(
                message = ex.message ?: "",
                path = request.servletPath,
                responseCode = 400,
                timestamp = Instant.now()
            ),
            HttpStatus.NO_CONTENT
        )

    data class ExceptionResponse(
        val message: String,
        val path: String,
        val responseCode: Int,
        val timestamp: Instant
    )
}