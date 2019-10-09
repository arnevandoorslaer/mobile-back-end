package be.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Event {

    private String eventName;
    private LocalDate startDate, endDate;
    private String location;
    private List<User> participants;
    private String extraInfo;
    private String picPath;

    public Event(){
        participants = new ArrayList<>();
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void addParticipant(User user){
        participants.add(user);
    }

    public void removeParticipant(User user){
        participants.remove(user);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
