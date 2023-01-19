package com.mwilk.lastsencesearch.deathsentence;

import com.mwilk.lastsencesearch.deathsentence.dto.HitScoreDto;
import com.mwilk.lastsencesearch.deathsentence.dto.TermsOccurrencesDto;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface DeathSentenceCustomRepository {
    Set<TermsOccurrencesDto> findMostCommonLastSentenceWords() throws IOException;

    List<TermsOccurrencesDto> findTermDocOccurrence() throws IOException;

    List<TermsOccurrencesDto> aggregateByRace() throws IOException;

    SearchHits<DeathSentence> findByPhraseWithFuzziness(String phrase, int fuzziness);

    List<HitScoreDto> findByPhraseWithSlop(String phrase, int slop) throws IOException;
}
