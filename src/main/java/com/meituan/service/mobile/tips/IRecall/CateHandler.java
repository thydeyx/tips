package com.meituan.service.mobile.tips.IRecall;

import com.google.common.collect.Lists;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 16/5/11
 * Time: 下午9:43
 * To change this template use File | Settings | File Templates.
 */

public class CateHandler implements CompleteEventHandler {

    private Log logger = LogFactory.getLog(TipsConsts.STGLOG);

    @Autowired
    private TairClientService tairClient;

    @Override
    public int work(TipsEvent event) throws Exception {
        TipsReq request = event.getTipsReq();
        SearchTipsRes result = event.getSearchTipsRes();

        String value = getResult(request.getCityid(), event.getNormQuery(), event);
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

    private String getResult(int cityid, String normQuery, TipsEvent event) {
        List<String> keysList = Lists.newArrayList();
        // bizarea_望京_1
        keysList.add(TipsConsts.BIZ_AREA + normQuery + "_" + cityid);
        // CATE_火锅_1
        keysList.add(TipsConsts.PRE_CATE + normQuery + "_" + cityid);
        try {
            Map<?, ?> value = tairClient.batchGet(keysList);
            logger.debug("key->" + keysList.toString() + "," + "value->" + value.get(keysList.get(1)));

            if (value.get(keysList.get(1)) != null) {
                try {
                    JSONObject obj = new JSONObject((String)value.get(keysList.get(1)));
                    return obj.getString("tag_list_for_tair");
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return null;
                }
            } else {
                event.setIsNodiff(true);
                return (String) value.get(keysList.get(0));
            }
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