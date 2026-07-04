package com.pucetec.events.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ExceptionResponse(val message: String, val source: String)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BlankFieldException::class)
    fun handleBlankField(ex: BlankFieldException) =
        ResponseEntity(ExceptionResponse(ex.message ?: "", "BlankFieldException"), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(InvalidCapacityException::class)
    fun handleInvalidCapacity(ex: InvalidCapacityException) =
        ResponseEntity(ExceptionResponse(ex.message ?: "", "InvalidCapacityException"), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(AttendeeNotFoundException::class)
    fun handleAttendeeNotFound(ex: AttendeeNotFoundException) =
        ResponseEntity(ExceptionResponse(ex.message ?: "", "AttendeeNotFoundException"), HttpStatus.NOT_FOUND)

    @ExceptionHandler(EventNotFoundException::class)
    fun handleEventNotFound(ex: EventNotFoundException) =
        ResponseEntity(ExceptionResponse(ex.message ?: "", "EventNotFoundException"), HttpStatus.NOT_FOUND)

    @ExceptionHandler(ReservationNotFoundException::class)
    fun handleReservationNotFound(ex: ReservationNotFoundException) =
        ResponseEntity(ExceptionResponse(ex.message ?: "", "ReservationNotFoundException"), HttpStatus.NOT_FOUND)

    @ExceptionHandler(SoldOutException::class)
    fun handleSoldOut(ex: SoldOutException) =
        ResponseEntity(ExceptionResponse(ex.message ?: "", "SoldOutException"), HttpStatus.CONFLICT)

    @ExceptionHandler(ReservationLimitExceededException::class)
    fun handleReservationLimit(ex: ReservationLimitExceededException) =
        ResponseEntity(ExceptionResponse(ex.message ?: "", "ReservationLimitExceededException"), HttpStatus.CONFLICT)

    @ExceptionHandler(ReservationAlreadyCancelledException::class)
    fun handleAlreadyCancelled(ex: ReservationAlreadyCancelledException) =
        ResponseEntity(ExceptionResponse(ex.message ?: "", "ReservationAlreadyCancelledException"), HttpStatus.CONFLICT)
}
