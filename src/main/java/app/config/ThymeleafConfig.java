package app.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafConfig
{
    public static TemplateEngine templateEngine()
    {
        // create the Thymeleaf engine (processes templates)
        TemplateEngine templateEngine = new TemplateEngine();

        // create resolver to find template files
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        // set folder where templates are located (resources/templates/)
        templateResolver.setPrefix("templates/");

        // set file extension for templates
        templateResolver.setSuffix(".html");

        // connect resolver to engine so it can find templates
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}