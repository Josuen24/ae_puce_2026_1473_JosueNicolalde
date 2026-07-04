package com.pucetec.events.services

import com.pucetec.events.entities.Attendee
import com.pucetec.events.entities.Event
import com.pucetec.events.entities.Reservation
import com.pucetec.events.exceptions.*
import com.pucetec.events.repositories.AttendeeRepository
import com.pucetec.events.repositories.EventRepository
import com.pucetec.events.repositories.ReservationRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ReservationServiceTest {

    @Mock private lateinit var reservationRepository: ReservationRepository
    @Mock private lateinit var attendeeRepository: AttendeeRepository
    @Mock private lateinit var eventRepository: EventRepository
    @InjectMocks private lateinit var reservationService: ReservationService

    private val attendee = Attendee(id = 1L, name = "Ana Torres", email = "ana@puce.edu.ec")
    private val event = Event(id = 1L, name = "Rock Fest", venue = "Quito Arena", totalTickets = 100, availableTickets = 10)
    private val reservation = Reservation(id = 1L, attendee = attendee, event = event, status = "ACTIVE", createdAt = LocalDateTime.now())

    @Test
    fun `createReservation retorna ReservationResponse cuando los datos son validos`() {
        `when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        `when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        `when`(reservationRepository.countByAttendeeIdAndStatus(1L, "ACTIVE")).thenReturn(0L)
        `when`(eventRepository.save(any(Event::class.java))).thenReturn(event)
        `when`(reservationRepository.save(any(Reservation::class.java))).thenReturn(reservation)
        val response = reservationService.createReservation(1L, 1L)
        assertEquals(1L, response.id)
        assertEquals("ACTIVE", response.status)
    }

    @Test
    fun `createReservation lanza AttendeeNotFoundException cuando el asistente no existe`() {
        `when`(attendeeRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<AttendeeNotFoundException> { reservationService.createReservation(99L, 1L) }
    }

    @Test
    fun `createReservation lanza EventNotFoundException cuando el evento no existe`() {
        `when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        `when`(eventRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<EventNotFoundException> { reservationService.createReservation(1L, 99L) }
    }

    @Test
    fun `createReservation lanza SoldOutException cuando no hay tickets disponibles`() {
        val soldOutEvent = Event(id = 2L, name = "Sold Out", venue = "Arena", totalTickets = 10, availableTickets = 0)
        `when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        `when`(eventRepository.findById(2L)).thenReturn(Optional.of(soldOutEvent))
        assertThrows<SoldOutException> { reservationService.createReservation(1L, 2L) }
    }

    @Test
    fun `createReservation lanza ReservationLimitExceededException cuando el asistente tiene 4 reservas activas`() {
        `when`(attendeeRepository.findById(1L)).thenReturn(Optional.of(attendee))
        `when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        `when`(reservationRepository.countByAttendeeIdAndStatus(1L, "ACTIVE")).thenReturn(4L)
        assertThrows<ReservationLimitExceededException> { reservationService.createReservation(1L, 1L) }
    }

    @Test
    fun `cancelReservation retorna ReservationResponse con status CANCELLED`() {
        `when`(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation))
        `when`(eventRepository.save(any(Event::class.java))).thenReturn(event)
        `when`(reservationRepository.save(any(Reservation::class.java))).thenReturn(reservation)
        val response = reservationService.cancelReservation(1L)
        assertNotNull(response)
    }

    @Test
    fun `cancelReservation lanza ReservationNotFoundException cuando la reserva no existe`() {
        `when`(reservationRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<ReservationNotFoundException> { reservationService.cancelReservation(99L) }
    }

    @Test
    fun `cancelReservation lanza ReservationAlreadyCancelledException cuando ya esta cancelada`() {
        val cancelled = Reservation(id = 2L, attendee = attendee, event = event, status = "CANCELLED", createdAt = LocalDateTime.now())
        `when`(reservationRepository.findById(2L)).thenReturn(Optional.of(cancelled))
        assertThrows<ReservationAlreadyCancelledException> { reservationService.cancelReservation(2L) }
    }

    @Test
    fun `getAllReservations retorna lista de ReservationResponse`() {
        `when`(reservationRepository.findAll()).thenReturn(listOf(reservation))
        assertEquals(1, reservationService.getAllReservations().size)
    }

    @Test
    fun `getAllReservations retorna lista vacia cuando no hay reservas`() {
        `when`(reservationRepository.findAll()).thenReturn(emptyList())
        assertTrue(reservationService.getAllReservations().isEmpty())
    }

    @Test
    fun `getReservationById retorna ReservationResponse cuando existe`() {
        `when`(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation))
        assertEquals(1L, reservationService.getReservationById(1L).id)
    }

    @Test
    fun `getReservationById lanza ReservationNotFoundException cuando no existe`() {
        `when`(reservationRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<ReservationNotFoundException> { reservationService.getReservationById(99L) }
    }
}
