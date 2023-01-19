package com.mwilk.lastsencesearch.deathsentence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mwilk.lastsencesearch.deathsentence.dto.DeathSentenceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "death_sentence")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "_class" })
public class DeathSentence {

    @Id
    private Integer executionId;

    @Field(type = FieldType.Nested, includeInParent = true)
    private OffenderInformation offenderInformation;

    @Field(type = FieldType.Nested, includeInParent = true)
    private CrimeDetails crimeDetails;

    @Field(type = FieldType.Text, fielddata = true, analyzer = "stop")
    private String lastStatement;

    static DeathSentence from(DeathSentenceDto deathSentenceDto){
        return DeathSentence.builder()
                .executionId(deathSentenceDto.getExecution())
                .offenderInformation(OffenderInformation.builder()
                        .firstName(deathSentenceDto.getFirstName())
                        .lastName(deathSentenceDto.getLastName())
                        .age(deathSentenceDto.getAge())
                        .race(OffenderInformation.raceFrom(deathSentenceDto.getRace()))
                        .educationalLevel(deathSentenceDto.getEducationalLevel())
                        .countyOfConviction(deathSentenceDto.getCountyOfConviction())
                        .nativeCounty(deathSentenceDto.getNativeCounty())
                        .ageWhenReceived(deathSentenceDto.getAgeWhenReceived())
                        .previousCrime(deathSentenceDto.getPreviousCrime())
                        .build())
                .crimeDetails(CrimeDetails.builder()
                        .blackVictim(deathSentenceDto.getBlackVictim())
                        .hispanicVictim(deathSentenceDto.getHispanicVictim())
                        .whiteVictim(deathSentenceDto.getWhiteVictim())
                        .victimOtherRaces(deathSentenceDto.getVictimOtherRaces())
                        .codefendants(deathSentenceDto.getCodefendants())
                        .femaleVictim(deathSentenceDto.getFemaleVictim())
                        .maleVictim(deathSentenceDto.getMaleVictim())
                        .numberVictim(deathSentenceDto.getNumberVictim())
                        .build())
                .lastStatement(deathSentenceDto.getLastStatement())
                .build();
    }

    DeathSentenceDto toDto(){
        return DeathSentenceDto.builder()
                .execution(executionId)
                .lastStatement(lastStatement)
                .firstName(this.offenderInformation.getFirstName())
                .lastName(this.offenderInformation.getLastName())
                .race(this.offenderInformation.raceDto())
                .educationalLevel(this.offenderInformation.getAgeWhenReceived())
                .nativeCounty(this.offenderInformation.getNativeCounty())
                .previousCrime(this.offenderInformation.getPreviousCrime())
                .countyOfConviction(this.offenderInformation.getCountyOfConviction())
                .age(this.offenderInformation.getAge())
                .ageWhenReceived(this.offenderInformation.getAgeWhenReceived())
                .numberVictim(this.crimeDetails.getNumberVictim())
                .blackVictim(this.crimeDetails.getBlackVictim())
                .whiteVictim(this.crimeDetails.getWhiteVictim())
                .hispanicVictim(this.crimeDetails.getHispanicVictim())
                .victimOtherRaces(this.crimeDetails.getVictimOtherRaces())
                .maleVictim(this.crimeDetails.getMaleVictim())
                .femaleVictim(this.crimeDetails.getFemaleVictim())
                .codefendants(this.crimeDetails.getCodefendants())
                .build();
    }
}
