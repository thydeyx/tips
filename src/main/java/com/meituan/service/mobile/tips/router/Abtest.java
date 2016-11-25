package com.meituan.service.mobile.tips.router;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chendayao
 * Date: 16-1-1
 * Time: 下午9:36
 * To change this template use File | Settings | File Templates.
 */

public class Abtest {
    private Log logger = LogFactory.getLog(this.getClass());

    private int numOfBuckets;
    private List<Segment> segments;
    private Segment defaultSegment;
    private Map<String, Segment> whiteList = Maps.newHashMap();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

    private boolean isValid() {
        if (numOfBuckets < 0) {
            return false;
        }

        if (segments.isEmpty())
            return true;

        // 每一个bucket的定义是正确的
        for (Segment segment : segments) {
            if (!segment.isValid())
                return false;
        }

        // 判断bucket之间有没有重合
        for (int i = 1; i < segments.size(); i++) {
            Segment current = segments.get(i);
            Segment previous = segments.get(i - 1);
            if (current.isOverlap(previous))
                return false;
        }

        return true;
    }

    public Segment getSegment(String abtestPrefix, String uuid, String cityid) {
        int index = 0;
        Segment ret = defaultSegment;

        if (uuid == null || uuid.length() < 10) {
            index = ((int) (Math.random() * numOfBuckets)) % numOfBuckets;
        }

        if (whiteList.containsKey(uuid)) {
            ret = whiteList.get(uuid);
        } else {
            String hashUUid = abtestPrefix + uuid;
            index = Math.abs(MurmurHash.hash32(hashUUid)) % numOfBuckets;
            int left = 0, right = segments.size();
            long now = System.currentTimeMillis() / 1000;
            while (left < right) {
                int m = (left + right) / 2;
                Segment s = segments.get(m);
                int r = s.locate(index);
                if (r == 0) {
                    if ((s.getCityIdsIncluded().isEmpty() || s.getCityIdsIncluded().contains(cityid))
                            && (!s.getCityIdsExcluded().contains(cityid))) {
                        if (now > s.getBeginTime() && now < s.getEndTime()) {
                            ret = s;  // hit
                        }
                    }
                    break;
                } else if (r < 0) {
                    right = m;
                } else {
                    left = m + 1;
                }
            }
        }

        logger.info("segment for uuid: " + uuid
                + ", BeginBucket:" + ret.getBeginBucket()
                + ", EndBucket:" + ret.getEndBucket()
                + ", Strategy:" + ret.getStrategy());

        return ret;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public Abtest(String configPath) throws Exception {
        InputStreamReader isr = new InputStreamReader(Abtest.class.getClassLoader().getResourceAsStream(configPath));
        char[] buf = new char[1024 * 1024];
        isr.read(buf);

        JSONObject obj = new JSONObject(new String(buf));
        numOfBuckets = obj.getInt("NumberOfBuckets");
        defaultSegment = parseSegment(obj.getJSONObject("DefaultSegment"));
        segments = new ArrayList<Segment>();

        JSONArray segmentsObj = obj.getJSONArray("Segments");
        for (int i = 0; i < segmentsObj.length(); i++) {
            JSONObject segObj = segmentsObj.getJSONObject(i);
            segments.add(parseSegment(segObj));
        }

        Collections.sort(segments);

        if (!isValid()) {
            logger.error("configPath->" + configPath + " =======invalid router=======");
        } else {
            logger.info("configPath->" + configPath + " =======valid router=======");
        }
    }

    public Segment parseSegment(JSONObject jsonObject) throws Exception {
        Segment seg = new Segment();
        try {
            seg.setBeginBucket(jsonObject.getInt("BeginBucket"));
        } catch (Exception e) {
            seg.setBeginBucket(0);
        }

        try {
            seg.setEndBucket(jsonObject.getInt("EndBucket"));
        } catch (Exception e) {
            seg.setEndBucket(99);
        }

        try {
            seg.setStrategy(jsonObject.getString("Strategy"));
        } catch (Exception e) {
            seg.setStrategy(seg.getBeginBucket() + "-" + seg.getEndBucket());
        }

        try {
            String beginTimeStr = jsonObject.getString("BeginTime");
            seg.setBeginTime(dateFormat.parse(beginTimeStr).getTime() / 1000);
        } catch (Exception e) {
            seg.setBeginTime(Long.MIN_VALUE);
        }

        try {
            String endTimeStr = jsonObject.getString("EndTime");
            seg.setEndTime(dateFormat.parse(endTimeStr).getTime() / 1000);
        } catch (Exception e) {
            seg.setEndTime(Long.MAX_VALUE);
        }

        Set<String> cityIdsIncluded = new HashSet<String>();
        try {
            JSONArray cityIdsIncludedJsonArray = jsonObject.getJSONArray("CityIdsIncluded");
            for (int j = 0; j < cityIdsIncludedJsonArray.length(); j++) {
                cityIdsIncluded.add(cityIdsIncludedJsonArray.getString(j));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        seg.setCityIdsIncluded(cityIdsIncluded);

        Set<String> cityIdsExcluded = new HashSet<String>();
        try {
            JSONArray cityIdsExcludedJsonArray = jsonObject.getJSONArray("CityIdsExcluded");
            for (int j = 0; j < cityIdsExcludedJsonArray.length(); j++) {
                cityIdsExcluded.add(cityIdsExcludedJsonArray.getString(j));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        seg.setCityIdsExcluded(cityIdsExcluded);

        seg.setSetting(new SegmentSetting(jsonObject.getJSONObject("Setting")));

        JSONArray handlerListJsonArray = jsonObject.getJSONArray("HandlerList");
        List<String> handlerList = Lists.newArrayList();
        for (int j = 0; j < handlerListJsonArray.length(); j++) {
            handlerList.add(handlerListJsonArray.getString(j));
        }
        seg.setHandlerList(handlerList);

        if (jsonObject.has("WhiteList")) {
            JSONArray whiteListJsonArray = jsonObject.getJSONArray("WhiteList");
            for (int j = 0; j < whiteListJsonArray.length(); j++) {
                whiteList.put(whiteListJsonArray.getString(j), seg);
            }
        }

        return seg;
    }

    public Segment getDefaultSegment() {
        return defaultSegment;
    }
}
