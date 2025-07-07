package org.seti.bruebabacken.repository;

import org.seti.bruebabacken.model.Fondo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FondoRepository extends MongoRepository<Fondo, String> {

} 