package cl.variacode.webtemplates.routes;

import static com.sun.tools.doclint.Entity.para;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Optional;

import cl.variacode.webtemplates.JSONTransformer;
import cl.variacode.webtemplates.controllers.TemplatesController;
import spark.Request;

public class TemplatesRoutes {

    private interface LazyHolder {
        TemplatesRoutes INSTANCE = new TemplatesRoutes(TemplatesController.getInstance(), JSONTransformer.getInstance());
    }

    public static TemplatesRoutes getInstance() {
        return LazyHolder.INSTANCE;
    }

    private final TemplatesController templatesController;
    private final JSONTransformer jsonTransformer;

    private TemplatesRoutes(final TemplatesController templatesController, final JSONTransformer jsonTransformer) {
        this.templatesController = templatesController;
        this.jsonTransformer = jsonTransformer;
    }

    public void attach() {

        post("/templates", (request, response) -> {
            return templatesController.newTemplate("nombre", "contenido").orElse(null);
        }, jsonTransformer);

        get("/templates", (request, response) -> {
            return templatesController.listTemplates();
        }, jsonTransformer);

        get("/template/:id", (request, response) -> {
            final String idParam = request.params(":id");

            2) para luego preguntar por el id al controlador,

            return templatesController.findById(0);
        }, jsonTransformer);

        delete("/template/:id", (request, response) -> {
            final String idParam = request.params(":id");

            2) para luego preguntar por el id al controlador,

            return templatesController.deleteById(0);
        }, jsonTransformer);

    }

    private Optional<Long> getIdParam(final Request request) {
        final String paramName = ":id";
        final String paramValue = request.params(paramName);

        1) la idea es obtener aca el id

        if (())

    }

}
