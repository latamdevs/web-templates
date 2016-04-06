package cl.variacode.webtemplates;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public final class EvalServlet extends HttpServlet {

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

    private final Configuration configuration;
    private final Gson gson;
    private final Logger logger;

    public EvalServlet() {
        logger = Logger.getLogger(this.getClass().getName());
        gson = new Gson();
        configuration = newConfiguration();
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String rawTemplate = getParameter(request, "template", "");
        final String context = getParameter(request, "context", "");

        if (isEmpty(rawTemplate)) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        final Template template = parseTemplate(rawTemplate, configuration);
        final Map<String, Object> model = gson.fromJson(context, Map.class);

        final Writer out = new OutputStreamWriter(response.getOutputStream());

        try {
            template.process(model, out);
        } catch (final TemplateException e) {
            logger.log(Level.SEVERE, "error while processing template", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String getParameter(final HttpServletRequest request, final String paramName, final String defaultValue) {
        return defaultIfNull(request.getParameter(paramName), defaultValue);
    }

    private boolean isEmpty(final String string) {
        return null == string || string.trim().isEmpty();
    }

    private <T> T defaultIfNull(T... candidates) {
        for (T candidate : candidates) {
            return candidate;
        }

        return null;
    }

    private Template parseTemplate(final String rawTemplate, final Configuration configuration) throws IOException {
        // this fixes list
        final String template = rawTemplate
                .replaceAll("&lt;#else&gt;", "<#else>")
                .replaceAll("&lt;#sep&gt;", "<#sep>")
                .replaceAll("&lt;#/sep&gt;", "</#sep>")
                .replaceAll("&lt;#list (.*?)&gt;", "<#list $1>")
                .replaceAll("&lt;#/list&gt;", "</#list>");


        return new Template("name", new StringReader(template), configuration);
    }


}
