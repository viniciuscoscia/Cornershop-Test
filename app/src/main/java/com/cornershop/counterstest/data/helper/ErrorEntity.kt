package com.cornershop.counterstest.data.helper

sealed class ErrorEntity {
    object Network : ErrorEntity()
    object NotFound : ErrorEntity()
    object AccessDenied : ErrorEntity()
    object ServiceUnavailable : ErrorEntity()
    object Unknown : ErrorEntity()
    object UnknownHttpException : ErrorEntity()
//    data class TestClass(val a: String, val b: String): ErrorEntity() I could have been used it :D
}