package chart.support.graphviz;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.attribute.Label.Justification.LEFT;

/**
 * TODO 又忘记写注释了？
 *
 * @author haibo.yang
 * @since 2024/1/29
 */
public class Demo {

    public static void main(String[] args) throws IOException {
        demo1();
        demo2();
    }

    public static void demo1() throws IOException {
        Graph g = Factory.graph("example1").directed()
                .graphAttr().with(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .with(
                        Factory.node("a").with(Color.RED).link(Factory.node("b")),
                        Factory.node("b").link(
                                Factory.to(Factory.node("c")).with(Attributes.attr("weight", 5), Style.DASHED)
                        )
                );
        Graphviz.fromGraph(g).height(100).render(Format.PNG).toFile(new File("example/ex1.png"));
    }

    public static void demo2() throws IOException {
        Node main = Factory.node("main").with(Label.html("<b>main</b><br/>start"), Color.rgb("1020d0").font()),
                init = Factory.node(Label.markdown("**_init_**")),
                execute = Factory.node("execute"),
                compare = Factory.node("compare").with(Shape.RECTANGLE, Style.FILLED, Color.hsv(.7, .3, 1.0)),
                mkString = Factory.node("mkString").with(Label.lines(LEFT, "make", "a", "multi-line")),
                printf = Factory.node("printf");

        Graph g = Factory.graph("example2").directed().with(
                main.link(
                        Factory.to(Factory.node("parse").link(execute)).with(LinkAttr.weight(8)),
                        Factory.to(init).with(Style.DOTTED),
                        Factory.node("cleanup"),
                        Factory.to(printf).with(Style.BOLD, Label.of("100 times"), Color.RED)),
                execute.link(
                        Factory.graph().with(mkString, printf),
                        Factory.to(compare).with(Color.RED)),
                init.link(mkString));

        Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("example/ex2.png"));
    }
}
