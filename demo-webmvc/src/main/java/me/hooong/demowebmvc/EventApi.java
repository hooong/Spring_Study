package me.hooong.demowebmvc;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventApi {

    @ExceptionHandler
    public ResponseEntity errorHandler() {
        return ResponseEntity.badRequest().body("can't create event as ...");
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody @Valid Event event, BindingResult bindingResult) {
        // save event to DB
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

//        return ResponseEntity.ok(event);
        return new ResponseEntity<Event>(event,HttpStatus.CREATED);
    }
}
