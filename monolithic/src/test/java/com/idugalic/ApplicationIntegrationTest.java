package com.idugalic;

import com.idugalic.commandside.blog.web.CreateBlogPostRequest;
import com.idugalic.commandside.project.web.CreateProjectRequest;
import com.idugalic.common.blog.model.BlogPostCategory;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link Application} starting on a random port.
 * 
 * @author idugalic
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private CreateBlogPostRequest createBlogPostRequest;
    private CreateProjectRequest createProjectRequest;
    private Calendar future;


    @Before
    public void setup() {
        future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, 1);
        
        createBlogPostRequest = new CreateBlogPostRequest();
        createBlogPostRequest.setTitle("title");
        createBlogPostRequest.setCategory(BlogPostCategory.ENGINEERING);
        createBlogPostRequest.setBroadcast(Boolean.FALSE);
        createBlogPostRequest.setDraft(Boolean.TRUE);
        createBlogPostRequest.setPublicSlug("publicurl");
        createBlogPostRequest.setRawContent("rawContent");
        createBlogPostRequest.setPublishAt(future.getTime());
        
        createProjectRequest = new CreateProjectRequest();
        createProjectRequest.setCategory("category");
        createProjectRequest.setDescription("description");
        createProjectRequest.setName("name");
        createProjectRequest.setRepoUrl("repoUrl");
        createProjectRequest.setSiteUrl("siteUrl");
    }

    @Test
    public void createBlogPostWithSuccess() throws Exception {

        HttpEntity<CreateBlogPostRequest> request = new HttpEntity<>(createBlogPostRequest);

        ResponseEntity<String> response = restTemplate.exchange("/api/blogpostcommands", HttpMethod.POST, request, String.class);
        System.out.println("################ "+response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
    
    @Test
    public void createProjectWithSuccess() throws Exception {

        HttpEntity<CreateProjectRequest> request = new HttpEntity<>(createProjectRequest);

        ResponseEntity<String> response = restTemplate.exchange("/api/projectcommands", HttpMethod.POST, request, String.class);
        System.out.println("################ "+response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }
    
    @Test
    public void createBlogPostWithValidationErrorTitleIsMandatory() throws Exception {

        createBlogPostRequest.setTitle(null);
        HttpEntity<CreateBlogPostRequest> request = new HttpEntity<>(createBlogPostRequest);

        ResponseEntity<String> response = restTemplate.exchange("/api/blogpostcommands", HttpMethod.POST, request, String.class);
        System.out.println("################ "+response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
    
    @Test
    public void createProjectWithValidationErrorNameIsMandatory() throws Exception {
        
        createProjectRequest.setName(null);
        HttpEntity<CreateProjectRequest> request = new HttpEntity<>(createProjectRequest);

        ResponseEntity<String> response = restTemplate.exchange("/api/projectcommands", HttpMethod.POST, request, String.class);
        System.out.println("################ "+response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

}
