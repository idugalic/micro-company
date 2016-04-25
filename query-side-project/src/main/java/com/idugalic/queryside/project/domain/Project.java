package com.idugalic.queryside.project.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.idugalic.common.project.event.ProjectCreatedEvent;

@Entity
public class Project {
	@Id
	private String id;
	
	public Project() {
		
	}
	
	public Project(String id) {
		super();
		this.id = id;
	}
	
	public Project(ProjectCreatedEvent event) {
		super();
		this.id = event.getId();
	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}
