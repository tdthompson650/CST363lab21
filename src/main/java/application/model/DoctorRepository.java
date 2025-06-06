package application.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRepository extends MongoRepository<Doctor, Integer> {
	
	Doctor findByIdAndLastName(int id, String lastName);
	Doctor findByIdAndFirstNameAndLastName(int id, String firstName, String lastName);
	Doctor findByLastName(String lastName);
	Doctor findById(int id);

}
