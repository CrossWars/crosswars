package xyz.crosswars.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.SEE_OTHER)
class SeeOtherException(message: String?) : ApplicationException(message)
