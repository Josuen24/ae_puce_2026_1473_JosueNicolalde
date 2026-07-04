package com.pucetec.events.controllers

import com.pucetec.events.dto.EventRequest
import com.pucetec.events.dto.EventResponse
import com.pucetec.events.services.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events")
class EventController(private val eventService: EventService) {

    @PostMapping
    fun createEvent(@RequestBody request: EventRequest): ResponseEntity<EventResponse> =
        ResponseEntity(eventService.createEvent(request), HttpStatus.CREATED)

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventResponse>> =
        ResponseEntity.ok(eventService.getAllEvents())

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: Long): ResponseEntity<EventResponse> =
        ResponseEntity.ok(eventService.getEventById(id))
}
