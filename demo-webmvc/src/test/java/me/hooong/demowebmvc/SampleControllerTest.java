package me.hooong.demowebmvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void eventForm() throws Exception {
        mockMvc.perform(get("/events/form"))
                .andDo(print())
                .andExpect(view().name("/events/form"))
                .andExpect(model().attributeExists("event"))
                .andExpect(request().sessionAttribute("event",notNullValue()));
    }

    @Test
    public void deleteEvent() throws Exception {
        mockMvc.perform(post("/events?name=hooong&limit=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("hooong"))
                .andExpect(jsonPath("limit").value(4));
    }

    @Test
    public void postEvent() throws Exception {
        mockMvc.perform(post("/events")
            .param("name","hooong")
            .param("limit","-10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors());
    }
}