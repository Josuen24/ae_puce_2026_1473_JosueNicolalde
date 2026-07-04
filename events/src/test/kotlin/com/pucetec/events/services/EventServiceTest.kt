package com.pucetec.events.services

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.entities.Event
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.exceptions.EventNotFoundException
import com.pucetec.events.exceptions.InvalidCapacityException
import com.pucetec.events.repositories.EventRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class EventServiceTest {

    @Mock private lateinit var eventRepository: EventRepository
    @InjectMocks private lateinit var eventService: EventService

    private val event = Event(id = 1L, name = "Rock Fest", venue = "Quito Arena", totalTickets = 100, availableTickets = 100)

    @Test
    fun `createEvent retorna EventResponse cuando los datos son validos`() {
        `when`(eventRepository.save(any(Event::class.java))).thenReturn(event)
        val response = eventService.createEvent(EventRequest(name = "Rock Fest", venue = "Quito Arena", totalTickets = 100))
        assertEquals(1L, response.id)
        assertEquals("Rock Fest", response.name)
        assertEquals(100, response.availableTickets)
    }

    @Test
    fun `createEvent lanza BlankFieldException cuando el nombre esta en blanco`() {
        assertThrows<BlankFieldException> {
            eventService.createEvent(EventRequest(name = "", venue = "Quito Arena", totalTickets = 100))
        }
    }

    @Test
    fun `createEvent lanza BlankFieldException cuando el venue esta en blanco`() {
        assertThrows<BlankFieldException> {
            eventService.createEvent(EventRequest(name = "Rock Fest", venue = "", totalTickets = 100))
        }
    }

    @Test
    fun `createEvent lanza InvalidCapacityException cuando totalTickets es menor a 1`() {
        assertThrows<InvalidCapacityException> {
            eventService.createEvent(EventRequest(name = "Rock Fest", venue = "Quito Arena", totalTickets = 0))
        }
    }

    @Test
    fun `getAllEvents retorna lista de EventResponse`() {
        `when`(eventRepository.findAll()).thenReturn(listOf(event))
        assertEquals(1, eventService.getAllEvents().size)
    }

    @Test
    fun `getAllEvents retorna lista vacia cuando no hay eventos`() {
        `when`(eventRepository.findAll()).thenReturn(emptyList())
        assertTrue(eventService.getAllEvents().isEmpty())
    }

    @Test
    fun `getEventById retorna EventResponse cuando el evento existe`() {
        `when`(eventRepository.findById(1L)).thenReturn(Optional.of(event))
        assertEquals(1L, eventService.getEventById(1L).id)
    }

    @Test
    fun `getEventById lanza EventNotFoundException cuando el evento no existe`() {
        `when`(eventRepository.findById(99L)).thenReturn(Optional.empty())
        assertThrows<EventNotFoundException> { eventService.getEventById(99L) }
    }
}
