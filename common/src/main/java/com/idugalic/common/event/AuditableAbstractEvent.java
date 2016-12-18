package com.idugalic.common.event;

import com.idugalic.common.model.AuditEntry;

/**
 * A base, auditable event class.
 * 
 * @author idugalic
 *
 */
public abstract class AuditableAbstractEvent extends AbstractEvent {

    private static final long serialVersionUID = -5389550139760061559L;

    private AuditEntry auditEntry;

    public AuditableAbstractEvent() {

    }

    public AuditableAbstractEvent(String id, AuditEntry auditEntry) {
        super(id);
        this.setAuditEntry(auditEntry);
    }

    public AuditEntry getAuditEntry() {
        return auditEntry;
    }

    public void setAuditEntry(AuditEntry auditEntry) {
        this.auditEntry = auditEntry;
    }

}
