package eu.ubitech.sma.repository.dao;

import eu.ubitech.sma.repository.domain.Individual;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Christos Paraskeva
 */
@Repository
public interface IndividualDAO extends MongoRepository<Individual,BigDecimal>{
    
    
    public List<Individual> findByScreenName(String screenName);
    
}
