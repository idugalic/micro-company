package com.idugalic.queryside.blog.handler;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.event.BlogPostPublishedEvent;
import com.idugalic.queryside.blog.domain.BlogPost;
import com.idugalic.queryside.blog.repository.BlogPostRepository;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.SequenceNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ProcessingGroup("default")
@Component
public class BlogPostViewEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BlogPostViewEventHandler.class);

    @Autowired
    private BlogPostRepository blogPostRepository;

    @EventHandler
    public void handle(BlogPostCreatedEvent event, @SequenceNumber Long version) {
        LOG.info("BlogPostCreatedEvent: [{}] ", event.getId());
        blogPostRepository.save(new BlogPost(event, version));
    }

    @EventHandler
    public void handle(BlogPostPublishedEvent event, @SequenceNumber Long version) {
        LOG.info("BlogPostCreatedEvent: [{}] ", event.getId());
        BlogPost post = blogPostRepository.findOne(event.getId());
        post.setDraft(false);
        post.setPublishAt(event.getPublishAt());
        post.setVersion(version);
        blogPostRepository.save(post);
    }
}
