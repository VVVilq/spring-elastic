package com.mwilk.lastsencesearch.deathsentence;

import com.mwilk.lastsencesearch.deathsentence.dto.DeathSentenceDto;
import com.mwilk.lastsencesearch.deathsentence.dto.HitScoreDto;
import com.mwilk.lastsencesearch.deathsentence.dto.TermsOccurrencesDto;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
class DeathSentenceApi {

    private final DeathSentenceService deathSentenceService;

    public DeathSentenceApi(DeathSentenceService deathSentenceService) {
        this.deathSentenceService = deathSentenceService;
    }

    @ResponseBody
    @GetMapping(value = "/all", produces = "application/json")
    List<DeathSentenceDto> testing(){
        return deathSentenceService.getAllDeathSentences();
    }

    @ResponseBody
    @GetMapping(value = "/most-used-nouns")
    Set<TermsOccurrencesDto> getMostFrequentWords() throws IOException {
        return deathSentenceService.getLastSentenceCommonNouns();
    }

    @ResponseBody
    @GetMapping(value = "/terms-occurrence")
    List<TermsOccurrencesDto> getTermsOccurrenceFrequency() throws IOException {
        return deathSentenceService.getTermOccurrenceFrequency();
    }

    @ResponseBody
    @GetMapping(value = "/race-occurrence")
    List<TermsOccurrencesDto> getRaceOccurrencesFrequency() throws IOException {
        return deathSentenceService.getRaceOccurrenceFrequency();
    }

    @ResponseBody
    @GetMapping(value = "/last-sentence/race/{race}")
    List<DeathSentenceDto> getLastSentencesByRace(@PathVariable(value="race") String race){
        return deathSentenceService.getLastSentenceByRace(race);
    }

    @ResponseBody
    @GetMapping(value = "/last-sentence/age/{lowerBound}/{upperBound}")
    List<DeathSentenceDto> get(@PathVariable(value="lowerBound") int lowerBound, @PathVariable(value="upperBound") int upperBound){
        return deathSentenceService.getByAgeBetween(lowerBound, upperBound);
    }

    @ResponseBody
    @GetMapping(value = "/last-sentence/fuzzy-search/{phrase}/{fuzziness}")
    List<SearchHit<DeathSentence>> searchByPhraseWithFuzziness(@PathVariable(value="phrase") String phrase, @PathVariable(value="fuzziness") int fuzziness){
        return deathSentenceService.searchByPhraseWithFuzziness(phrase, fuzziness);
    }

    @ResponseBody
    @GetMapping(value = "/last-sentence/sloppy-search/{phrase}/{slop}")
    List<HitScoreDto> searchByPhraseWithSlop(@PathVariable(value="phrase") String phrase, @PathVariable(value="slop") int slop) throws IOException {
        return deathSentenceService.searchByPhraseWithSlop(phrase, slop);
    }
}
