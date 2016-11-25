package com.meituan.service.mobile.tips.router;

import java.util.List;
import java.util.Set;

public class Segment implements Comparable<Segment> {
    private int beginBucket = 0;
    private int endBucket = 99;
    private Set<String> cityIdsIncluded;
    private Set<String> cityIdsExcluded;
    private List<String> handlerList;
    private long beginTime = 0;
    private long endTime = 0;
    private String strategy;
    private SegmentSetting setting;

    public int getBeginBucket() {
        return beginBucket;
    }

    public void setBeginBucket(int beginBucket) {
        this.beginBucket = beginBucket;
    }

    public int getEndBucket() {
        return endBucket;
    }

    public void setEndBucket(int endBucket) {
        this.endBucket = endBucket;
    }

    public SegmentSetting getSetting() {
        return setting;
    }

    public void setSetting(SegmentSetting setting) {
        this.setting = setting;
    }

    public boolean isValid() {
        return (beginBucket <= endBucket) && (beginTime <= endTime);
    }

    public boolean isOverlap(Segment other) {
        boolean t = (endBucket <= other.beginBucket) || (beginBucket >= other.endBucket);
        return !t;
    }

    public Set<String> getCityIdsIncluded() {
        return cityIdsIncluded;
    }

    public Set<String> getCityIdsExcluded() {
        return cityIdsExcluded;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setCityIdsIncluded(Set<String> cityIdsIncluded) {
        this.cityIdsIncluded = cityIdsIncluded;
    }

    public void setCityIdsExcluded(Set<String> cityIdsExcluded) {
        this.cityIdsExcluded = cityIdsExcluded;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<String> getHandlerList() {
        return handlerList;
    }

    public void setHandlerList(List<String> handlerList) {
        this.handlerList = handlerList;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public int compareTo(Segment other) {
        if (beginBucket < other.beginBucket)
            return -1;
        else if (beginBucket > other.beginBucket)
            return 1;
        else
            return 0;
    }

    public int locate(int point) {
        if (point >= beginBucket && point <= endBucket)
            return 0;
        else if (point < beginBucket)
            return -1;
        else
            return 1;
    }
}