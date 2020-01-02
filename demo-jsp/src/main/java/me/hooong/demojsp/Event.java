package me.hooong.demojsp;

import java.time.LocalDateTime;

public class Event {

    private String name;

    private LocalDateTime starts;

    public LocalDateTime getStarts() {
        return starts;
    }

    public void setStarts(LocalDateTime starts) {
        this.starts = starts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
