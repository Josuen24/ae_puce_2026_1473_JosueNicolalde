package com.pucetec.events.services

import com.pucetec.events.dto.ReservationResponse
import com.pucetec.events.entities.Reservation
import com.pucetec.events.exceptions.*
import com.pucetec.events.mappers.toResponse
import com.pucetec.events.repositories.AttendeeRepository
import com.pucetec.events.repositories.EventRepository
import com.pucetec.events.repositories.ReservationRepository
import org.springframework.stereotype.Service

@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val attendeeRepository: AttendeeRepository,
    private val eventRepository: EventRepository
) {

    fun createReservation(attendeeId: Long, eventId: Long): ReservationResponse {
        val attendee = attendeeRepository.findById(attendeeId)
            .orElseThrow { AttendeeNotFoundException("Attendee not found with id: $attendeeId") }

        val event = eventRepository.findById(eventId)
            .orElseThrow { EventNotFoundException("Event not found with id: $eventId") }

        if (event.availableTickets < 1) {
            throw SoldOutException("Event is sold out")
        }

        val activeReservations = reservationRepository.countByAttendeeIdAndStatus(attendeeId, "ACTIVE")
        if (activeReservations >= 4) {
            throw ReservationLimitExceededException("Attendee has reached the maximum of 4 active reservations")
        }

        event.availableTickets -= 1
        eventRepository.save(event)

        val reservation = Reservation(attendee = attendee, event = event)
        return reservationRepository.save(reservation).toResponse()
    }

    fun cancelReservation(reservationId: Long): ReservationResponse {
        val reservation = reservationRepository.findById(reservationId)
            .orElseThrow { ReservationNotFoundException("Reservation not found with id: $reservationId") }

        if (reservation.status == "CANCELLED") {
            throw ReservationAlreadyCancelledException("Reservation is already cancelled")
        }

        reservation.status = "CANCELLED"
        reservation.event.availableTickets += 1
        eventRepository.save(reservation.event)

        return reservationRepository.save(reservation).toResponse()
    }

    fun getAllReservations(): List<ReservationResponse> = reservationRepository.findAll().map { it.toResponse() }

    fun getReservationById(id: Long): ReservationResponse =
        reservationRepository.findById(id)
            .orElseThrow { ReservationNotFoundException("Reservation not found with id: $id") }
            .toResponse()
}
