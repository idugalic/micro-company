package com.idugalic.common.event;

import java.io.Serializable;

/**
 * A base abstract event class.
 * 
 * @author idugalic
 *
 */
public abstract class AbstractEvent implements Serializable {

    private static final long serialVersionUID = -6658015030606619450L;

    private String id;

    public AbstractEvent() {

    }

    public AbstractEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}