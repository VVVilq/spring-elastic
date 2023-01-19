package com.mwilk.lastsencesearch.deathsentence;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.mwilk.lastsencesearch.deathsentence.dto.DeathSentenceDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CSVDeathSentenceParser {

    public List<DeathSentenceDto> parseFileToDeathSentences(File csvFile) throws IOException {


        CsvMapper csvMapper = new CsvMapper();

        CsvSchema csvSchema = CsvSchema.emptySchema()
                .withColumnSeparator(',')
                .withComments();

        MappingIterator<DeathSentenceDto> deathSentencesIter = csvMapper
                .readerWithTypedSchemaFor(DeathSentenceDto.class)
                .readValues(csvFile);

        return deathSentencesIter.readAll();
    }
}
