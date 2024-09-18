package com.idugalic.configuration;

import java.lang.reflect.Method;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.idugalic.queryside.blog.domain.BlogPost;
import com.idugalic.queryside.project.domain.Project;

/**
 * A configuration of rest data respositories for {@link BlogPost} and {@link Project}.
 * 
 * @author idugalic
 *
 */
@Configuration
public class RestConfiguration extends RepositoryRestMvcConfiguration {

	public RestConfiguration(final ApplicationContext context, final ObjectFactory<ConversionService> conversionService) {
		super(context, conversionService);
	}

	@Bean
	public WebMvcRegistrations webMvcRegistrationsHandlerMapping() {
		return new WebMvcRegistrations() {
			@Override
			public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
				return new RequestMappingHandlerMapping() {
					private final static String API_BASE_PATH = "api";

					@Override
					protected void registerHandlerMethod(final Object handler, final Method method, RequestMappingInfo mapping) {
						final Class<?> beanType = method.getDeclaringClass();
						if (AnnotationUtils.findAnnotation(beanType, RestController.class) != null) {
							final PatternsRequestCondition apiPattern = new PatternsRequestCondition(API_BASE_PATH)
									.combine(mapping.getPatternsCondition());

							mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
									mapping.getMethodsCondition(), mapping.getParamsCondition(),
									mapping.getHeadersCondition(), mapping.getConsumesCondition(),
									mapping.getProducesCondition(), mapping.getCustomCondition());
						}

						super.registerHandlerMethod(handler, method, mapping);
					}
				};
			}
		};
	}

	@Configuration
	static class RestConfigurationExposeId extends RepositoryRestConfigurerAdapter {
		@Override
		public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config) {
			config.exposeIdsFor(BlogPost.class, Project.class);
			config.setBasePath("/api");
		}
	}

}
