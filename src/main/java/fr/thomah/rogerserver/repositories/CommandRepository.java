package fr.thomah.rogerserver.repositories;

import fr.thomah.rogerserver.entities.Command;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommandRepository extends MongoRepository<Command, String> {

}
