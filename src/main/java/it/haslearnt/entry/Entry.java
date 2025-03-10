package it.haslearnt.entry;

import it.haslearnt.cassandra.mapping.EntityWithGeneratedId;
import it.haslearnt.cassandra.mapping.Column;
import it.haslearnt.cassandra.mapping.Entity;

@Entity("Entries")
public class Entry extends EntityWithGeneratedId {

	@Column("skill")
	private String skill;

	@Column("when")
	private String when;

	@Column("difficulty")
	private String difficulty;

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

}
