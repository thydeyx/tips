package com.meituan.service.mobile.tips.logcollector;

import com.meituan.service.mobile.tips.frame.CompleteEventHandler;
import com.meituan.service.mobile.tips.model.TipsEvent;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 16-1-2
 * Time: 下午2:24
 * To change this template use File | Settings | File Templates.
 */
public class LogCollectorHandler implements CompleteEventHandler {
    @Override
    public int work(TipsEvent event) throws Exception {
        ReqLogCollector.collectLog(event);
        return 0;
    }
}
