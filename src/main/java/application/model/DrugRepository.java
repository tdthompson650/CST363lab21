package application.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DrugRepository extends MongoRepository<Drug, Integer> {
	
	Drug findByName(String name);

}
