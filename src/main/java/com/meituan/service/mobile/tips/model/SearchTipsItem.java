package com.meituan.service.mobile.tips.model;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-24
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
public class SearchTipsItem {
    public String word="";
    public Integer count;
    public String stg;
    public double score;
    public Map<String, Double>featureMap;

    public SearchTipsItem(String word, Integer count) {
        this.word = word;
        this.count = count;
    }

    public Map<String, Double> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(Map<String, Double> featureMap) {
        this.featureMap = featureMap;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStg() {
        return stg;
    }

    public void setStg(String stg) {
        this.stg = stg;
    }

}
