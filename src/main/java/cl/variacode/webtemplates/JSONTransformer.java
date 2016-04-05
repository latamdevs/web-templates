package cl.variacode.webtemplates;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JSONTransformer implements ResponseTransformer {

    private interface LazyHolder {
        JSONTransformer INSTANCE = new JSONTransformer(new Gson());
    }

    public static JSONTransformer getInstance() {
        return LazyHolder.INSTANCE;
    }

    private final Gson gson;

    public JSONTransformer(final Gson gson) {
        this.gson = gson;
    }

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}
