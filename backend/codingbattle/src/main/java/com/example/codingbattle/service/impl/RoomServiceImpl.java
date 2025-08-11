package com.example.codingbattle.service.impl;

import com.example.codingbattle.dto.CreateRoomRequest;
import com.example.codingbattle.exception.CodingBattleException;
import com.example.codingbattle.model.Room;
import com.example.codingbattle.repository.RoomRepository;
import com.example.codingbattle.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);
    private static final int SHORT_ID_LENGTH = 8;

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(CreateRoomRequest request) {
        logger.info("Creating room: {}", request.getName());

        Room room = new Room();
        room.setShortId(generateUniqueShortId());
        room.setName(request.getName());
        room.setQuestion(request.getQuestion());
        room.setTestCases(request.getTestCases());
        room.setCreatedBy(request.getOwner());
        room.setActive(true);

        Room savedRoom = roomRepository.save(room);
        logger.info("Successfully created room with ID: {} and Short ID: {}", savedRoom.getId(), savedRoom.getShortId());
        return savedRoom;
    }

    private String generateUniqueShortId() {
        String shortId;
        do {
            StringBuilder sb = new StringBuilder(SHORT_ID_LENGTH);
            for (int i = 0; i < SHORT_ID_LENGTH; i++) {
                sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(
                        (int) (Math.random() * "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length())));
            }
            shortId = sb.toString();
        } while (roomRepository.existsByShortId(shortId));
        return shortId;
    }

    @Override
    public Optional<Room> getRoomByShortId(String shortId) {
        return roomRepository.findByShortId(shortId);
    }

    @Override
    public boolean doesRoomExist(String shortId) {
        return roomRepository.existsByShortId(shortId);
    }

    @Override
    public List<Room> getAllRooms(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return roomRepository.findAll(pageable).getContent();
    }

    @Override
    public Room updateRoomStatus(String shortId, String status) {
        Room room = roomRepository.findByShortId(shortId)
                .orElseThrow(() -> CodingBattleException.roomNotFound(shortId));

        // Validate status
        if (!Arrays.asList("WAITING", "ACTIVE", "COMPLETED").contains(status.toUpperCase())) {
            throw new CodingBattleException("Invalid room status: " + status);
        }

        room.setActive("ACTIVE".equals(status.toUpperCase()));
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(String shortId) {
        Room room = roomRepository.findByShortId(shortId)
                .orElseThrow(() -> CodingBattleException.roomNotFound(shortId));
        roomRepository.delete(room);
    }

    @Override
    public Room addParticipant(String shortId, String userName) {
        Room room = roomRepository.findByShortId(shortId)
                .orElseThrow(() -> CodingBattleException.roomNotFound(shortId));

        // Implementation depends on your Room model having a participants field
        // This is a placeholder - you'd need to add participants field to Room model
        return roomRepository.save(room);
    }

    @Override
    public Room removeParticipant(String shortId, String userName) {
        Room room = roomRepository.findByShortId(shortId)
                .orElseThrow(() -> CodingBattleException.roomNotFound(shortId));

        // Implementation depends on your Room model having a participants field
        return roomRepository.save(room);
    }
}