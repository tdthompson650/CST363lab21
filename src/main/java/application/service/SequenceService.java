package application.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import application.model.DatabaseSequence;

@Service
public class SequenceService {
	
	@Autowired
	MongoOperations mongoOperations;

	public int getNextSequence(String sequenceName) {
		DatabaseSequence counter = 
				mongoOperations.findAndModify(
					query(where("_id").is(sequenceName)),
					new Update().inc("seq",1), options().returnNew(true).upsert(true),
					DatabaseSequence.class);
		return counter.getSeq();
	}

}
