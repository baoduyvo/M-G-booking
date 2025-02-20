package com.vtnq.web.Configurations;

import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Configuration
@EnableWebSecurity
public class SercurityConfiguration {
    @Autowired
    private AuthService authService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {return httpSecurity
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/LoginAdmin","/registerUser","/registerOwner","/register","/Login","/css/**","/js/**","/user/**","/SuperAdmin/assets/**",
                                    "/ForgotPassword","/images/flight/**","/images/hotels/**","/images/**","/","/SearchFlight","/Admin/SignatureContract/{id}",
                                    "/DetailHotel/{id}","/InformationCustomer/{id}","/rating","/Payment","/Success","/payFlight/**","/RoundTrip/{id}","/SearchHotelFlight/{id}","/RoundTripHotel/{id}","/HistoryOrderFlight/**","/HistoryOrderHotel/**","/Error","/InformationFlightHotel/**","/InformationFly/**",
                                    "/Contact",
                                    "/About","/ForgotAdminPassword","/HistoryBooking").permitAll()
                            .requestMatchers("SuperAdmin/Home","/SuperAdmin/Country/add","/SuperAdmin/Country"
                            ,"/SuperAdmin/Country/update","/SuperAdmin/AccountAdmin/add"
                            ,"/SuperAdmin/Airline/add","/SuperAdmin/Airline","/SuperAdmin/Airline/edit/{id}",
                                    "/SuperAdmin/Airline/UpdateAirline").hasAnyRole("SUPERADMIN")
                            .requestMatchers("/Admin/Home","/Admin/City/add","/Admin/City","/Admin/City/edit/{id}",
                                    "Admin/City/UpdateCity","Admin/District/{id}",
                                    "Admin/District/add","/Admin/District/edit/{id}","/Admin/District/update","/Admin/District/delete/{id}",
                                    "Admin/AirPort/add","Admin/AirPort","/Admin/AirPort/edit/{id}","/Admin/Flight/add","/Admin/Flight/edit/{id}","/Admin/Flight/UpdateFlight","/Admin/Flight/addSeat","/Admin/Contract", "/Admin/Booking","/Admin/Booking/detail/{id}").hasAnyRole("ADMIN")
                            .requestMatchers("/Owner","/Owner/Hotel/add","/Owner/Hotel","/Owner/Hotel/edit/{id}","/Owner/Hotel/update","/Owner/Hotel/Detail/{id}"
                            ,"/Owner/service/add","/Owner/service/{id}","/Owner/service/edit/{id}","/Owner/service/update","/Owner/Room/{id}","/Owner/Room/add",
                                    "/Owner/Room/edit/{id}","/Owner/Room/update","/Owner/Room/delete/{id}","/Owner/Amenities/{id}","/Owner/Amenities/add","/Owner/Amenities/edit/{id}",
                                    "/Owner/Amenities/update","/Owner/Amenities/delete/{id}","/Owner/Room/addType").hasAnyRole("OWNER")
                            .requestMatchers("/Profile"
                                   ).hasAnyRole("USER")
                            .anyRequest().authenticated();
                })

                .formLogin(form -> form
                        .loginPage("/LoginAdmin")
                        .loginPage("/Login")
                        .loginProcessingUrl("/account/process-login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                String referer = request.getHeader("referer");
                                if(referer.contains("/LoginAdmin")){
                                    HttpSession session = request.getSession();
                                    session.setAttribute("Message", "Login Failed");
                                    session.setAttribute("MessageType", "error");
                                response.sendRedirect(referer);
                                }else if(referer.contains("/Login")){
                                    HttpSession session = request.getSession();
                                    session.setAttribute("Message", "Login Failed");
                                    session.setAttribute("MessageType", "error");
                                    response.sendRedirect(referer);
                                }
                            }
                        })
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
                                Map<String, String> urls = new HashMap<>();
                                urls.put("ROLE_ADMIN", "/Admin/Home");
                                urls.put("ROLE_SUPERADMIN", "/SuperAdmin/Home");
                                urls.put("ROLE_USER", "/");
                                urls.put("ROLE_OWNER", "/Owner");

                                  // Default redirect to error page
                                String email = authentication.getName();
                                Account account = authService.GetAccountByEmail(email);

                                // Store the current account in the session
                                request.getSession().setAttribute("currentAccount", account);

                                // Retrieve the previous page URL from the Referer header
                                String previousPage = request.getHeader("Referer");
                                String redirectUrl = previousPage;
                                if (previousPage != null && previousPage.contains("/LoginAdmin")) {
                                    // Check if the user has ROLE_ADMIN or ROLE_SUPERADMIN
                                    boolean hasValidRole = authorities.stream()
                                            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN") || authority.getAuthority().equals("ROLE_SUPERADMIN"));

                                    if (hasValidRole) {
                                        // If the previous page is /LoginAdmin and the user has the correct role, redirect to the previous page
                                        for (GrantedAuthority authority : authorities) {
                                            String role = authority.getAuthority();
                                            if (urls.containsKey(role)) {
                                                redirectUrl = urls.get(role);
                                                break;
                                            }
                                        }
                                    } else {
                                        // If not valid, redirect to error page
                                        HttpSession session = request.getSession();
                                        session.setAttribute("Message", "Login Failed");
                                        session.setAttribute("MessageType", "error");
                                        redirectUrl = "/LoginAdmin";
                                    }
                                } else if(previousPage != null && previousPage.contains("/Login")) {
                                    boolean hasValidRole = authorities.stream()
                                            .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER") || authority.getAuthority().equals("ROLE_OWNER"));
                                    if (hasValidRole) {
                                        for (GrantedAuthority authority : authorities) {
                                            String role = authority.getAuthority();
                                            if (urls.containsKey(role)) {
                                                redirectUrl = urls.get(role);
                                                break;
                                            }
                                        }
                                    }else{
                                        HttpSession session = request.getSession();
                                        session.setAttribute("Message", "Login Failed");
                                        session.setAttribute("MessageType", "error");
                                        redirectUrl = "/Login";
                                    }
                                }

                                // Redirect to the final URL (either previous page or based on role)
                                HttpSession session = request.getSession();
                                session.setAttribute("Message", "Login Failed");
                                session.setAttribute("MessageType", "error");
                                response.sendRedirect(redirectUrl);

                            }

                        })
                )
            .logout(logout -> logout
                    .logoutUrl("/account/logout")
                    .addLogoutHandler((request, response, authentication) -> {
                        // Get the current account from the session
                        HttpSession session = request.getSession(false); // Get the current session, if any
                        if (session != null) {
                            Account currentAccount = (Account) session.getAttribute("currentAccount");
                            if (currentAccount != null) {
                                // Custom logic with currentAccount
                                try {
                                    if ("ROLE_ADMIN".equals(currentAccount.getAccountType()) || "ROLE_SUPERADMIN".equals(currentAccount.getAccountType())) {
                                        // Redirect to /LoginAdmin
                                        response.sendRedirect("/LoginAdmin");
                                        session.invalidate();
                                        return;
                                    }else if("ROLE_USER".equals(currentAccount.getAccountType()) ) {
                                        response.sendRedirect("/");
                                        session.invalidate();
                                        return;
                                    }else{
                                        response.sendRedirect("/Login");
                                        session.invalidate();
                                        return;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace(); // Log the exception
                                }
                            }
                            // Invalidate session after processing
                            session.invalidate();
                        }
                    })
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        // Handle unauthenticated access (401 error)
                        response.sendRedirect("/Error");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        // Handle access denied (403 error)
                        response.sendRedirect("/Error");
                    })
            )

            .build();
    }



    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    public void globalConfig(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(authService);
    }
}
