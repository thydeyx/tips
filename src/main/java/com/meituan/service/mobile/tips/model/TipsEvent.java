package com.meituan.service.mobile.tips.model;

import com.meituan.dataapp.service.tips.thrift.TipsReq;
import com.meituan.dataapp.service.tips.thrift.TipsRes;
import com.meituan.service.mobile.tips.frame.Event;
import com.meituan.service.mobile.tips.router.Segment;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-27
 * Time: 上午8:36
 * To change this template use File | Settings | File Templates.
 */
public class TipsEvent extends Event {
    private SearchTipsRes searchTipsRes;
    private TipsReq tipsReq;
    private TipsRes tipsRes;
    private String normQuery;  // 归一化query
    private Segment segment;
    private boolean isNodiff;

    public TipsEvent(SearchTipsRes searchTipsRes, TipsReq tipsReq, TipsRes tipsRes) {
        super(tipsReq.getTraceId(), tipsReq.getUuid());
        this.searchTipsRes = searchTipsRes;
        this.tipsReq = tipsReq;
        this.tipsRes = tipsRes;
        this.normQuery = "";
        this.isNodiff = false;
    }

    public SearchTipsRes getSearchTipsRes() {
        return searchTipsRes;
    }

    public void setSearchTipsRes(SearchTipsRes searchTipsRes) {
        this.searchTipsRes = searchTipsRes;
    }

    public TipsReq getTipsReq() {
        return tipsReq;
    }

    public void setTipsReq(TipsReq tipsReq) {
        this.tipsReq = tipsReq;
    }

    public TipsRes getTipsRes() {
        return tipsRes;
    }

    public void setTipsRes(TipsRes tipsRes) {
        this.tipsRes = tipsRes;
    }

    public String getNormQuery() {
        return normQuery;
    }

    public void setNormQuery(String normQuery) {
        this.normQuery = normQuery;
    }

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public boolean isNodiff() {
        return isNodiff;
    }

    public void setIsNodiff(boolean isNodiff) {
        this.isNodiff = isNodiff;
    }
}
