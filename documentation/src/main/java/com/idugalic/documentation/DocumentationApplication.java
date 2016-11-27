package com.idugalic.documentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.Container;
import com.structurizr.model.InteractionStyle;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.ContainerView;
import com.structurizr.view.DynamicView;
import com.structurizr.view.Routing;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

/**
 * 
 * @author idugalic
 * 
 * Software architecture diagrams with structurizr.com
 * 
 * Static diagrams, whether drawn on a whiteboard or with a general purpose diagramming tool such as Microsoft Visio, tend to get out of date quickly and often don't reflect the structure of the code. 
 * On the other hand, automatically generated diagrams, such as UML class diagrams created by reverse-engineering the code, typically show far too much detail, limiting their usefulness.
 */
@SpringBootApplication
public class DocumentationApplication {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentationApplication.class);
    
  
    private static final Long WORKSPACE_ID = 22311L;
    
    private static final String MICROSERVICE_TAG = "Microservice";
    private static final String MESSAGE_BUS_TAG = "Message Bus";
    private static final String DATASTORE_TAG = "Database";
    
    /**
     * WORK IN PROGRESS (https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/core/MicroservicesExample.java)
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(DocumentationApplication.class);
        Environment env = app.run(args).getEnvironment();
       
        Workspace workspace = new Workspace("Micro Company", "An example of a microservices architecture, which includes asynchronous and parallel behaviour.");
        Model model = workspace.getModel();

        SoftwareSystem mySoftwareSystem = model.addSoftwareSystem("My Company Information System", "Stores information");
       
        Person user = model.addPerson("User", "A user");
        
        // ### API Gateway ###
        Container uiApplication = mySoftwareSystem.addContainer("UI Application - API Gateway", "Allows users to manage their profile, blogs and projects", "Angular");

        // ### Command side ###
        Container eventStoreDatabase = mySoftwareSystem.addContainer("Event Store", "Stores all events (evensourcing).", "MySQL");
        eventStoreDatabase.addTags(DATASTORE_TAG);
        
        Container blogCommandService = mySoftwareSystem.addContainer("Blog Command Service", "The point of access for blog mangement - command side.", "Java and Spring Boot");
        blogCommandService.addTags(MICROSERVICE_TAG);

        Container projectCommandService = mySoftwareSystem.addContainer("Project Command Service", "The point of access for project mangement - command side.", "Java and Spring Boot");
        projectCommandService.addTags(MICROSERVICE_TAG);
       
        //### Query side ###
        Container blogQueryService = mySoftwareSystem.addContainer("Blog Query Service", "The point of access for blog materialized views - query side.", "Java and Spring Boot");
        blogQueryService.addTags(MICROSERVICE_TAG);

        Container projectQueryService = mySoftwareSystem.addContainer("Project Query Service", "The point of access for project materialized views - query side.", "Java and Spring Boot");
        projectQueryService.addTags(MICROSERVICE_TAG);
        
        Container blogQueryStore = mySoftwareSystem.addContainer("Blog Query Store", "Stores information about Blog posts - materialized view", "MySQL");
        blogQueryStore.addTags(DATASTORE_TAG);
        
        Container projectQueryStore = mySoftwareSystem.addContainer("Project Query Store", "Stores information about Projects - materialized view", "MySQL");
        projectQueryStore.addTags(DATASTORE_TAG);

        // ### Bus ###
        Container messageBus = mySoftwareSystem.addContainer("Message Bus", "Transport for business events.", "RabbitMQ");
        messageBus.addTags(MESSAGE_BUS_TAG);

        // ##### Relations ####
        user.uses(mySoftwareSystem, "Uses");
        user.uses(uiApplication, "Uses");
       
        // # Blog
        uiApplication.uses(blogCommandService, "Creates & publish blog posts", "JSON/HTTPS", InteractionStyle.Synchronous);
        blogCommandService.uses(eventStoreDatabase, "Stores blog events in", "JDBC", InteractionStyle.Synchronous);
        eventStoreDatabase.uses(messageBus, "Sends/Tails all events to", "", InteractionStyle.Asynchronous);
        messageBus.uses(blogQueryService, "Sends blog events to", "", InteractionStyle.Asynchronous);
        blogQueryService.uses(blogQueryStore, "Stores blog data in", "", InteractionStyle.Synchronous);
        uiApplication.uses(blogQueryService, "Read & search blog posts", "JSON/HTTPS", InteractionStyle.Synchronous);

        //# Project
        uiApplication.uses(projectCommandService, "Creates & edit projects", "JSON/HTTPS", InteractionStyle.Synchronous);
        projectCommandService.uses(eventStoreDatabase, "Stores project events in", "JDBC", InteractionStyle.Synchronous);
        eventStoreDatabase.uses(messageBus, "Sends/Tails all events to", "", InteractionStyle.Asynchronous);
        messageBus.uses(projectQueryService, "Sends project events to", "", InteractionStyle.Asynchronous);
        projectQueryService.uses(projectQueryStore, "Stores project data in", "", InteractionStyle.Synchronous);
        uiApplication.uses(projectQueryService, "Read & search projects", "JSON/HTTPS", InteractionStyle.Synchronous);

        messageBus.uses(uiApplication, "Sends all events to", "WebSocket", InteractionStyle.Asynchronous);

        //Create views
        ViewSet views = workspace.getViews();
        
        SystemContextView contextView = views.createSystemContextView(mySoftwareSystem, "Context", "The System Context diagram for the 'micro-company' application");
        contextView.addAllElements();

        ContainerView containerView = views.createContainerView(mySoftwareSystem, "Containers", null);
        containerView.addAllElements();
        
        // ### Dynamic view - Create/Publish Blog post ###
        DynamicView dynamicViewCreateBlog = views.createDynamicView(mySoftwareSystem, "Create Blog/Publish post", "This diagram shows what happens when a user creates/publishes a blog post.");
        dynamicViewCreateBlog.add(user, uiApplication);
        dynamicViewCreateBlog.add(uiApplication, blogCommandService);
        dynamicViewCreateBlog.add(blogCommandService, eventStoreDatabase);
        dynamicViewCreateBlog.add(eventStoreDatabase, messageBus);

        dynamicViewCreateBlog.startParallelSequence();
        dynamicViewCreateBlog.add(messageBus, blogQueryService);
        dynamicViewCreateBlog.add(messageBus, uiApplication);
        dynamicViewCreateBlog.endParallelSequence();

        dynamicViewCreateBlog.add(blogQueryService, blogQueryStore);
        
        // ### Dynamic view - Query Blog posts ###
        DynamicView dynamicViewQueryBlog = views.createDynamicView(mySoftwareSystem, "Query Blog post", "This diagram shows what happens when a user query a blog post/s.");
        dynamicViewQueryBlog.add(user, uiApplication);
        dynamicViewQueryBlog.add(uiApplication, "Consume blog rest API", blogQueryService);
        dynamicViewQueryBlog.add(blogQueryService, "Query the blog store", blogQueryStore);

        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).color("#000000");
        styles.addElementStyle(Tags.PERSON).background("#ffbf00").shape(Shape.Person);
        styles.addElementStyle(Tags.CONTAINER).background("#facc2E");
        styles.addElementStyle(MESSAGE_BUS_TAG).width(1600).shape(Shape.RoundedBox);
        styles.addElementStyle(MICROSERVICE_TAG).shape(Shape.Hexagon);
        styles.addElementStyle(DATASTORE_TAG).background("#f5da81").shape(Shape.Cylinder);
        styles.addRelationshipStyle(Tags.RELATIONSHIP).routing(Routing.Orthogonal);

        styles.addRelationshipStyle(Tags.ASYNCHRONOUS).dashed(true);
        styles.addRelationshipStyle(Tags.SYNCHRONOUS).dashed(false);

        uploadWorkspaceToStructurizr(workspace, WORKSPACE_ID, env.getProperty("spring.application.structurizr.apikey"), env.getProperty("spring.application.structurizr.apisecret"));
    }
    
    private static void uploadWorkspaceToStructurizr(Workspace workspace, Long workspaceId, String apiKey, String apiSecret) throws Exception {
        LOG.info("### Structurizr api Key: " + apiKey);
        LOG.info("### Structurizr api Secret: " + apiSecret);
        
        StructurizrClient structurizrClient = new StructurizrClient(apiKey, apiSecret);
        structurizrClient.setMergeFromRemote(true);
        structurizrClient.putWorkspace(workspaceId, workspace);
    }
    
    

}
