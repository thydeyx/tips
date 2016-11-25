package com.meituan.service.mobile.tips.logcollector;

import com.meituan.dataapp.service.tips.thrift.TipsReq;
import com.meituan.service.mobile.tips.consts.TipsConsts;
import com.meituan.service.mobile.tips.model.SearchTipsItem;
import com.meituan.service.mobile.tips.model.SearchTipsRes;
import com.meituan.service.mobile.tips.model.TipsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-26
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class ImpLogCollector {

    private static Logger logger = LoggerFactory.getLogger(TipsConsts.IMPLOG);

    private static final String FIELD_TIMESTAMP = "timestamp";
    private static final String FIELD_GID = "globalid";
    private static final String FIELD_REQID = "reqid";
    private static final String FIELD_CITYID = "cityid";
    private static final String FIELD_UUID = "uuid";
    private static final String FIELD_USERID = "userid";
    private static final String FIELD_DEVICE = "device";
    private static final String FIELD_QUERY = "query";
    private static final String FIELD_POSITION = "position";
    private static final String FIELD_WORD = "word";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_SOURCE = "source";
    private static final String FIELD_ALGO = "algo";
    private static final String FIELD_FEATURE = "feature";

    public static void collectLog(TipsEvent event) {
        TipsReq request = event.getTipsReq();
        SearchTipsRes result = event.getSearchTipsRes();

        int cityId = request.getCityid();
        int reqid = request.getReqid();
        int userid = request.getUserid();
        int limit = request.getLimit();
        long timeStamp = System.currentTimeMillis();
        String uuid = request.getUuid();
        String device = request.getDevice();
        String query = request.getQuery();
        String globalId = event.getGlobalId();

        StringBuilder sb;
        int pos = 0;
        for (SearchTipsItem item: result.getTipsList()) {
            sb = new StringBuilder();
            sb.append(FIELD_TIMESTAMP + "=" + timeStamp + " ");
            sb.append(FIELD_GID + "=" + globalId + " ");
            sb.append(FIELD_REQID + "=" + reqid + " ");
            sb.append(FIELD_CITYID + "=" + cityId + " ");
            sb.append(FIELD_UUID + "=" + uuid + " ");
            sb.append(FIELD_USERID + "=" + userid + " ");
            sb.append(FIELD_DEVICE + "=" + device + " ");
            sb.append(FIELD_QUERY + "=" + query + " ");
            sb.append(FIELD_POSITION + "=" + pos++ + " ");
            sb.append(FIELD_WORD + "=" + item.getWord() + " ");
            // sb.append(FIELD_ALGO + "=" + getAlgo(item.getStg()) + " ");
            logger.info(sb.toString());

            if (pos >= limit) {
                break;
            }
        }
    }

    public static String mapToString(Map<String, Double> featureMap) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, Double> entry : featureMap.entrySet()) {
            if (i++ > 0) {
                sb.append("," + entry.getKey() + ":" + entry.getValue());
            } else {
                sb.append(entry.getKey() + ":" + entry.getValue());
            }
        }
        return sb.toString();
    }

    private static String getAlgo(String stg) {
        return stg;
    }
}
