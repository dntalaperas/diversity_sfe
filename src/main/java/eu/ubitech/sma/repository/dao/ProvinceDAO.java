package eu.ubitech.sma.repository.dao;

import eu.ubitech.sma.repository.domain.Province;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Christos Paraskeva
 */
@Repository
public interface ProvinceDAO extends MongoRepository<Province, String> {

}
