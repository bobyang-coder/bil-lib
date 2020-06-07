[TOC]
# super pom / parent pom

# bom
## 1.作用？
- 管理jar包版本
- 可以解决依赖冲突

实际上，BOM本质上是一个普通的POM文件，区别是对于使用方而言，生效的只有<dependencyManagement>这一个部分。
只需要在<dependencyManagement>定义对外发布的客户端版本即可
## 2.如何使用BOM？

业务应用使用时，首先需要在pom.xml文件的<dependencyManagement>中，声明对BOM的依赖，然后在实际使用依赖的地方把版本去掉即可。
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.bil</groupId>
            <artifactId>bil-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```


## 参考链接
https://blog.csdn.net/lonelymanontheway/article/details/80623408