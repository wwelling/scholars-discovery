package edu.tamu.scholars.discovery.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Jackson2Helper;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.context.MethodValueResolver;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.auth.controller.request.Registration;

@Service
public class TemplateService {

    private static final Handlebars handlebars = new Handlebars();

    @Value("${ui.url:http://localhost:4200}")
    private String uiUrl;

    @Value("${vivo.base-url:http://localhost:8080/vivo}")
    private String vivoUrl;

    private final ResourceService resourceService;

    public TemplateService(ResourceService resourceService) {
        this.resourceService = resourceService;
        handlebars.with(new HighConcurrencyTemplateCache());
        handlebars.registerHelper("json", Jackson2Helper.INSTANCE);
    }

    @PostConstruct
    public void init() throws Exception {
        InputStream helpers = resourceService.getResource("templates/helpers.js");
        handlebars.registerHelpers("helpers.js", helpers);
    }

    public String template(String template, Object data) {
        try {
            return handlebars.compileInline(template).apply(buildContext(data));
        } catch (IOException e) {
            throw new RuntimeException("Unable to template", e);
        }
    }

    public String templateSparql(String name, String uri) {
        String path = String.format("templates/sparql/%s.sparql", name);
        Map<String, String> data = new HashMap<>();
        data.put("uri", uri);
        Context context = Context.newBuilder(data).build();
        try {
            return handlebars.compileInline(resourceService.getTemplate(path)).apply(context);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to template %s sparql", name), e);
        }
    }

    public String templateConfirmRegistrationMessage(Registration registration, String key) {
        String path = "templates/emails/confirm-registration.html";
        Map<String, Object> data = new HashMap<>();
        data.put("registration", registration);
        data.put("link", String.format("%s?key=%s", uiUrl, key));
        Context context = Context.newBuilder(data).build();
        try {
            return handlebars.compileInline(resourceService.getTemplate(path)).apply(context);
        } catch (IOException e) {
            throw new RuntimeException("Unable to template registration messsage", e);
        }
    }

    private Context buildContext(Object data) {
        return Context.newBuilder(data).resolver(
            JsonNodeValueResolver.INSTANCE,
            JavaBeanValueResolver.INSTANCE,
            FieldValueResolver.INSTANCE,
            MapValueResolver.INSTANCE,
            MethodValueResolver.INSTANCE
        ).build();
    }

}
