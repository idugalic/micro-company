package com.idugalic.commandside.project.aggregate;

import com.idugalic.commandside.project.command.CreateProjectCommand;
import com.idugalic.commandside.project.command.UpdateProjectCommand;
import com.idugalic.common.model.AuditEntry;
import com.idugalic.common.project.event.ProjectCreatedEvent;
import com.idugalic.common.project.event.ProjectUpdatedEvent;

import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.messaging.interceptors.JSR303ViolationException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

/**
 * Domain (aggregate) test for {@link ProjectAggregate}.
 * 
 * @author idugalic
 *
 */
public class BlogPostAggregateTest {

    private FixtureConfiguration<ProjectAggregate> fixture;
    private AuditEntry auditEntry;
    private static final String WHO = "idugalic";

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<ProjectAggregate>(ProjectAggregate.class);
        fixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor());
        auditEntry = new AuditEntry(WHO);
    }

    @Test
    public void createBlogPostTest() throws Exception {
        CreateProjectCommand command = new CreateProjectCommand(auditEntry, "name", "repoURL", "siteURL", "category", "description");
        fixture.given().when(command).expectEvents(new ProjectCreatedEvent(command.getId(), auditEntry, "name", "repoURL", "siteURL", "category", "description"));
    }
    
    @Test(expected = JSR303ViolationException.class)
    public void createBlogPostWithWrongNameTest() throws Exception {
        CreateProjectCommand command = new CreateProjectCommand(auditEntry, null, "repoURL", "siteURL", "category", "description");
        fixture.given().when(command).expectEvents(new ProjectCreatedEvent(command.getId(), auditEntry, "name", "repoURL", "siteURL", "category", "description"));
    }

    @Test
    public void updateBlogPostTest() throws Exception {
        UpdateProjectCommand command = new UpdateProjectCommand("id", auditEntry, "name2", "repoURL2", "siteURL2", "description2");
        fixture.given(new ProjectCreatedEvent(command.getId(), command.getAuditEntry(), "name", "repoURL", "siteURL", "category", "description")).when(command).expectEvents(
                new ProjectUpdatedEvent(command.getId(), auditEntry, command.getName(), command.getRepoUrl(), command.getSiteUrl(), command.getDescription()));
    }

}
