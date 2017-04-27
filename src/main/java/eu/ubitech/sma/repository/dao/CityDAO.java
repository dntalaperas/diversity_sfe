package eu.ubitech.sma.repository.dao;

import eu.ubitech.sma.repository.domain.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Christos Paraskeva
 */
@Repository
public interface CityDAO extends MongoRepository<City, String> {

}
