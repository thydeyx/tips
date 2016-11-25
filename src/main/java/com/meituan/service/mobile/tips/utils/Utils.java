package com.meituan.service.mobile.tips.utils;

import com.vividsolutions.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一些公共方法.
 * User: zhanlijun
 * Date: 15-03-05
 * Time: 下午3:55
 */

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static final double EPS =  1e-6;

    public static final double DEGREES_TO_RADIANS =  Math.PI / 180.0;

    public static final double RADIANS_TO_DEGREES =  1 / DEGREES_TO_RADIANS;

    public static double toRadians(double degrees) {
        return degrees * DEGREES_TO_RADIANS;
    }

    public static double toDegrees(double radians) {
        return radians * RADIANS_TO_DEGREES;
    }
    /**
     * 利用多项式拟合法快速求解距离： http://tech.meituan.com/lucene-distance.html.
     * 适用条件：中国范围，距离在300km之内，300km之外也可使用，但精度会有影响.
     */
    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        //1) 计算三个参数
        double dx = lng1 - lng2; // 经度差值
        double dy = lat1 - lat2; // 纬度差值
        double b = (lat1 + lat2) / 2.0; // 平均纬度
        //2) 计算东西方向距离和南北方向距离(单位：米)，东西距离采用三阶多项式，南北采用一阶多项式即可
        double lx = (0.05 * b*b*b - 19.16 * b*b + 47.13 * b + 110966 ) * dx; // 东西距离
        double ly = (17 * b + 110352) * dy; // 南北距离
        //3) 用平面的矩形对角距离公式计算总距离
        return Math.sqrt(lx * lx + ly * ly);
    }

    //根据中心点经纬度+距离反推出查询范围矩形，根据上述计算距离公式反推
    public static com.vividsolutions.jts.geom.Polygon getSpatialSearchRange(double lat, double lng, double dis) { //dis单位是米
        if(dis <= 0.0 || Math.abs(lat) < EPS || Math.abs(lng) < EPS ) {
            return null;
        }
        WKTReader reader = new WKTReader();

        // 1）先求出经度差值
        double dx= dis/(0.05 * lat*lat*lat - 19.16 * lat*lat + 47.13 * lat + 110966 ) + 0.0002; // 故意加上20m范围，预防少取（多取比少取好）
        // 2）接着求出纬度差值
        double dy = dis/(6367000.0*DEGREES_TO_RADIANS) + 0.0001; // 故意加上10m范围
        com.vividsolutions.jts.geom.Polygon polygon = null;
        try {
            polygon = (com.vividsolutions.jts.geom.Polygon)reader.read("POLYGON(("
                    + (lng-dx) + " " + (lat-dy) + ","
                    + (lng+dx) + " " + (lat-dy) + ","
                    + (lng+dx) + " " + (lat+dy) + ","
                    + (lng-dx) + " " + (lat+dy) + ","
                    + (lng-dx) + " " + (lat-dy)
                    + "))");
        } catch (Exception e) {
            logger.error("e->" + e.getMessage(), e);
        }
        return polygon;
    }

    //根据中心点经纬度+距离反推出查询范围矩形，根据上述计算距离公式反推
    public static String getSpatialSearchRangeStr(double lat, double lng, double dis) { //dis单位是米
        if(dis <= 0.0 || Math.abs(lat) < EPS || Math.abs(lng) < EPS ) {
            return null;
        }
        // 1）先求出经度差值
        double dx= dis/(0.05 * lat*lat*lat - 19.16 * lat*lat + 47.13 * lat + 110966 ) + 0.0002; // 故意加上20m范围，预防少取（多取比少取好）
        // 2）接着求出纬度差值
        double dy = dis/(6367000.0*DEGREES_TO_RADIANS) + 0.0001; // 故意加上10m范围
        String polygon = null;
        try {
            polygon = "POLYGON((" + (lng-dx) + " " + (lat-dy) + "," + (lng+dx) + " " + (lat-dy) + "," + (lng+dx) + " " + (lat+dy) + "," + (lng-dx) + " " + (lat+dy) + "," + (lng-dx) + " " + (lat-dy) + "))";
        } catch (Exception e) {
            logger.error("e->" + e.getMessage(), e);
        }
        return polygon;
    }

}
