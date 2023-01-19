package com.mwilk.lastsencesearch.deathsentence;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import com.mwilk.lastsencesearch.deathsentence.dictionary.NounDictionary;
import com.mwilk.lastsencesearch.deathsentence.dto.DeathSentenceDto;
import com.mwilk.lastsencesearch.deathsentence.dto.HitScoreDto;
import com.mwilk.lastsencesearch.deathsentence.dto.TermsOccurrencesDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class DeathSentenceService {

    private final DeathSentenceRepository deathSentenceRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final NounDictionary nounDictionary;

    @Autowired
    public DeathSentenceService(DeathSentenceRepository deathSentenceRepository, ElasticsearchClient elasticsearchClient, NounDictionary nounDictionary) {

        this.deathSentenceRepository = deathSentenceRepository;
        this.elasticsearchClient = elasticsearchClient;
        this.nounDictionary = nounDictionary;
    }

    public List<DeathSentenceDto> getAllDeathSentences(){
        return StreamSupport.stream(deathSentenceRepository.findAll().spliterator(),false)
                .map(DeathSentence::toDto).collect(Collectors.toList());
    }

    public Set<TermsOccurrencesDto> getLastSentenceCommonNouns() throws IOException {
        return deathSentenceRepository.findMostCommonLastSentenceWords().stream()
                .filter(term -> nounDictionary.isNoun(term.getTerm()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<DeathSentenceDto> getByAgeBetween(int lowerBound, int upperBound) {
        return deathSentenceRepository
                .findByOffenderInformationAgeBetween(lowerBound, upperBound)
                .stream().map(DeathSentence::toDto)
                .collect(Collectors.toList());
    }

    public List<TermsOccurrencesDto> getTermOccurrenceFrequency() throws IOException {
        return deathSentenceRepository.findTermDocOccurrence();
    }

    public List<DeathSentenceDto> getLastSentenceByRace(String race) {
        return deathSentenceRepository.findByRace(race, PageRequest.of(0,20)).stream()
                .map(DeathSentence::toDto)
                .collect(Collectors.toList());
    }

    public List<TermsOccurrencesDto> getRaceOccurrenceFrequency() throws IOException {
        return deathSentenceRepository.aggregateByRace();
    }

    public List<SearchHit<DeathSentence>> searchByPhrase(String phrase) {
        return deathSentenceRepository.findByLastStatementContaining("Lord Jesus").getSearchHits();
    }

    public List<SearchHit<DeathSentence>> searchByPhraseWithFuzziness(String phrase, int fuzziness) {
        return deathSentenceRepository.findByPhraseWithFuzziness(phrase, fuzziness).getSearchHits();
    }

    public List<HitScoreDto> searchByPhraseWithSlop(String phrase, int slop) throws IOException {
        return deathSentenceRepository.findByPhraseWithSlop(phrase, slop);
    }

    @PostConstruct
    public void initData(){
        try {
            CountResponse docCount = elasticsearchClient.count(CountRequest.of(builder -> builder.index("death_sentence")));
            if(docCount.count() == 0) {
                CSVDeathSentenceParser parser = new CSVDeathSentenceParser();
                File csvFile = new File("src/main/resources/Texas Last Statement2.csv");
                List<DeathSentenceDto> deathSentences = parser.parseFileToDeathSentences(csvFile);
                deathSentenceRepository.saveAll(deathSentences.stream().map(DeathSentence::from).collect(Collectors.toList()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
