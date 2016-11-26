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
        
        Container customerApplication = mySoftwareSystem.addContainer("Customer Application", "Allows customers to manage their profile.", "Angular");

        Container customerService = mySoftwareSystem.addContainer("Customer Service", "The point of access for customer information.", "Java and Spring Boot");
        customerService.addTags(MICROSERVICE_TAG);
       
        Container customerDatabase = mySoftwareSystem.addContainer("Customer Database", "Stores customer information.", "Oracle 12c");
        customerDatabase.addTags(DATASTORE_TAG);

        Container reportingService = mySoftwareSystem.addContainer("Reporting Service", "Creates normalised data for reporting purposes.", "Ruby");
        reportingService.addTags(MICROSERVICE_TAG);
       
        Container reportingDatabase = mySoftwareSystem.addContainer("Reporting Database", "Stores a normalised version of all business data for ad hoc reporting purposes.", "MySQL");
        reportingDatabase.addTags(DATASTORE_TAG);

        Container auditService = mySoftwareSystem.addContainer("Audit Service", "Provides organisation-wide auditing facilities.", "C# .NET");
        auditService.addTags(MICROSERVICE_TAG);
       
        Container auditStore = mySoftwareSystem.addContainer("Audit Store", "Stores information about events that have happened.", "Event Store");
        auditStore.addTags(DATASTORE_TAG);

        Container messageBus = mySoftwareSystem.addContainer("Message Bus", "Transport for business events.", "RabbitMQ");
        messageBus.addTags(MESSAGE_BUS_TAG);

        user.uses(mySoftwareSystem, "Uses");
        user.uses(customerApplication, "Uses");
        customerApplication.uses(customerService, "Updates customer information using", "JSON/HTTPS", InteractionStyle.Synchronous);
        customerService.uses(messageBus, "Sends customer update events to", "", InteractionStyle.Asynchronous);
        customerService.uses(customerDatabase, "Stores data in", "JDBC", InteractionStyle.Synchronous);
        customerService.uses(customerApplication, "Sends events to", "WebSocket", InteractionStyle.Asynchronous);
        messageBus.uses(reportingService, "Sends customer update events to", "", InteractionStyle.Asynchronous);
        messageBus.uses(auditService, "Sends customer update events to", "", InteractionStyle.Asynchronous);
        reportingService.uses(reportingDatabase, "Stores data in", "", InteractionStyle.Synchronous);
        auditService.uses(auditStore, "Stores events in", "", InteractionStyle.Synchronous);

        //create some views
        ViewSet views = workspace.getViews();
        
        SystemContextView contextView = views.createSystemContextView(mySoftwareSystem, "Context", "The System Context diagram for the 'micro-company' application");
        contextView.addAllElements();

        ContainerView containerView = views.createContainerView(mySoftwareSystem, "Containers", null);
        containerView.addAllElements();

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
