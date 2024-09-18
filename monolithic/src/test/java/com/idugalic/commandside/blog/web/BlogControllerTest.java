package com.idugalic.commandside.blog.web;

import com.idugalic.commandside.blog.command.CreateBlogPostCommand;

import java.nio.charset.Charset;
import java.util.HashSet;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.interceptors.JSR303ViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MVC test for {@link BlogController}
 * 
 * @author idugalic
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(secure=false, controllers=BlogController.class)
public class BlogControllerTest {

    @Autowired
    private MockMvc mvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private JacksonTester<CreateBlogPostRequest> jsonCreate;
    
    @MockBean
    private CommandGateway commandGateway;
    
    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper(); 
        // Possibly configure the mapper
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void createBlogPostWithValidationErrorTest() throws Exception {
        
        CreateBlogPostRequest request = new CreateBlogPostRequest();
        given(this.commandGateway.sendAndWait(Mockito.any(CreateBlogPostCommand.class))).willThrow(new CommandExecutionException("Test", new JSR303ViolationException(new HashSet())));
        
        this.mvc.perform(post("/api/blogpostcommands")
                .content(this.jsonCreate.write(request).getJson())
                .contentType(contentType))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void createBlogPostWithSuccessTest() throws Exception {
        
        CreateBlogPostRequest request = new CreateBlogPostRequest();
        given(this.commandGateway.sendAndWait(Mockito.any(CreateBlogPostCommand.class))).willReturn("Success");
        
        this.mvc.perform(post("/api/blogpostcommands")
                .content(this.jsonCreate.write(request).getJson())
                .contentType(contentType))
                .andExpect(status().isCreated());
    }
   

}
