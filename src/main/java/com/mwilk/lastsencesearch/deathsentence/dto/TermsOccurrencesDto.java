package com.mwilk.lastsencesearch.deathsentence.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class TermsOccurrencesDto {
    private String term;
    private Integer occurrences;
}
