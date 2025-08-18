package com.school;

import com.school.model.entity.classschedule.ClassRoom;
import com.school.repository.SchoolRepository;
import com.school.model.entity.School;
import com.school.model.entity.SchoolClass;
import com.school.repository.classschedule.ClassRoomRepository;
import com.school.service.SchoolClassService;
import com.school.service.classschedule.EmptyScheduleSchemaBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WarmupDatabasePopulation implements ApplicationListener<ApplicationStartedEvent> {
    private final SchoolRepository schoolRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SchoolClassService schoolClassService;
    private final EmptyScheduleSchemaBuilderService emptyScheduleSchemaBuilderService;
    private static final Logger log = LoggerFactory.getLogger(WarmupDatabasePopulation.class);

    public WarmupDatabasePopulation(SchoolRepository schoolRepository,
                                    ClassRoomRepository classRoomRepository,
                                    SchoolClassService schoolClassService,
                                    EmptyScheduleSchemaBuilderService emptyScheduleSchemaBuilderService) {
        this.schoolRepository = schoolRepository;
        this.classRoomRepository = classRoomRepository;
        this.schoolClassService = schoolClassService;
        this.emptyScheduleSchemaBuilderService = emptyScheduleSchemaBuilderService;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("Warmup database population");
        if (schoolRepository.findAll().isEmpty()) {
            addSchoolWithClassOnWarmup();
            addRooms();
        } else {
            log.info("Warmup population not needed!");
        }
    }

    private void addSchoolWithClassOnWarmup() {
        School savedSchool = schoolRepository.save(new School("Main School NO. 1", new ArrayList<>()));
        SchoolClass firstWarmupClass = createWarmupSchoolClass();
        savedSchool.getSchoolClasses().add(firstWarmupClass);
        schoolRepository.save(savedSchool);
        log.info("Application warmup school saved to database!");
        log.debug("Saved School: {}", savedSchool);

        emptyScheduleSchemaBuilderService.generateEmptySchedule(firstWarmupClass);
    }

    private SchoolClass createWarmupSchoolClass() {
        return schoolClassService.createNewClassWithAssignedSubjects();
    }

    private void addRooms() {
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
