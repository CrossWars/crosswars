package xyz.crossward.exception

import java.lang.RuntimeException

open class ApplicationException(message: String?) : RuntimeException(message) {
}
