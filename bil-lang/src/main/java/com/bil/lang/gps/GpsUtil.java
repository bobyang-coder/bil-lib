package com.bil.lang.gps;


import lombok.Data;

import java.io.Serializable;

/**
 * 坐标系转换工具类
 *
 * @author haibo.yang <bobyang_coder@163.com>
 * @since 2018/9/27
 */
public class GpsUtil {


    private static final double PI = 3.1415926535897932384626;
    private static final double X_PI = 3.14159265358979324 * 3000.0 / 180.0;
    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0
                * PI)) * 2.0 / 3.0;
        return ret;
    }

    private static Location transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return Location.of(lon, lat);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return Location.of(mgLon, mgLat);
    }

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        return lat < 0.8293 || lat > 55.8271;
    }

    /**
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param lat
     * @param lon
     * @return
     */
    public static Location gps84ToGcj02(double lat, double lon) {
        return transform(lat, lon);
    }

    /**
     * 火星坐标系 (GCJ-02) to 84
     *
     * @param lat
     * @param lon
     * @return
     */
    public static Location gcj02ToGps84(double lat, double lon) {
        Location location = transform(lat, lon);
        double longitude = lon * 2 - location.getLng();
        double latitude = lat * 2 - location.getLat();
        return Location.of(longitude, latitude);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    public static Location gcj02ToBd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        return Location.of(tempLon, tempLat);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法
     * 将 BD-09 坐标转换成GCJ-02 坐标
     *
     * @param lat
     * @param lon
     * @return
     */
    public static Location bd09ToGcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        return Location.of(tempLon, tempLat);
    }

    /**
     * 将gps84转为bd09
     *
     * @param lat
     * @param lon
     * @return
     */
    public static Location gps84ToBd09(double lat, double lon) {
        Location gcj02 = gps84ToGcj02(lat, lon);
        return gcj02ToBd09(gcj02.getLat(), gcj02.getLng());
    }

    public static Location bd09ToGps84(double lat, double lon) {
        Location gcj02 = bd09ToGcj02(lat, lon);
        Location gps84 = gcj02ToGps84(gcj02.getLat(), gcj02.getLng());
        //保留小数点后六位
        gps84.setLat(retain6(gps84.getLat()));
        gps84.setLng(retain6(gps84.getLng()));
        return gps84;
    }

    /**
     * 保留小数点后六位
     *
     * @param num
     * @return
     */
    private static double retain6(double num) {
        String result = String.format("%.6f", num);
        return Double.parseDouble(result);
    }


    /**
     * 坐标信息
     */
    @Data
    public static class Location implements Serializable {

        /**
         * 经度
         */
        private double lng;

        /**
         * 纬度
         */
        private double lat;


        public static Location of(double lng, double lat) {
            if (lng < -180 || lng > 180 || lat < -90 || lat > 90) {
                throw new RuntimeException(String.format("construct Location error. lng: %s, lat: %s", lng, lat));
            }
            Location location = new Location();
            location.setLng(lng);
            location.setLat(lat);
            return location;
        }
    }
}


