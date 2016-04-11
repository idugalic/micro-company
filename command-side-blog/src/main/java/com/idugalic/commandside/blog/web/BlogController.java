package com.idugalic.commandside.blog.web;

import java.security.Principal;
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
@RequestMapping(value = "/blogposts")
public class BlogController {

	private static final Logger LOG = LoggerFactory.getLogger(BlogController.class);

	@Autowired
	private CommandGateway commandGateway;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void create(@RequestBody CreateBlogPostCommand command, HttpServletResponse response, Principal principal) {
		LOG.debug(CreateBlogPostCommand.class.getSimpleName() + " request received");
		//TODO Set who and when - Audit. Maybe this can be some intercepter 
		command.setAuditEntry(new AuditEntry(principal!=null?principal.getName():null));
		commandGateway.sendAndWait(command);
		LOG.debug(CreateBlogPostCommand.class.getSimpleName() + " sent to command gateway: Blog Post [{}] ", command.getId());
	}
	
	@RequestMapping(value = "/{id}/publishcommand", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void publish(@PathVariable String id, @RequestBody PublishBlogPostCommand command, HttpServletResponse response, Principal principal) {
		LOG.debug(PublishBlogPostCommand.class.getSimpleName() + " request received");
		//TODO Set who and when - Audit. Maybe this can be some intercepter 
		command.setAuditEntry(new AuditEntry(principal!=null?principal.getName():null));
		command.setId(id);
		commandGateway.sendAndWait(command);
		LOG.debug(PublishBlogPostCommand.class.getSimpleName() + " sent to command gateway: Blog Post [{}] ", command.getId());
	}

}
