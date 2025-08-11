// File: src/main/java/com/example/codingbattle/service/RoomService.java
package com.example.codingbattle.service;

import com.example.codingbattle.dto.CreateRoomRequest;
import com.example.codingbattle.model.Room;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    /**
     * Creates a new coding battle room
     */
    Room createRoom(CreateRoomRequest request);

    /**
     * Retrieves a room by its short ID
     */
    Optional<Room> getRoomByShortId(String shortId);

    /**
     * Checks if a room exists by short ID
     */
    boolean doesRoomExist(String shortId);

    /**
     * Gets all rooms with pagination
     */
    List<Room> getAllRooms(int page, int size);

    /**
     * Updates room status (WAITING, ACTIVE, COMPLETED)
     */
    Room updateRoomStatus(String shortId, String status);

    /**
     * Deletes a room by short ID
     */
    void deleteRoom(String shortId);

    /**
     * Adds a participant to the room
     */
    Room addParticipant(String shortId, String userName);

    /**
     * Removes a participant from the room
     */
    Room removeParticipant(String shortId, String userName);
}