package com.idugalic.commandside.project.web;

import com.idugalic.commandside.project.command.CreateProjectCommand;

import java.nio.charset.Charset;
import java.util.HashSet;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.interceptors.JSR303ViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MVC test for {@link ProjectController}
 * 
 * @author idugalic 
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(secure=false, controllers=ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private JacksonTester<CreateProjectRequest> jsonCreate;
    
    @MockBean
    private CommandGateway commandGateway;
    
    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper(); 
        // Possibly configure the mapper
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void createProjectWithValidationErrorTest() throws Exception {
        
        CreateProjectRequest request = new CreateProjectRequest();
        given(this.commandGateway.sendAndWait(any(CreateProjectCommand.class))).willThrow(new CommandExecutionException("Test", new JSR303ViolationException(new HashSet())));
        
        this.mvc.perform(post("/api/projectcommands")
                .content(this.jsonCreate.write(request).getJson())
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void createProjectWithSuccessTest() throws Exception {
        
        CreateProjectRequest request = new CreateProjectRequest();
        given(this.commandGateway.sendAndWait(any(CreateProjectCommand.class))).willReturn("Success");
        
        this.mvc.perform(post("/api/projectcommands")
                .content(this.jsonCreate.write(request).getJson())
                .contentType(contentType))
                .andExpect(status().isCreated());
    }
   

}
