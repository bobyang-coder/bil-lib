package chart;

/**
 * 图表类型枚举
 *
 * @author haibo.yang
 * @since 2024/1/30
 */
public enum ChartTypeEnum {
    C01_GANTT("gantt", "甘特图"),
    C02_ACTIVITY("activity", "活动图"),
    ;

    private String code;

    private String desc;

    ChartTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
