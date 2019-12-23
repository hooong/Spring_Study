package me.hooong.demospring51;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MyEventHanddler {
    @EventListener
    @Async
    public void onApplicationEvent(MyEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("이벤트 받았다. 데이터는" + myEvent.getData());
    }

    @EventListener
    @Async
    public void onApplicationEvent(ContextRefreshedEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("ContextRefreshedEvent");
    }

    @EventListener
    @Async
    public void onApplicationEvent(ContextClosedEvent myEvent) {
        System.out.println(Thread.currentThread().toString());
        System.out.println("ContextClosedEvent");
    }
}
