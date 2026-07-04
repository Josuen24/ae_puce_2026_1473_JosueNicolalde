package com.pucetec.events.controllers

import com.pucetec.events.dto.AttendeeRequest
import com.pucetec.events.dto.AttendeeResponse
import com.pucetec.events.services.AttendeeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/attendees")
class AttendeeController(private val attendeeService: AttendeeService) {

    @PostMapping
    fun createAttendee(@RequestBody request: AttendeeRequest): ResponseEntity<AttendeeResponse> =
        ResponseEntity(attendeeService.createAttendee(request), HttpStatus.CREATED)

    @GetMapping
    fun getAllAttendees(): ResponseEntity<List<AttendeeResponse>> =
        ResponseEntity.ok(attendeeService.getAllAttendees())
}
