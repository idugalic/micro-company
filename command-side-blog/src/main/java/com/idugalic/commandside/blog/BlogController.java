package com.idugalic.commandside.blog;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.repository.ConcurrencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.idugalic.commandside.blog.command.CreateBlogPostCommand;

@RestController
@RequestMapping(value = "/blog")
public class BlogController {

	private static final Logger LOG = LoggerFactory.getLogger(BlogController.class);

	@Autowired
	private CommandGateway commandGateway;

	@RequestMapping(method = RequestMethod.GET)
	public String greeting(Principal principal) {
		return "Hello. You are in command !!!";
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void create(@RequestBody CreateBlogPostCommand request, HttpServletResponse response) {
		LOG.debug(CreateBlogPostCommand.class.getSimpleName() + " request received: ID: {}}", request.getId());
		try {
			commandGateway.sendAndWait(request);
			LOG.debug(CreateBlogPostCommand.class.getSimpleName() + " sent to command gateway: Blog Post [{}] ", request.getId()); 
			response.setStatus(HttpServletResponse.SC_CREATED);
			return;
		} catch (CommandExecutionException cex) {
			LOG.warn(CreateBlogPostCommand.class.getSimpleName() + " FAILED. Unable to execute the command. Message: {}", cex.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			if (null != cex.getCause()) {
				LOG.warn("CAUSED BY: {} {}", cex.getCause().getClass().getName(), cex.getCause().getMessage());
				if (cex.getCause() instanceof ConcurrencyException) {
					LOG.warn("ISSUE: A Blog Post with ID [{}] already exists.", request.getId());
					response.setStatus(HttpServletResponse.SC_CONFLICT);
				}
			}
		}
	}

}
