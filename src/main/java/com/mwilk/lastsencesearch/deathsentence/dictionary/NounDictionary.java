package com.mwilk.lastsencesearch.deathsentence.dictionary;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class NounDictionary {
    List<String> nounList = new ArrayList<>();

    public NounDictionary(List<String> nounList) {
        this.nounList = nounList;
    }

    void setNounList(List<String> nounList) {
        this.nounList = nounList;
    }

    public boolean isNoun(String word){
        return nounList.contains(word);
    }
}
