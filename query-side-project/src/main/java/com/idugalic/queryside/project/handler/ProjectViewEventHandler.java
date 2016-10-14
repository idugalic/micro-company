package com.idugalic.queryside.project.handler;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventhandling.annotation.SequenceNumber;
import org.axonframework.eventhandling.replay.ReplayAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idugalic.common.project.event.ProjectCreatedEvent;
import com.idugalic.queryside.project.domain.Project;
import com.idugalic.queryside.project.repository.ProjectRepository;

@Component
public class ProjectViewEventHandler implements ReplayAware {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectViewEventHandler.class);

    @Autowired
    private ProjectRepository projectRepository;

    @EventHandler
    public void handle(ProjectCreatedEvent event, @SequenceNumber Long version) {
        LOG.info("ProjectCreatedEvent: [{}] ", event.getId());
        projectRepository.save(new Project(event, version));
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
