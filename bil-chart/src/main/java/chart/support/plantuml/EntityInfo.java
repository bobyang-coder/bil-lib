package chart.support.plantuml;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sourceforge.plantuml.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 实体依赖信息
 *
 * @author haibo.yang
 * @since 2022/6/1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityInfo {

    /**
     * 实体名称
     */
    public String entityName;

    /**
     * 实体描述
     */
    private String entityDesc;

    /**
     * 颜色
     */
    private String color;

    /**
     * 依赖关系
     */
    public List<DependentItem> dependents;


    /**
     * 依赖项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DependentItem {

        /**
         * 实体名称
         */
        public String entityName;

        /**
         * 关系描述
         */
        public String relationDesc;
    }


    public String toPlantuml() {
        StringBuilder builder = new StringBuilder();
        for (DependentItem r : dependents) {
            //[A] -> [B] : desc
            String relation = String.format("[%s] -down-> [%s] : %s \n", r.getEntityName(), entityName, r.getRelationDesc());
            builder.append(relation);
        }
        return builder.toString();
    }

    public static void generatePlantuml(List<EntityInfo> infoList) {
        StringBuilder relationInfoBuilder = new StringBuilder();
        StringBuilder componentConfigInfoBuilder = new StringBuilder();
        for (EntityInfo info : infoList) {
            //组件配置
            if (StringUtils.isNotBlank(info.getColor())) {
                String componentConfig = String.format("[%s] %s\n", info.getEntityName(), info.getColor());
                componentConfigInfoBuilder.append(componentConfig);
            }
            if (StringUtils.isNotBlank(info.getEntityDesc())) {
                String componentNote = String.format("note right of [%s]\n %s \n end note \n", info.getEntityName(), info.getEntityDesc());
                componentConfigInfoBuilder.append(componentNote);
            }
            //组件关系
            relationInfoBuilder.append(info.toPlantuml());
        }
        StringBuilder builder = new StringBuilder("@startuml\n");
        builder.append(componentConfigInfoBuilder.toString()).append("\n");
        builder.append(relationInfoBuilder.toString()).append("\n");
        builder.append("@enduml");
        String s = builder.toString();
        System.out.println(s);
        PlantumlUtil.generatePlantUmlImage(s);
    }

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/haibo.yang/Documents/github/bil-framework/bil-tools/src/main/java/com/bil/tools/util/entity_dependent_info.json");
//        ClassPathResource resource = new ClassPathResource("com/bil/tools/util/entity_dependent_info.json");
        String content = FileUtils.readText(new FileInputStream(file));
        List<EntityInfo> dependentInfoList = JSON.parseArray(content, EntityInfo.class);
        generatePlantuml(dependentInfoList);
    }
}
