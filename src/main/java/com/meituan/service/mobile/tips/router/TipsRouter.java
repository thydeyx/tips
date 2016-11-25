package com.meituan.service.mobile.tips.router;

import com.google.common.collect.Maps;
import com.meituan.dataapp.service.tips.thrift.TipsReq;
import com.meituan.service.mobile.tips.consts.TipsConsts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 16-1-1
 * Time: 下午9:36
 * To change this template use File | Settings | File Templates.
 */

public class TipsRouter {
    private Log logger = LogFactory.getLog(this.getClass());

    private Map<Integer, Abtest> reqIdList = Maps.newHashMap();
    private String runningMode;

    public TipsRouter(String configPath) throws Exception {
        InputStreamReader isr = new InputStreamReader(TipsRouter.class.getClassLoader().getResourceAsStream(configPath));
        char[] buf = new char[1024 * 1024];
        isr.read(buf);

        JSONArray jsonArray = new JSONArray(new String(buf));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject segObj = jsonArray.getJSONObject(i);
            reqIdList.put(segObj.getInt("reqid"), new Abtest(segObj.getString("config")));
        }
    }

    public Segment getSegment(TipsReq tipsReq) {
        int reqid = tipsReq.getReqid();
        if (reqIdList.containsKey(reqid)) {
            // default情况下走默认策略
            if (runningMode.equals("default")) {
                return reqIdList.get(reqid).getDefaultSegment();
            } else {
                return reqIdList.get(reqid).getSegment(TipsConsts.AbtestPrefix,
                        tipsReq.getUuid(), Integer.toString(tipsReq.getCityid()));
            }
        } else {
            logger.error("reqid->" + reqid + " getSegment is failed.");
            return null;
        }
    }

    public String getRunningMode() {
        return runningMode;
    }

    public void setRunningMode(String runningMode) {
        this.runningMode = runningMode;
    }
}
