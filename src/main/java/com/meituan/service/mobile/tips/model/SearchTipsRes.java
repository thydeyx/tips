package com.meituan.service.mobile.tips.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-24
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */
public class SearchTipsRes {

    private List<SearchTipsItem> tipsList;

    public SearchTipsRes(List<SearchTipsItem> tipsList) {
        this.tipsList = tipsList;
    }

    public List<SearchTipsItem> getTipsList() {
        return tipsList;
    }

    public void setTipsList(List<SearchTipsItem> tipsList) {
        this.tipsList = tipsList;
    }
}
