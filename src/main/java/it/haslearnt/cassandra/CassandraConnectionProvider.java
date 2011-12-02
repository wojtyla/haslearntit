/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.cassandra;

import org.scale7.cassandra.pelops.spring.CommonsBackedPoolFactoryBean;

public class CassandraConnectionProvider extends CommonsBackedPoolFactoryBean {

	private boolean clean = false;

	@Override
	public void afterPropertiesSet() throws Exception {
		CassandraKeyspaceManager.ensureKeyspace(getCluster(), getKeyspace());
		if (clean) {
			CassandraColumnFamilyManager.recreateColumnFamily(getCluster(), getKeyspace(), "Notes", "LongType");
			CassandraColumnFamilyManager.recreateColumnFamily(getCluster(), getKeyspace(), "Entries", "UTF8Type");
			CassandraColumnFamilyManager.recreateColumnFamily(getCluster(), getKeyspace(), "Users", "UTF8Type");
		} else {
			CassandraColumnFamilyManager.addColumnFamilyIfNeeded(getCluster(), getKeyspace(), "Notes", "LongType");
			CassandraColumnFamilyManager.addColumnFamilyIfNeeded(getCluster(), getKeyspace(), "Entries", "UTF8Type");
			CassandraColumnFamilyManager.addColumnFamilyIfNeeded(getCluster(), getKeyspace(), "Users", "UTF8Type");
		}
		super.afterPropertiesSet();
	}

	public void setClean(boolean clean) {
		this.clean = clean;
	}
}
