package com.meituan.service.mobile.tips.queryResult;

import com.meituan.dataapp.service.tips.thrift.TipsReq;
import com.meituan.service.mobile.tips.config.MtConfig4Tips;
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
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
public class QueryResultHandler implements CompleteEventHandler {

    private Log logger = LogFactory.getLog(TipsConsts.STGLOG);

    @Autowired
    private QueryResultRedisClient redisClient;

    @Override
    public int work(TipsEvent event) throws Exception {
        if (!MtConfig4Tips.isQueryResult) {
            return 0;
        }

        TipsReq request = event.getTipsReq();
        SearchTipsRes result = event.getSearchTipsRes();

        String[] keyList = getQueryList(event.getNormQuery(), request.getCityid(), result.getTipsList());
        List<String> valueList = redisClient.getQueryResult(keyList);
        // 判断是否取到结果集, 判断返回结果集大小是否equals
        if (valueList != null && valueList.size() == keyList.length) {
            int pos = 0;
            for (SearchTipsItem item : result.getTipsList()) {
                if (valueList.get(pos) != null) {
                    item.setCount(Integer.parseInt(valueList.get(pos)));
                }
                logger.debug("query->" + item.getWord() + " result->" + valueList.get(pos));
                pos++;
            }
            return 0;
        } else {
            logger.error("cityid->" + request.getCityid() + " query->" + request.getQuery()
                    + " keyList->" + keyList.length + " valueList->" + valueList);
            return -1;
        }
    }

    private String[] getQueryList(String Query, int cityid, List<SearchTipsItem> itemList) {
        String[] keyList = new String[itemList.size()];
        int pos = 0;
        for (SearchTipsItem item : itemList) {
            // 1_望京火锅
            keyList[pos++] = (cityid + "_" + Query + item.getWord());
        }
        return keyList;
    }

    public void setRedisClient(QueryResultRedisClient redisClient) {
        this.redisClient = redisClient;
    }
}
