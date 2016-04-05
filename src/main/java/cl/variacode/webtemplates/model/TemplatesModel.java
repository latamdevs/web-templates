package cl.variacode.webtemplates.model;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public final class TemplatesModel {

    private interface LazyHolder {
        TemplatesModel INSTANCE = new TemplatesModel();
    }

    public static TemplatesModel getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Optional<Long> insert(final String name, final String content) {
        return null;
    }

    public Optional<Template> getById(final long id) {
        return Optional.ofNullable(new Template("espero", "sea lo que querias"));
    }

    public List<Template> selectAllNoneDeleted() {
        return IntStream.range(0, 5)
                .mapToObj(i -> {
                    final int index =  1 + i;

                    final String name = format("name-%d", index);
                    final String content = format("content-%d", index);

                    return new Template(name, content);
                })
                .collect(toList());

    }

}
