package com.idugalic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import com.idugalic.queryside.blog.domain.BlogPost;
import com.idugalic.queryside.project.domain.Project;

@Configuration
public class RestConfiguration extends RepositoryRestMvcConfiguration {

	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(BlogPost.class);
		config.exposeIdsFor(Project.class);
	}
}
