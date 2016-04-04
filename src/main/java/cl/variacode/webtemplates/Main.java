package cl.variacode.webtemplates;

import static java.lang.String.format;
import static spark.Spark.before;
import static spark.Spark.externalStaticFileLocation;

import spark.Spark;

public class Main {

    public static void main(final String[] args) {
        // staticFileLocation("/META-INF/resources");
        externalStaticFileLocation(format("%s/web-templates-ui", System.getProperty("user.dir")));
        before(SparkWebJars.getInstance());
        Spark.init();

    }

}
