package be.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    @NotNull(message = "id can't be null")
    private long id;
    private String eventName;
    private LocalDate startDate, endDate;
    private String location;
    private ArrayList<User> participants;
    private String extraInfo;
    private String picPath;

    public Event(){
        participants = new ArrayList<>();
    }

    public long getId() {
        return this.id;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void addParticipant(User user){
        if (user == null) throw new ModelException("user can't be null");
        participants.add(user);
    }

    public void removeParticipant(User user){
        if (user == null) throw new ModelException("user can't be null");
        participants.remove(user);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        if (eventName == null) throw new ModelException("eventName can't be null");
        if (eventName.isEmpty()) throw new ModelException("eventName can't be empty");
        this.eventName = eventName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) throw new ModelException("startDate can't be null");
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) throw new ModelException("endDate can't be null");
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null) throw new ModelException("location can't be null");
        if (location.isEmpty()) throw new ModelException("location can't be empty");
        this.location = location;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        if (extraInfo == null) throw new ModelException("extraInfo can't be null");
        if (extraInfo.isEmpty()) throw new ModelException("extraInfo can't be empty");
        this.extraInfo = extraInfo;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        if (picPath == null) throw new ModelException("picPath can't be null");
        if (picPath.isEmpty()) throw new ModelException("picPath can't be empty");
        if (!picPath.contains(".jpg") && !picPath.contains(".jpg")) {
            throw new ModelException("file must be in jpg or png format, flikker");
        }
        this.picPath = picPath;
    }

}
