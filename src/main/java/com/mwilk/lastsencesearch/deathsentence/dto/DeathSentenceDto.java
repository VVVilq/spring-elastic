package com.mwilk.lastsencesearch.deathsentence.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@JsonPropertyOrder({"execution","lastName","firstName","prisonerId", "age", "race", "race",
        "countyOfConviction", "ageWhenReceived", "educationalLevel", "nativeCounty", "previousCrime", "codefendants", "numberVictim",
        "whiteVictim", "hispanicVictim", "blackVictim", "victimOtherRaces", "femaleVictim", "maleVictim", "lastStatement"})
public class DeathSentenceDto {

    public enum Race{
        White, Black, Hispanic, Other
    }
    private Integer execution;
    private String lastName;
    private String firstName;
    private Integer prisonerId;
    private Integer age;
    private Race race;
    private String countyOfConviction;
    private Integer ageWhenReceived;
    private Integer educationalLevel;
    private Boolean nativeCounty;
    private Boolean previousCrime;
    private Integer codefendants;
    private Integer numberVictim;
    private Integer whiteVictim;
    private Integer hispanicVictim;
    private Integer blackVictim;
    private Integer victimOtherRaces;
    private Integer femaleVictim;
    private Integer maleVictim;
    private String lastStatement;
}
