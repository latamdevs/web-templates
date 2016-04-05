package cl.variacode.webtemplates;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private interface LazyHolder {
        JsonTransformer INSTANCE = new JsonTransformer(new Gson());
    }

    public static JsonTransformer getInstance() {
        return LazyHolder.INSTANCE;
    }

    private final Gson gson;

    private JsonTransformer(final Gson gson) {
        this.gson = gson;
    }

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}
