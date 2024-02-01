package chart.gantt;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;

import java.util.Date;
import java.util.List;

/**
 * 甘特图 - 任务项
 *
 * @author haibo.yang
 * @since 2023/11/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GanttTaskItem {

    @ApiModelProperty("section名称")
    private String sectionName;

    @ApiModelProperty("任务名称")
    private String taskName;


    private String style;

    private String status;

    private String startDate;

    private String endDate;


    public void append(StringBuilder sb) {
        sb.append("\n");
        //section
        if (StringUtils.isNotBlank(sectionName)) {
            sb.append("section ").append(sectionName).append("\n");
        }
        sb.append(taskName)
                .append(" :")
                .append(this.addComma(style))
                .append(this.addComma(status))
                .append(this.addComma(startDate))
                .append(this.addComma(endDate))
                .replace(sb.length() - 1, sb.length(), "")
                .append("\n");
    }

    public String addComma(String value) {
        if (StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }
        return value + ",";
    }

    public GanttTaskItem copy(Date start, Date end) {
        String startDate = LocalDateTime.fromDateFields(start).toString("HH:mm");
        String endDate = LocalDateTime.fromDateFields(end).toString("HH:mm");
        return copy(startDate, endDate);
    }

    public GanttTaskItem copy(String startDate, String endDate) {
        return GanttTaskItem.builder()
                .sectionName(this.sectionName)
                .taskName(this.taskName)
                .style(this.style)
                .status(this.status)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    /**
     * 按日的甘特图
     *
     * @param itemList
     * @return
     */
    public static String toGanttTextByDay(List<GanttTaskItem> itemList) {
        StringBuilder sb = new StringBuilder();
        sb.append("gantt\n");
        sb.append("dateFormat HH:mm:ss\n");
        sb.append("axisFormat %H:%M:%S\n");
        for (GanttTaskItem item : itemList) {
            item.append(sb);
        }
        return sb.toString();
    }
}
