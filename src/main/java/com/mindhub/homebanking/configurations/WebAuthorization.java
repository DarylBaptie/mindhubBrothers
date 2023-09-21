package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

    @EnableWebSecurity

    @Configuration

    public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {



        http.authorizeRequests()

                .antMatchers("/index.html", "/clientRegistration.html", "/clientRegistration.css", "/clientRegistration.js", "/pages/images/**", "/images/**", "/index.js", "/indexStyles.css", "/login.html", "/login.js", "/login.css").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers(HttpMethod.POST, "/api/createLoan").hasAuthority("ADMIN")
                .antMatchers("/rest/**", "/admin/**", "/api/login", "/api/clients", "/h2-console", "/manager.html", "/manager.js", "/newLoanForm.html", "/newLoanForm.css", "/newLoanForm.js").hasAuthority("ADMIN")
                .antMatchers("/web/**", "/api/login", "/api/clients/current/cards", "/api/clients/current", "/api/accounts/**", "/api/loans", "/api/clients/current/transactions/pdf").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts", "/api/clients/current/cards", "/api/clients/current/transactions", "/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/api/clients/current/cards/deactivate", "/api/clients/current/accounts/close", "/api/clients/current/loans/loanPayment").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/card/transactions").permitAll()
                .anyRequest().denyAll();


        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout");

        http.csrf().disable();

        http.headers().frameOptions().disable();

        http.cors();

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }


    }

}



