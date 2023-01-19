package com.mwilk.lastsencesearch.deathsentence;


import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;


public interface DeathSentenceRepository extends ElasticsearchRepository<DeathSentence, String>, DeathSentenceCustomRepository {
    @Query("{\"wildcard\": {\"offenderInformation.race\": {\"value\": \"?0\"}}}")
    List<DeathSentence> findByRace(String name, Pageable pageable);

    List<DeathSentence> findByOffenderInformationAgeBetween(int lowerBound, int upperBound);

    SearchHits<DeathSentence> findByLastStatementContaining(String phrase);
}
