package chart.support.plantuml;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * plantuml 工具类
 *
 * @author haibo.yang
 * @since 2022/6/1
 */
@Slf4j
public class PlantumlUtil {


    public static File generatePlantUmlImage(String plantumlContent) {
        if (StringUtils.isBlank(plantumlContent)) {
            return null;
        }
        File file = new File(System.currentTimeMillis() + ".png");
        try (OutputStream outputStream = new FileOutputStream(file)) {
            SourceStringReader reader = new SourceStringReader(plantumlContent);
            //这里生成图片 只能支持 plantuml 的时序图和新活动图，其他类型的图需要依赖 Graphviz
            DiagramDescription diagramDescription = reader.outputImage(outputStream, new FileFormatOption(FileFormat.PNG));
            log.info("生成image图片-{}", diagramDescription.getDescription());
        } catch (Exception e) {
            log.error("发送plantuml图片执行异常", e);
        }
        return file;
    }

    public static void main(String[] args) {
        String source = "@startuml\n" +
                "skinparam componentStyle rectangle\n" +
                "\n" +
                "interface \"Data Access\" as DA\n" +
                "\n" +
                "DA - [First Component]\n" +
                "[First Component] ..> HTTP : use\n" +
                "\n" +
                "@enduml";
        generatePlantUmlImage(source);
    }
}
