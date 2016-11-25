package com.meituan.service.mobile.tips.checkQuery;

import com.meituan.dataapp.service.tips.thrift.TipsReq;
import com.meituan.service.mobile.data.DataClient;
import com.meituan.service.mobile.tips.consts.TipsConsts;
import com.meituan.service.mobile.tips.frame.CompleteEventHandler;
import com.meituan.service.mobile.tips.model.TipsEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-27
 * Time: 上午9:22
 * To change this template use File | Settings | File Templates.
 */
public class CheckQueryHandler implements CompleteEventHandler {

    private Log logger = LogFactory.getLog(TipsConsts.STGLOG);

    @Autowired
    private DataClient uuidClient;

    @Override
    public int work(TipsEvent event) throws Exception {
        TipsReq tipsReq = event.getTipsReq();
        event.setNormQuery(getNormQuery(tipsReq.getQuery()));
        return 0;
    }

    // trim left or right empty char
    private String getNormQuery(String query) {
        query = query.toLowerCase();
        return query.trim();
    }
}
