package com.mwilk.lastsencesearch.deathsentence.dictionary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = { "com.mwilk.lastsencesearch.deathsentence" })
public class NounDictionaryConfig {

    @Bean
    public NounDictionary readNounList(){
        NounDictionary nounDictionary = new NounDictionary();
        List<String> nounList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/nounList.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                nounList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        nounDictionary.setNounList(nounList);
        return nounDictionary;
    }
}
