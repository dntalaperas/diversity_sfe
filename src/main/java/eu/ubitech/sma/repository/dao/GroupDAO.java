package eu.ubitech.sma.repository.dao;

import eu.ubitech.sma.repository.domain.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Christos Paraskeva
 */
@Repository

public interface GroupDAO extends MongoRepository<Group, String> {

    public Group findOneByProfileOrderByCreatedDateDesc(String pid);

}
