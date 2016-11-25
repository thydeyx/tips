package com.meituan.service.mobile.tips.logcollector;

import com.meituan.dataapp.service.tips.thrift.Location;
import com.meituan.dataapp.service.tips.thrift.SelectMsg;
import com.meituan.dataapp.service.tips.thrift.TipsReq;
import com.meituan.dataapp.service.tips.thrift.TipsRes;
import com.meituan.jmonitor.JMonitor;
import com.meituan.service.mobile.tips.consts.JMonitorKey;
import com.meituan.service.mobile.tips.consts.TipsConsts;
import com.meituan.service.mobile.tips.model.TipsEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 15-12-26
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */

public class ReqLogCollector {

    private static Logger logger = LoggerFactory.getLogger(TipsConsts.REQLOG);
    private static Log requestLog = LogFactory.getLog(TipsConsts.REQLOGTOFILE);

    private static final String FIELD_TIMESTAMP = "timestamp";
    // api生成的唯一问题跟踪id
    private static final String FIELD_TRACEID = "traceid";
    private static final String FIELD_GID = "globalid";
    private static final String FIELD_REQID = "reqid";
    private static final String FIELD_CITYID = "cityid";
    private static final String FIELD_UUID = "uuid";
    private static final String FIELD_USERID = "userid";
    private static final String FIELD_DEVICE = "device";
    private static final String FIELD_QUERY = "query";
    private static final String FIELD_COUNT = "count";
    private static final String FIELD_LENGTH = "length";
    private static final String FIELD_LOCATION = "location";
    private static final String FIELD_SELECTMSG = "selectMsg";
    private static final String FIELD_TYPE = "type";
    private static final String FIELD_STG = "strategy";
    private static final String FIELD_FEATURE = "feature";
    private static final String FIELD_SERVICETIME = "serviceTime";

    public static void collectLog(TipsEvent event) {
        TipsReq request = event.getTipsReq();
        TipsRes result = event.getTipsRes();

        // log request
        long timeStamp = System.currentTimeMillis();
        long serviceTime = timeStamp - event.getServiceBegin();
        StringBuilder sb = new StringBuilder();

        // 临时hardcode,无结果标记Nodiff
        if (result.getTipsList().size() == 0 || event.isNodiff() == true) {
            sb.append(FIELD_STG + "=" + TipsConsts.NODIFF + " ");
        } else {
            sb.append(FIELD_STG + "=" + event.getSegment().getStrategy() + " ");
        }

        // hardCode解决abtest问题
        if (event.getSegment().getStrategy().equals("hide") && event.isNodiff() == false) {
            result.getTipsList().clear();
        }

        sb.append(FIELD_TIMESTAMP + "=" + timeStamp + " ");
        sb.append(FIELD_TRACEID + "=" + request.getTraceId() + " ");
        sb.append(FIELD_GID + "=" + event.getGlobalId() + " ");
        sb.append(FIELD_REQID + "=" + request.getReqid() + " ");
        sb.append(FIELD_CITYID + "=" + request.getCityid() + " ");
        sb.append(FIELD_UUID + "=" + request.getUuid() + " ");
        sb.append(FIELD_USERID + "=" + request.getUserid() + " ");
        sb.append(FIELD_DEVICE + "=" + request.getDevice() + " ");
        sb.append(FIELD_QUERY + "=" + request.getQuery() + " ");
        sb.append(FIELD_COUNT + "=" + result.getTipsList().size() + " ");
        sb.append(FIELD_LENGTH + "=" + request.getLength() + " ");
        sb.append(FIELD_LOCATION + "=" + getLocation(request.getLocation()) + " ");
        sb.append(FIELD_SELECTMSG + "=" + getSelectMsg(request.getSelectMsg()) + " ");
        //sb.append(FIELD_STG + "=" + event.getSegment().getStrategy() + " ");


        sb.append(FIELD_SERVICETIME + "=" + serviceTime + " ");
        String log = sb.toString();
        logger.info(log);
        requestLog.info(log);
        // JMonitor ServiceTime
        JMonitor.add(request.getReqid() + "." + JMonitorKey.ServiceTime, serviceTime);
        // log impression
        ImpLogCollector.collectLog(event);
    }

    private static String getSelectMsg(SelectMsg selectMsg) {
        return "";
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

    public static String getLocation(Location loc) {
        if (loc != null) {
            return loc.getLatitude() + "," + loc.getLongitude();
        } else {
            return "";
        }
    }
}
