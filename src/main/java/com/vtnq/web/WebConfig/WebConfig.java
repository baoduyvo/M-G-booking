package com.vtnq.web.WebConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map the URL '/images/**' to the file system location of uploaded images
        registry.addResourceHandler("/images/hotels/**")  // Path you want to access the images via URL
                .addResourceLocations("file:src/main/resources/static/images/hotels/"); // Physical path on the server

        // You can add more directories if necessary, for example, for other image types
        registry.addResourceHandler("/images/flight/**")
                .addResourceLocations("file:src/main/resources/static/images/flight/");
        registry.addResourceHandler("/images/rooms/**")  // Path you want to access the images via URL
                .addResourceLocations("file:src/main/resources/static/images/rooms/");
        registry.addResourceHandler("/images/users/**")
                .addResourceLocations("file:src/main/resources/static/images/users/");
    }
}

