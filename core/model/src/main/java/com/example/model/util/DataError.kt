package com.example.model.util

sealed interface DataError : Error {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        BAD_REQUEST,
        UNKNOWN;

        companion object
    }

    enum class Local : DataError {
        DISK_FULL,
        NOT_FOUND,
        QUANTITY_EXCEEDED,
        MIN_QUANTITY_REACHED
    }
}