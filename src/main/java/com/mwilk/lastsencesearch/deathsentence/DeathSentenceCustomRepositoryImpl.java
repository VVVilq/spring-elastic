package com.mwilk.lastsencesearch.deathsentence;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.MtermvectorsRequest;
import co.elastic.clients.elasticsearch.core.MtermvectorsResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.mtermvectors.MultiTermVectorsResult;
import com.mwilk.lastsencesearch.deathsentence.dto.HitScoreDto;
import com.mwilk.lastsencesearch.deathsentence.dto.TermsOccurrencesDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DeathSentenceCustomRepositoryImpl implements DeathSentenceCustomRepository {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticsearchClient elasticsearchClient;

    public DeathSentenceCustomRepositoryImpl(ElasticsearchOperations elasticsearchOperations, ElasticsearchClient elasticsearchClient) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Set<TermsOccurrencesDto> findMostCommonLastSentenceWords() throws IOException {


        Query query = elasticsearchOperations.matchAllQuery().setPageable(Pageable.ofSize(600));
        SearchHits<DeathSentence> search = elasticsearchOperations.search(query, DeathSentence.class);
        List<String> ids = StreamSupport.stream(search.spliterator(), false)
                .map(SearchHit::getContent)
                .map(DeathSentence::getExecutionId)
                .map(integer -> Integer.toString(integer))
                .collect(Collectors.toList());


        MtermvectorsRequest mtermvectorsRequest = new MtermvectorsRequest.Builder().index("death_sentence").ids(ids).termStatistics(true).fields(List.of("lastStatement")).build();

        MtermvectorsResponse mtermvectors = elasticsearchClient.mtermvectors(mtermvectorsRequest);

        return mtermvectors.docs().stream()
                .map(MultiTermVectorsResult::termVectors)
                .map(stringTermVectorMap -> stringTermVectorMap.get("lastStatement").terms().entrySet().stream()
                        .map(term -> new TermsOccurrencesDto(term.getKey(), term.getValue().ttf())).collect(Collectors.toList()))
                .flatMap(List::stream).sorted(Comparator.comparing(TermsOccurrencesDto::getOccurrences).reversed()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public List<TermsOccurrencesDto> findTermDocOccurrence() throws IOException {

        SearchResponse<Void> response = elasticsearchClient.search(builder -> builder
                .index("death_sentence")
                .size(0)
                .query(MatchAllQuery.of(m -> m.queryName("match-all"))._toQuery())
                .aggregations("term-occurrence", a -> a.terms(t -> t.field("lastStatement").size(60))), Void.class);

        return response.aggregations().get("term-occurrence").sterms().buckets().array().stream()
                .map(termsBucket -> new TermsOccurrencesDto(termsBucket.key().stringValue(), Long.valueOf(termsBucket.docCount()).intValue()))
                .collect(Collectors.toList());

    }

    @Override
    public List<TermsOccurrencesDto> aggregateByRace() throws IOException {

        SearchResponse<Void> response = elasticsearchClient.search(builder -> builder
                .index("death_sentence")
                .size(0)
                .query(MatchAllQuery.of(m -> m.queryName("match-all"))._toQuery())
                .aggregations("race-occurrence", a -> a.terms(t -> t.field("offenderInformation.race").size(60))), Void.class);

        return response.aggregations().get("race-occurrence").sterms().buckets().array().stream()
                .map(termsBucket -> new TermsOccurrencesDto(termsBucket.key().stringValue(), Long.valueOf(termsBucket.docCount()).intValue()))
                .collect(Collectors.toList());
    }


    @Override
    public SearchHits<DeathSentence> findByPhraseWithFuzziness(String phrase, int fuzziness) {
        NativeQuery lastStatement = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("lastStatement")
                        .query(phrase)
                        .operator(Operator.And)
                        .fuzziness(String.valueOf(fuzziness))
                        .prefixLength(2))
                )
                .build();

        return elasticsearchOperations.search(lastStatement, DeathSentence.class);
    }

    @Override
    public List<HitScoreDto> findByPhraseWithSlop(String phrase, int slop) throws IOException {

        return elasticsearchClient.search(builder -> builder
                        .index("death_sentence")
                        .size(10)
                        .query(q -> q.matchPhrase(p -> p.field("lastStatement").query(phrase).slop(slop))), DeathSentence.class).hits().hits().stream()
                .map(deathSentenceHit -> HitScoreDto.builder()
                        .hitScore(deathSentenceHit.score())
                        .sentence(Optional.ofNullable(deathSentenceHit.source()).orElseThrow(IllegalStateException::new).getLastStatement())
                        .build())
                .collect(Collectors.toList());
    }
}
