package com.zbank.bankaccount.port.controller.handler

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.zbank.bankaccount.domain.model.common.BusinessException
import com.zbank.bankaccount.domain.model.common.EntityNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ControllerExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(BusinessException::class)
    fun handleAccountAlreadyExists(ex: BusinessException, request: WebRequest): ResponseEntity<Any> {
        val errorBody = ErrorBody(ex.message, listOf())

        return handleExceptionInternal(ex, errorBody, HttpHeaders.EMPTY, HttpStatus.UNPROCESSABLE_ENTITY, request)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleAccountAlreadyExists(ex: EntityNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val errorBody = ErrorBody(ex.message, listOf())

        return handleExceptionInternal(ex, errorBody, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, request)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errorBody = ErrorBody(ex.localizedMessage, listOf())
        val cause = ex.cause
        if (cause is MissingKotlinParameterException) {
            return handleMissingKotlinParameterException(cause, headers, status, request)
        }
        return handleExceptionInternal(ex, errorBody, headers, status, request)
    }

    private fun handleMissingKotlinParameterException(
        ex: MissingKotlinParameterException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val propertyPath = ex.path.joinToString(".") { it.fieldName }
        val fieldError = FieldError(
            propertyPath,
            "is required",
            "required.field"
        )
        val errorBody = ErrorBody("Json property missing", listOf(fieldError))
        return handleExceptionInternal(ex, errorBody, headers, status, request)
    }

    override fun handleExceptionInternal(
        ex: Exception, body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val bodyError = when (body) {
            is ErrorBody -> ApiError(status.value(), body.message, status.reasonPhrase, body.fieldErrors)
            else -> ApiError(status.value(), "An error occurred", status.reasonPhrase)
        }
        return ResponseEntity.status(status).headers(headers).body(bodyError)
    }

}
