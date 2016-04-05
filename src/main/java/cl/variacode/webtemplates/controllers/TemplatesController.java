package cl.variacode.webtemplates.controllers;

import java.util.List;
import java.util.Optional;

import cl.variacode.webtemplates.model.Template;
import cl.variacode.webtemplates.model.TemplatesModel;

public final class TemplatesController {

    private interface LazyHolder {
        TemplatesController INSTANCE = new TemplatesController(TemplatesModel.getInstance());
    }

    public static TemplatesController getInstance() {
        return LazyHolder.INSTANCE;
    }

    private TemplatesModel templatesModel;

    private TemplatesController(final TemplatesModel templatesModel) {
        this.templatesModel = templatesModel;
    }

    public Optional<Template> newTemplate(final String name, final String content) {
        final Optional<Long> newId = templatesModel.insert(name, content);
        return newId.flatMap(templatesModel::getById);
    }

    public List<Template> listTemplates() {
        return templatesModel.selectAllNoneDeleted();
    }



}
