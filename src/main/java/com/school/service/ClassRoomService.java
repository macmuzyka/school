package com.school.service;

import com.school.model.entity.classschedule.ClassRoom;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.sql.classschedule.ClassRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassRoomService {
    private final ClassRoomRepository classRoomRepository;

    public ClassRoomService(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    public List<ClassRoom> getUnassignedClassRooms() {
        return classRoomRepository.findAll().stream().filter(room -> !room.isOccupied()).collect(Collectors.toList());
    }

    public void updateWithAssignedTimeSlot(ClassRoom classRoom, TimeSlot timeSlot) {
        classRoom.addTimeSlot(timeSlot);
        classRoomRepository.save(classRoom);
    }
}
