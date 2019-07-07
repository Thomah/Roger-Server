package fr.thomah.rogerserver.repositories;

import fr.thomah.rogerserver.entities.FileData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileDataRepository extends MongoRepository<FileData, String> {

    List<FileData> findByIsSync(boolean isSync);



}
