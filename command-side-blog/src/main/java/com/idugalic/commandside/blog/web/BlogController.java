package com.idugalic.commandside.blog.web;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.idugalic.commandside.blog.command.CreateBlogPostCommand;
import com.idugalic.commandside.blog.command.PublishBlogPostCommand;
import com.idugalic.common.model.AuditEntry;



@RestController
@RequestMapping(value = "/blog")
public class BlogController {

	private static final Logger LOG = LoggerFactory.getLogger(BlogController.class);

	@Autowired
	private CommandGateway commandGateway;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody CreateBlogPostForm request, HttpServletResponse response) {
		LOG.debug(CreateBlogPostForm.class.getSimpleName() + " request received");
		String id = UUID.randomUUID().toString();
	
		//TODO Replace who with Principal
		commandGateway.sendAndWait(new CreateBlogPostCommand(id, new AuditEntry("who", new Date()), request.getTitle(), request.getRawContent(), request.getPublicSlug(), request.getDraft(), request.getBroadcast(), request.getPublishAt(), request.getCategory()));
		LOG.debug(CreateBlogPostCommand.class.getSimpleName() + " sent to command gateway: Blog Post [{}] ", id);
	}
	
	@RequestMapping(value = "/{id}/publishcommand", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void publish(@PathVariable String id, @RequestBody PublishBlogPostForm request, HttpServletResponse response) {
		LOG.debug(PublishBlogPostForm.class.getSimpleName() + " request received");
		
		//TODO Replace who with Principal
		commandGateway.sendAndWait(new PublishBlogPostCommand(new AuditEntry("who", new Date()), id, request.getPublishAt()));
		LOG.debug(PublishBlogPostCommand.class.getSimpleName() + " sent to command gateway: Blog Post [{}] ", id);
	}

}
