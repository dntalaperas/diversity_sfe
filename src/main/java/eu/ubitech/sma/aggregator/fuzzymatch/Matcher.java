package eu.ubitech.sma.aggregator.fuzzymatch;

import com.wcohen.ss.BasicStringWrapperIterator;
import com.wcohen.ss.JaroWinkler;
import com.wcohen.ss.SoftTFIDF;
import com.wcohen.ss.api.StringWrapper;
import com.wcohen.ss.api.Tokenizer;
import com.wcohen.ss.tokens.SimpleTokenizer;
import eu.ubitech.sma.repository.dao.CityDAO;
import eu.ubitech.sma.repository.dao.ProvinceDAO;
import eu.ubitech.sma.repository.domain.City;
import eu.ubitech.sma.repository.domain.Individual;
import eu.ubitech.sma.repository.domain.Province;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christos Paraskeva
 */
@Component
public class Matcher {

    @Autowired
    ProvinceDAO provinceDAO;

    @Autowired
    CityDAO cityDAO;

    private final String STRIP_CHARS_REGEXP = "[\\-\\+\\.\\^:_1234567890!@]";
    private final String EMPTY_STRING = "";
    private final double minTokenSimilarity = 0.75;
    private final double minSimilarityScore = 0.7;
    private final Tokenizer tokenizer = new SimpleTokenizer(false, true);
    // create a SoftTFIDF distance learner
    private final SoftTFIDF distance = new SoftTFIDF(tokenizer, new JaroWinkler(), minTokenSimilarity);
    private List<Province> provinces = null;
    List<City> cities = null;

//    public static void main(String[] args) {
//
//        List<Individual> tmplist = genericDAO.findAll(Individual.class);
//        Set<Individual> individuals = new HashSet(0);
//        individuals.addAll(tmplist);
//        genericDAO.saveAll(Matcher.INSTANCE.getBestMatchLocation(individuals));
//        System.out.println("Done!");
//    }
//    
    private boolean fetchProvincesAndCities() {

        this.setProvinces(provinceDAO.findAll());
        this.setCities(cityDAO.findAll());
        return (this.getCities() != null && this.getProvinces() != null);
    }

    private List<Province> getProvinces() {
        return provinces;
    }

    private void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public Set<Individual> getBestMatchLocation(Set<Individual> individuals) {
        int count = 0;
        Map<StringWrapper, City> citiesList = new HashMap();

        if (fetchProvincesAndCities()) {

            for (City city : this.getCities()) {
                citiesList.put(distance.prepare(city.getCity()), city);
            }

            distance.train(new BasicStringWrapperIterator(citiesList.keySet().iterator()));

            for (Individual individual : individuals) {

                if (!individual.getLocation().isEmpty() && !individual.getLocation().equals("")) {
                    double highestScore = 0;
                    double score = 0;

                    for (StringWrapper city : citiesList.keySet()) {
                        if ((score = getSimilarityScore(distance, getFormalizedString(individual.getLocation()), city)) >= this.minSimilarityScore && score > highestScore) {
                            highestScore = score;
                            individual.setCity(citiesList.get(city));
                            individual.setProvince(individual.getCity().getProvince());
                        }
                    }

                }

            }

        }
        //System.out.println("\nAll entries: " + individuals.size() + "\nTotal matches: " + count);
        return individuals;
    }

    private double getSimilarityScore(SoftTFIDF distance, String s, StringWrapper t) {
        // compute the similarity
        double score = distance.score(distance.prepare(s), t);
        if (score > minSimilarityScore) {
            // Print the score
            // String output = "========================================".concat("\nString s:  '" + s + "'").concat("\nString t:  '" + t + "'").concat("\nSimilarity: " + score).concat("\nExplanation:\n" + distance.explainScore(s, t.unwrap()));
            //  Logger.getLogger(Matcher.class.getName()).info(output);
        }

        return score;
    }

    private String getFormalizedString(String s) {
        return s.toLowerCase().split(",")[0].replaceAll(STRIP_CHARS_REGEXP, EMPTY_STRING);
    }

    private List<City> getCities() {
        return cities;
    }

    private void setCities(List<City> cities) {
        this.cities = cities;
    }

}
