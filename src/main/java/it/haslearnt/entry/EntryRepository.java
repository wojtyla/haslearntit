package it.haslearnt.entry;

import it.haslearnt.cassandra.mapping.CassandraRepository;

import org.springframework.stereotype.Repository;

@Repository
public class EntryRepository extends CassandraRepository<Entry> {

}
