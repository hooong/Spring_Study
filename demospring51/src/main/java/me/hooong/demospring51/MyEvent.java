package me.hooong.demospring51;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;


public class MyEvent {

    private int data;

    private Object source;

    public MyEvent(Object source, int data) {
        this.source = source;
        this.data = data;
    }

    public MyEvent(Object source) {
        this.source = source;
    }

    public int getData() {
        return data;
    }
}
