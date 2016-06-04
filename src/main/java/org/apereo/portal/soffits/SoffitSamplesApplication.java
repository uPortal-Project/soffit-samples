package org.apereo.portal.soffits;

import org.apereo.portlet.soffit.renderer.SoffitRendererController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SoffitSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoffitSamplesApplication.class, args);
    }

    /**
     * Enables soffits within this application.
     */
    @Bean
    public SoffitRendererController soffitRendererController() {
        return new SoffitRendererController();
    }

}
