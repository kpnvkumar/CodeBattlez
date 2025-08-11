// File: src/main/java/com/example/codingbattle/repository/RoomRepository.java
package com.example.codingbattle.repository;

import com.example.codingbattle.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {

    // ADDED: Method to find a room by our custom, user-friendly ID
    Optional<Room> findByShortId(String shortId);

    // ADDED: Method to check if a room with this shortId already exists
    boolean existsByShortId(String shortId);

    List<Room> findByIsActiveTrue();
    List<Room> findByCreatedBy(String createdBy);

    @Query("{ '_id': ?0, 'isActive': true }")
    Optional<Room> findByIdAndActive(String id);

    @Query("{ 'isActive': true, 'createdAt': { $gte: ?0 } }")
    List<Room> findActiveRoomsCreatedAfter(LocalDateTime date);
}