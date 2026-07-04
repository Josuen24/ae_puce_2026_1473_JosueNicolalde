package com.pucetec.events.services

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.entities.Attendee
import com.pucetec.events.exceptions.BlankFieldException
import com.pucetec.events.repositories.AttendeeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AttendeeServiceTest {

    @Mock private lateinit var attendeeRepository: AttendeeRepository
    @InjectMocks private lateinit var attendeeService: AttendeeService

    private val attendee = Attendee(id = 1L, name = "Ana Torres", email = "ana@puce.edu.ec")

    @Test
    fun `createAttendee retorna AttendeeResponse cuando los datos son validos`() {
        `when`(attendeeRepository.save(any(Attendee::class.java))).thenReturn(attendee)
        val response = attendeeService.createAttendee(AttendeeRequest(name = "Ana Torres", email = "ana@puce.edu.ec"))
        assertEquals(1L, response.id)
        assertEquals("Ana Torres", response.name)
    }

    @Test
    fun `createAttendee lanza BlankFieldException cuando el nombre esta en blanco`() {
        assertThrows<BlankFieldException> {
            attendeeService.createAttendee(AttendeeRequest(name = "", email = "ana@puce.edu.ec"))
        }
    }

    @Test
    fun `createAttendee lanza BlankFieldException cuando el email esta en blanco`() {
        assertThrows<BlankFieldException> {
            attendeeService.createAttendee(AttendeeRequest(name = "Ana Torres", email = ""))
        }
    }

    @Test
    fun `getAllAttendees retorna lista de AttendeeResponse`() {
        `when`(attendeeRepository.findAll()).thenReturn(listOf(attendee))
        assertEquals(1, attendeeService.getAllAttendees().size)
    }

    @Test
    fun `getAllAttendees retorna lista vacia cuando no hay asistentes`() {
        `when`(attendeeRepository.findAll()).thenReturn(emptyList())
        assertTrue(attendeeService.getAllAttendees().isEmpty())
    }
}
