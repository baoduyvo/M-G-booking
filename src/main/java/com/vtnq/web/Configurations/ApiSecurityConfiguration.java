package com.vtnq.web.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class ApiSecurityConfiguration {

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth.
                        requestMatchers("/api/ForgetPassword",
                                "/api/CheckOTP",
                                "/api/ChangePassword",
                                "/api/District/{id}",
                                "/api/hotel/DeletePictureImage/{id}",
                                "/api/hotel/UpdateMultipleImage/{id}",
                                "/api/AirPort/SearchAirPort",
                                "/api/ContractOwner/AcceptContract",
                                "/api/city/SearchHotelByCityOrHotel",
                                "/api/room/UpdateMultipleImage/{id}",
                                "/api/room/DeletePictureImage/{id}",
                                "/api/type/FindTypeByHotel/{id}",
                                "/api/seat/{id}",
                                "/api/Country/All",
                                "/api/city/FindCityByCountry/{id}",
                                "/api/AirPort/FindById/{id}",
                                "/api/account/register/user",
                                "/api/Flight/detail/{id}",
                                "/api/seat/existBySeat/{id}",
                                "/api/city/FindById/{id}",
                                "/api/Amenity/{id}",
                                "/api/room/picture/{id}",
                                "/api/Flight/getFlight",
                                "/api/hotel/SearchHotel",
                                "/api/account/Login",
                                "api/room/GetRoomByHotel"
                                ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // This is still supported for versions below 6.1
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow only trusted origins (replace with actual front-end URLs in production)
        configuration.setAllowedOrigins(List.of("http://localhost:8686", "https://your-production-frontend.com"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies, authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
