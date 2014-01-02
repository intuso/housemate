package com.intuso.housemate.server.dependencygraph;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.grapher.GrapherModule;
import com.google.inject.grapher.InjectorGrapher;
import com.google.inject.grapher.graphviz.GraphvizModule;
import com.google.inject.grapher.graphviz.GraphvizRenderer;
import com.intuso.housemate.server.ServerModule;
import com.intuso.housemate.server.PCModule;
import com.intuso.housemate.server.storage.impl.SjoerdDBModule;
import com.intuso.utilities.log.Log;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 25/11/13
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class DependencyGraph {

    @Test
    public void graph() throws IOException {
        File outputFile = File.createTempFile("server-graph", ".dot");
        System.out.println("Creating graph in " + outputFile.getAbsolutePath());

        PrintWriter out = new PrintWriter(outputFile, "UTF-8");

        Injector injector = Guice.createInjector(new GrapherModule(), new GraphvizModule());
        GraphvizRenderer renderer = injector.getInstance(GraphvizRenderer.class);
        renderer.setOut(out).setRankdir("TB");

        injector.getInstance(InjectorGrapher.class)
                .of(Guice.createInjector(
                        new PCModule(new Log("Housemate"), Maps.<String, String>newHashMap()),
                        new SjoerdDBModule(),
                        new ServerModule()))
                .graph();
    }
}
