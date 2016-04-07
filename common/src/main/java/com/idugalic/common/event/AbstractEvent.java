package com.idugalic.common.event;

import java.io.Serializable;

public abstract class AbstractEvent implements Serializable {

	private static final long serialVersionUID = -6658015030606619450L;
	
	private String id;

	public AbstractEvent(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}