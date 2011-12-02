package it.haslearnt.entry;

import org.scale7.cassandra.pelops.UuidHelper;

public class Entry {

	private String skill;
	private String id;
	private String when;
	private String difficulty;

	public String id() {
		return id;
	}

	public String what() {
		return skill;
	}

	public String when() {
		return when;
	}

	public String howDifficult() {
		return difficulty;
	}

	public Entry iveLearnt(String skill) {
		this.skill = skill;
		return this;
	}

	public Entry today() {
		this.when = "today";
		return this;
	}

	public Entry andItWas(String difficulty) {
		this.difficulty = difficulty;
		return this;
	}

	public void generateId() {
		this.id = UuidHelper.newTimeUuid().toString();
	}

}
