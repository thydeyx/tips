package com.meituan.service.mobile.tips.frame;

import com.meituan.service.mobile.tips.router.MurmurHash;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-27
 * Time: 上午8:28
 * To change this template use File | Settings | File Templates.
 */
public class Event {
    public String globalId;

    public long serviceBegin;

    public int retCode;

    public Event(String traceId, String uuid) {
        // 生成一次请求的唯一请求id
        this.globalId = MurmurHash.getGlobalId();
        this.serviceBegin = System.currentTimeMillis();
        this.retCode = 0;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public int getRetCode() {
        return retCode;
    }

    public long getServiceBegin() {
        return serviceBegin;
    }

    public void setServiceBegin(long serviceBegin) {
        this.serviceBegin = serviceBegin;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
}
