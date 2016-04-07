package com.idugalic.commandside.blog;
import java.security.Principal;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value ="/blog")
public class BlogController {

	@Autowired
    private CommandGateway commandGateway;
	
    @RequestMapping(method = RequestMethod.GET)
    public String greeting(Principal principal) {
        return "Hello "+principal.getName() +". You are in command !!!";
    }

}
