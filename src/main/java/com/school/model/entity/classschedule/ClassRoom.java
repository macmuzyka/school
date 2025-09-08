package com.school.model.entity.classschedule;

import com.school.model.entity.Audit;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class_room")
public class ClassRoom extends Audit {
    @Column(nullable = false)
    private Integer roomNumber;
    @OneToMany(mappedBy = "classRoom")
    private List<TimeSlot> timeSlot = new ArrayList<>();
    private boolean occupied = false;

    public ClassRoom(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public ClassRoom() {
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void assignTimeSlot(TimeSlot timeSlot) {
        this.timeSlot.add(timeSlot);
        this.occupied = true;
    }

    public void detachFromTimeSlot() {
        this.timeSlot = new ArrayList<>();
        this.occupied = false;
    }

    public List<TimeSlot> getTimeSlot() {
        return timeSlot;
    }

    public void addTimeSlot(TimeSlot timeSlot) {
        this.timeSlot.add(timeSlot);
    }

    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
                "roomNumber=" + roomNumber +
                ", timeSlotIds=" + timeSlot.stream().map(TimeSlot::getId).toList() +
                ", occupied=" + occupied +
                '}';
    }
}
