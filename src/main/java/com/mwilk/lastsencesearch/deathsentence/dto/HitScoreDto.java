package com.mwilk.lastsencesearch.deathsentence.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HitScoreDto {
    private Double hitScore;
    private String sentence;
}
