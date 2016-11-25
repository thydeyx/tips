package com.meituan.service.mobile.tips.IRecall;

import com.meituan.dataapp.service.tips.thrift.TipsReq;
import com.meituan.jmonitor.JMonitor;
import com.meituan.ptdata.tairClientService.impl.TairClientService;
import com.meituan.service.mobile.tips.consts.JMonitorKey;
import com.meituan.service.mobile.tips.consts.TipsConsts;
import com.meituan.service.mobile.tips.frame.CompleteEventHandler;
import com.meituan.service.mobile.tips.model.SearchTipsItem;
import com.meituan.service.mobile.tips.model.SearchTipsRes;
import com.meituan.service.mobile.tips.model.TipsEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-27
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */
public class BizAreaHandler implements CompleteEventHandler{

    private Log logger = LogFactory.getLog(TipsConsts.STGLOG);

    @Autowired
    private TairClientService tairClient;

    @Override
    public int work(TipsEvent event) throws Exception {
        TipsReq request = event.getTipsReq();
        SearchTipsRes result = event.getSearchTipsRes();

        String value = getBizAreaResult(request.getCityid(), event.getNormQuery());
        if (value != null) {
            packResult(value, result.getTipsList());
        }
        return 0;
    }

    private void packResult(String value, List<SearchTipsItem> tipsList) {
        if (value.length() > 0) {
            String[] wordList = value.split("_");
            for (String word:wordList) {
                tipsList.add(new SearchTipsItem(word, 1));
            }
        }
    }

    private String getBizAreaResult(int cityid, String normQuery) {
        // bizarea_望京_1
        String key = (TipsConsts.BIZ_AREA + normQuery + "_" + cityid);
        try {
            String value = tairClient.get(key);
            logger.debug("key->" + key + "," + "value->" + value);
            return value;
        } catch (Exception e) {
            logger.error("e->" + e.getMessage(), e);
            JMonitor.add(JMonitorKey.TairError);
            return null;
        }
    }

    public void setTairClient(TairClientService tairClient) {
        this.tairClient = tairClient;
    }
}
