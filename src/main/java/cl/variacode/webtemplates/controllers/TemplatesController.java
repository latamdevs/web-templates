package cl.variacode.webtemplates.controllers;

public class TemplatesController {

    private interface LazyHolder {
        TemplatesController INSTANCE = new TemplatesController();
    }

    public static TemplatesController getInstance() {
        return LazyHolder.INSTANCE;
    }

}
