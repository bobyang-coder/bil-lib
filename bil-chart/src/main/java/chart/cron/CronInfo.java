package chart.cron;

import chart.gantt.GanttTaskItem;
import cn.hutool.cron.pattern.CronPatternUtil;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 又忘记写注释了？
 *
 * @author haibo.yang
 * @since 2023/11/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CronInfo {

    private String corn;

    private String name;

    @ApiModelProperty(value = "每天执行点")
    private List<String> executePointDay;

    public static List<CronInfo> parse(String text) {
        String[] array = text.split("\n");
        List<CronInfo> cronInfoList = Lists.newArrayList();
        for (String line : array) {
            line = line.replace("USERDEFINE:", "")
                    .replace("EVERYDAY:", "")
                    .replace("EVERYWEEK:", "")
                    .replace("EVERYMONTH:", "");
            String[] split = line.split("::");
            CronInfo cronInfo = CronInfo.builder()
                    .corn(split[0])
                    .name(split[1])
                    .executePointDay(parseExecutePointDay(split[0]))
                    .build();
            cronInfoList.add(cronInfo);
        }
        return cronInfoList;
    }


    private static final LocalDateTime start = LocalDateTime.parse("20231001", ISODateTimeFormat.basicDate());
    private static final LocalDateTime end = start.plusDays(1);
    private static final LocalDateTime endWeek = start.plusDays(7);
    private static final LocalDateTime endMonth = start.plusMonths(1);

    public static List<String> parseExecutePointDay(String corn) {
        List<Date> dateList = CronPatternUtil.matchedDates(corn, start.toDate(), end.toDate(), 1000, false);
        if (CollectionUtils.isEmpty(dateList)) {
            dateList = CronPatternUtil.matchedDates(corn, start.toDate(), endWeek.toDate(), 1, false);
        }
        if (CollectionUtils.isEmpty(dateList)) {
            dateList = CronPatternUtil.matchedDates(corn, start.toDate(), endMonth.toDate(), 1, false);
        }
        return dateList.stream()
                .map(CronInfo::formatDate)
                .collect(Collectors.toList());
    }


    public static String text = "USERDEFINE:0 50 0/5 * * ?::下载电子回单pdf文件\n" +
            "USERDEFINE:0 2 7,13 * * ?::同步银行流水\n" +
            "EVERYDAY:0 30 23 * * ?::付款单自动同步\n" +
            "EVERYDAY:0 30 10 ? * 2-6::付款退票邮件通知任务\n" +
            "EVERYWEEK:0 18 4 ? * 1::推送供应商主数据行名行号数据任务\n" +
            "EVERYDAY:0 0 13 * * ?::从业财按日同步汇率\n" +
            "EVERYDAY:0 30 8 * * ?::报销类付款单自动提交银企任务\n" +
            "USERDEFINE:0 0/30 * * * ?::银行付款单同步状态\n" +
            "USERDEFINE:0 15 0/1 * * ?::银行付款单同步状态（花旗）\n" +
            "EVERYMONTH:0 30 2 20 1,2,3,4,5,6,7,8,9,10,11,12 ?::行名行号\n" +
            "USERDEFINE:0 0 1/6 * * ?::中银香港下载电子回单PDF文件\n" +
            "EVERYDAY:0 40 22 * * ?::AD域人员同步\n" +
            "EVERYMONTH:6 6 6 10 1,2,3,4,5,6,7,8,9,10,11,12 ?::凭证数据推送海波龙中间库\n" +
            "EVERYDAY:0 0 2 * * ?::用户行为分析\n" +
            "EVERYDAY:0 0 22 * * ?::水滴资金自动调拨任务1级\n" +
            "USERDEFINE:0 0 0/4 * * ?::交易明细匹配业务单据任务\n" +
            "USERDEFINE:0 30 0/3 * * ?::余额和交易明细查询\n" +
            "EVERYDAY:0 0 1 ? * 2-6::AD域PS部门同步\n" +
            "EVERYDAY:30 10 3 ? * 2-6::同步凭证匹配电子回单标记任务\n" +
            "USERDEFINE:0 30 0/12 * * ?::更新交易明细生成凭证标记任务\n" +
            "EVERYDAY:0 40 7 * * ?::定时执行系统内部日任务\n" +
            "USERDEFINE:0 20 0/2 * * ?::电子回单与交易明细匹配的后台事务\n" +
            "EVERYDAY:30 30 4 * * ?::业财凭证匹配电子回单任务\n" +
            "EVERYDAY:0 0 23 * * ?::水滴资金自动调拨任务2级\n" +
            "EVERYDAY:0 35 23 * * ?::零点前余额和交易明细查询\n" +
            "EVERYDAY:0 10 14 ? * 2-6::资金闲置邮件提醒任务\n" +
            "USERDEFINE:* 5 1/3 * * ?::电子回单与交易明细匹配任务(非银企账号)\n" +
            "EVERYDAY:0 40 3 ? * 2-6::推送海波龙成本中心数据任务\n" +
            "USERDEFINE:0 33 0/5 * * ?::下载电子回单结构性数据\n" +
            "EVERYDAY:45 51 0 * * ?::非OA系统相关付款信息同步\n" +
            "EVERYDAY:0 0 3 * * ? *::~后台事务转储";


    private static String formatDate(Date date) {
        if (null == date) {
            return "5m";
        }
        return LocalDateTime.fromDateFields(date).toString("HH:mm:ss");
    }

    public static String genDayGanttText() {
        List<CronInfo> cronInfoList = parse(text);
        cronInfoList.sort(Comparator.comparing(o -> o.getExecutePointDay().get(0)));
        List<GanttTaskItem> itemList = Lists.newArrayList();

        for (CronInfo cronInfo : cronInfoList) {
            List<String> dateList = cronInfo.getExecutePointDay();
            System.out.println("解析" + cronInfo.getCorn() + cronInfo.getName() + ":" + dateList.size());
            if (dateList.size() <= 5 && dateList.size() > 1) {
                for (String date : dateList) {
                    GanttTaskItem taskItem = GanttTaskItem.builder()
                            .sectionName(cronInfo.getName())
                            .taskName(cronInfo.getName() + "(" + cronInfo.getCorn() + ")")
                            .style(null)
                            .status("active")
                            .startDate(date)
                            .endDate("10m")
                            .build();
                    itemList.add(taskItem);
                }
                continue;
            }

            String style = null;
            String startDate = Iterables.getFirst(dateList, null);
            String endDate = null;
            if (dateList.size() > 1) {
//                style = "milestone";
                endDate = Iterables.getLast(dateList);
            } else {
                style = "milestone";
            }


            GanttTaskItem taskItem = GanttTaskItem.builder()
                    .sectionName(cronInfo.getName())
                    .taskName(cronInfo.getName() + "(" + cronInfo.getCorn() + ")")
                    .style(style)
                    .status("active")
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
            itemList.add(taskItem);
        }
//        itemList.sort(Comparator.comparing(GanttTaskItem::getSectionName).thenComparing(GanttTaskItem::getStartDate));
//        itemList.sort(Comparator.comparing(GanttTaskItem::getStartDate).thenComparing(GanttTaskItem::getStartDate));
        return GanttTaskItem.toGanttTextByDay(itemList);
    }

    public static void main(String[] args) {
        System.out.println(genDayGanttText());
    }
}
