package com.pucetec.events.controllers

import com.pucetec.events.dto.ReservationRequest
import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.services.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reservations")
class ReservationController(private val reservationService: ReservationService) {

    @PostMapping
    fun createReservation(@RequestBody request: ReservationRequest): ResponseEntity<ReservationResponse> =
        ResponseEntity(reservationService.createReservation(request.attendeeId, request.eventId), HttpStatus.CREATED)

    @PutMapping("/{id}/cancel")
    fun cancelReservation(@PathVariable id: Long): ResponseEntity<ReservationResponse> =
        ResponseEntity.ok(reservationService.cancelReservation(id))

    @GetMapping
    fun getAllReservations(): ResponseEntity<List<ReservationResponse>> =
        ResponseEntity.ok(reservationService.getAllReservations())

    @GetMapping("/{id}")
    fun getReservationById(@PathVariable id: Long): ResponseEntity<ReservationResponse> =
        ResponseEntity.ok(reservationService.getReservationById(id))
}
