package com.school.service;

import com.school.model.entity.School;
import com.school.model.entity.SchoolClass;
import com.school.model.entity.classschedule.ClassRoom;
import com.school.repository.sql.SchoolRepository;
import com.school.repository.sql.classschedule.ClassRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ApplicationWarmupService {
    private final SchoolRepository schoolRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SchoolClassService schoolClassService;
    private final Logger log = LoggerFactory.getLogger(ApplicationWarmupService.class);

    public ApplicationWarmupService(SchoolRepository schoolRepository,
                                    ClassRoomRepository classRoomRepository,
                                    SchoolClassService schoolClassService) {
        this.schoolRepository = schoolRepository;
        this.classRoomRepository = classRoomRepository;
        this.schoolClassService = schoolClassService;
    }

    public void addSchoolWithClassOnWarmup() {
        School savedSchool = saveMainSchool();
        SchoolClass firstWarmupClass = createWarmupSchoolClass();
        assignWarmupClass(savedSchool, firstWarmupClass);
        addRooms();
    }

    private School saveMainSchool() {
        return schoolRepository.save(new School("Main School NO. 1", new ArrayList<>()));
    }

    private SchoolClass createWarmupSchoolClass() {
        return schoolClassService.createNewClassWithAssignedSubjects();
    }

    private void assignWarmupClass(School savedSchool, SchoolClass firstWarmupClass) {
        savedSchool.getSchoolClasses().add(firstWarmupClass);
        schoolRepository.save(savedSchool);
        log.debug("Application warmup school saved to database");
        log.debug("Saved School: {}", savedSchool);
    }

    private void addRooms() {
        log.debug("Adding rooms...");
        generateRoomsForFloors();
    }

    private void generateRoomsForFloors() {
        for (int floor = 1; floor <= 8; floor++) {
            for (int floorRoom = 1; floorRoom <= 50; floorRoom++) {
                classRoomRepository.save(new ClassRoom(floor * 100 + floorRoom));
            }
        }
        log.info("Generated {} rooms upon application startup", classRoomRepository.count());
    }
}
