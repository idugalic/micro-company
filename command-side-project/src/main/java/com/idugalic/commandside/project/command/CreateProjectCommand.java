package com.idugalic.commandside.project.command;

import java.util.UUID;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idugalic.common.command.AuditableAbstractCommand;
import com.idugalic.common.model.AuditEntry;

public class CreateProjectCommand extends AuditableAbstractCommand {

	@JsonIgnore
	@TargetAggregateIdentifier
	private String id;
	
	public CreateProjectCommand(AuditEntry auditEntry) {
		this.id = UUID.randomUUID().toString();
		this.setAuditEntry(auditEntry);
	}

	public String getId() {
		return id;
	}
}
