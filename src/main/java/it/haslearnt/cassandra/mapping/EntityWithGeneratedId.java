package it.haslearnt.cassandra.mapping;

import org.scale7.cassandra.pelops.UuidHelper;

public class EntityWithGeneratedId {

	@Id
	protected String id;

	public String id() {
		return id;
	}

	public void generateId() {
		this.id = UuidHelper.newTimeUuid().toString();
	}

	public EntityWithGeneratedId withId(String entryId) {
		this.id = entryId;
		return this;
	}

}
