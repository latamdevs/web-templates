package cl.variacode.webtemplates;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;
import static spark.Spark.before;
import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.post;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class Main {

    public static void main(final String[] args) {
        final Gson gson = new Gson();
        final JsonTransformer json = JsonTransformer.getInstance();
        final SparkWebJars webjars = SparkWebJars.getInstance();
        final Configuration configuration = newConfiguration();

        final String webappDir = format("%s/web-templates-ui", System.getProperty("user.dir"));

        externalStaticFileLocation(webappDir);

        before(webjars);

        post("/api/eval", (request, response) -> {
            final String rawTemplate = firstNonNull(request.queryParams("template"), "");
            final String context = firstNonNull(request.queryParams("context"), "{}");

            if (isNullOrEmpty(rawTemplate)) {
                return "";
            }

            final Template template = parseTemplate(rawTemplate, configuration);
            final Map<String, Object> model = gson.fromJson(context, Map.class);

            final HttpServletResponse raw = response.raw();
            final Writer out = new OutputStreamWriter(raw.getOutputStream());

            template.process(model, out);

            return null;
        }, json);
    }

    private static Template parseTemplate(final String rawTemplate, final Configuration configuration) throws IOException {
        // this fixes list
        final String template = rawTemplate
                .replaceAll("&lt;#list (.*?)&gt;", "<#list $1>")
                .replaceAll("&lt;#else&gt;", "<#else>")
                .replaceAll("&lt;#/list&gt;", "</#list>");

        return new Template("name", new StringReader(template), configuration);
    }

    private static Configuration newConfiguration() {
        // Create your Configuration instance, and specify if up to what FreeMarker
        // version (here 2.3.24) do you want to apply the fixes that are not 100%
        // backward-compatible. See the Configuration JavaDoc for details.
        Configuration cfg = new Configuration(new Version("2.3.23"));

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(true);

        return cfg;
    }

}
