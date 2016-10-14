package com.idugalic.common.command;

import com.idugalic.common.model.AuditEntry;

public class AuditableAbstractCommand {

    private AuditEntry auditEntry;

    public AuditableAbstractCommand(AuditEntry auditEntry) {
        this.setAuditEntry(auditEntry);
    }

    public AuditableAbstractCommand() {
    }

    public AuditEntry getAuditEntry() {
        return auditEntry;
    }

    public void setAuditEntry(AuditEntry auditEntry) {
        this.auditEntry = auditEntry;
    }

}
