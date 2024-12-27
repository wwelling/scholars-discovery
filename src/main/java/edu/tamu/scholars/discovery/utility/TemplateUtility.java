package edu.tamu.scholars.discovery.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Jackson2Helper;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;

public class TemplateUtility {

    private static final Handlebars HANDLEBARS = new Handlebars();

    private TemplateUtility() throws Exception {
        InputStream helpers = ResourceUtility.getResource("templates/helpers.js");

        HANDLEBARS.with(new HighConcurrencyTemplateCache());
        HANDLEBARS.registerHelper("json", Jackson2Helper.INSTANCE);
        HANDLEBARS.registerHelpers("helpers.js", helpers);
    }

    public static String templateSparql(String template, String uri) throws IOException {
        Context context = Context.newBuilder(Map.of("uri", uri))
                .build();

        return HANDLEBARS.compileInline(template)
                .apply(context);
    }

}
