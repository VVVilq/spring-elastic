package com.mwilk.lastsencesearch.deathsentence;

import com.mwilk.lastsencesearch.deathsentence.dto.DeathSentenceDto;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OffenderInformation {
    private enum Race{
        White, Black, Hispanic, Other
    }

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Text)
    private String firstName;

    @Field(type = FieldType.Integer)
    private Integer age;

    @Field(type = FieldType.Keyword)
    private Race race;

    @Field(type = FieldType.Text)
    private String countyOfConviction;

    @Field(type = FieldType.Integer)
    private Integer ageWhenReceived;

    @Field(type = FieldType.Integer)
    private Integer educationalLevel;

    @Field(type = FieldType.Boolean)
    private Boolean nativeCounty;

    @Field(type = FieldType.Boolean)
    private Boolean previousCrime;

    static Race raceFrom(DeathSentenceDto.Race race){
       return Race.valueOf(race.name());
    }

    DeathSentenceDto.Race raceDto(){
        return DeathSentenceDto.Race.valueOf(this.getRace().name());
    }

}
