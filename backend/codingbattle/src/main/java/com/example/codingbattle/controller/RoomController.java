// File: src/main/java/com/example/codingbattle/controller/RoomController.java
package com.example.codingbattle.controller;

import com.example.codingbattle.dto.CreateRoomRequest;
import com.example.codingbattle.dto.ErrorResponse;
import com.example.codingbattle.exception.CodingBattleException;
import com.example.codingbattle.model.Room;
import com.example.codingbattle.service.RoomService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity<Room> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        logger.info("Creating room with name: {}", request.getName());
        Room room = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @GetMapping("/{shortId}")
    public ResponseEntity<Room> getRoomByShortId(@PathVariable String shortId) {
        logger.debug("Fetching room with shortId: {}", shortId);

        Optional<Room> roomOptional = roomService.getRoomByShortId(shortId);

        if (roomOptional.isPresent()) {
            return ResponseEntity.ok(roomOptional.get());
        } else {
            throw CodingBattleException.roomNotFound(shortId);
        }
    }

    @GetMapping("/{shortId}/exists")
    public ResponseEntity<Boolean> roomExists(@PathVariable String shortId) {
        logger.debug("Checking if room exists: {}", shortId);
        boolean exists = roomService.doesRoomExist(shortId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.debug("Fetching rooms - page: {}, size: {}", page, size);
        List<Room> rooms = roomService.getAllRooms(page, size);
        return ResponseEntity.ok(rooms);
    }

    @PutMapping("/{shortId}/status")
    public ResponseEntity<Room> updateRoomStatus(
            @PathVariable String shortId,
            @RequestParam String status) {
        logger.info("Updating room {} status to: {}", shortId, status);
        Room updatedRoom = roomService.updateRoomStatus(shortId, status);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{shortId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String shortId) {
        logger.info("Deleting room: {}", shortId);
        roomService.deleteRoom(shortId);
        return ResponseEntity.noContent().build();
    }
}