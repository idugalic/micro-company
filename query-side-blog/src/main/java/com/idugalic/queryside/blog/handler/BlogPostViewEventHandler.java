package com.idugalic.queryside.blog.handler;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventhandling.annotation.SequenceNumber;
import org.axonframework.eventhandling.replay.ReplayAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.event.BlogPostPublishedEvent;
import com.idugalic.queryside.blog.domain.BlogPost;
import com.idugalic.queryside.blog.repository.BlogPostRepository;

@Component
public class BlogPostViewEventHandler implements ReplayAware {

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

    public void beforeReplay() {
        LOG.info("Event Replay is about to START. Clearing the View...");
    }

    public void afterReplay() {
        LOG.info("Event Replay has FINISHED.");
    }

    public void onReplayFailed(Throwable cause) {
        LOG.error("Event Replay has FAILED.");
    }
}
