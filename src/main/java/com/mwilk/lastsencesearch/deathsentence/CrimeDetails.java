package com.mwilk.lastsencesearch.deathsentence;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@EqualsAndHashCode
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CrimeDetails {

    @Field(type = FieldType.Integer)
    private Integer codefendants;

    @Field(type = FieldType.Integer)
    private Integer numberVictim;

    @Field(type = FieldType.Integer)
    private Integer whiteVictim;

    @Field(type = FieldType.Integer)
    private Integer hispanicVictim;

    @Field(type = FieldType.Integer)
    private Integer blackVictim;

    @Field(type = FieldType.Integer)
    private Integer victimOtherRaces;

    @Field(type = FieldType.Integer)
    private Integer femaleVictim;

    @Field(type = FieldType.Integer)
    private Integer maleVictim;
}
