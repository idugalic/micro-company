package com.idugalic.commandside.blog.web;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.idugalic.commandside.blog.command.CreateBlogPostCommand;
import com.idugalic.commandside.blog.command.PublishBlogPostCommand;
import com.idugalic.common.model.AuditEntry;

/**
 * A web controller for managing {@link BlogPostAggregate} - create/update only.
 * 
 * @author idugalic
 *
 */
@RestController
@RequestMapping(value = "/blogpostcommands")
public class BlogController {

    private static final Logger LOG = LoggerFactory.getLogger(BlogController.class);

    private String getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
        return null;
    }

    private AuditEntry createAudit() {
        return new AuditEntry(getCurrentUser());
    }

    @Autowired
    private CommandGateway commandGateway;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@RequestBody CreateBlogPostRequest request, HttpServletResponse response, Principal principal) {
        LOG.debug(CreateBlogPostRequest.class.getSimpleName() + " request received");

        CreateBlogPostCommand command = new CreateBlogPostCommand(createAudit(), request.getTitle(),
                request.getRawContent(), request.getPublicSlug(), request.getDraft(), request.getBroadcast(),
                request.getPublishAt(), request.getCategory(), getCurrentUser());
        commandGateway.sendAndWait(command);
        LOG.debug(CreateBlogPostCommand.class.getSimpleName() + " sent to command gateway: Blog Post [{}] ", command.getId());
    }

    @RequestMapping(value = "/{id}/publishcommand", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void publish(@PathVariable String id, @RequestBody PublishBlogPostRequest request, HttpServletResponse response, Principal principal) {
        LOG.debug(PublishBlogPostRequest.class.getSimpleName() + " request received");

        PublishBlogPostCommand command = new PublishBlogPostCommand(id, createAudit(), request.getPublishAt());
        commandGateway.sendAndWait(command);
        LOG.debug(PublishBlogPostCommand.class.getSimpleName() + " sent to command gateway: Blog Post [{}] ", command.getId());
    }

}
