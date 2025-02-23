package tn.pi.cabinetmedicalproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import tn.pi.cabinetmedicalproject.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers(
//                        "/registration**",
//                        "/js/**",
//                        "/css/**",
//                        "/img/**").permitAll()
//                .antMatchers("/admine").hasAuthority("ADMIN")
////
//                .antMatchers("/index").hasAuthority("ROLE_DOCTOR")
//                .antMatchers("/pharmacyhome").hasAuthority("ROLE_PHARMACY")
//                .antMatchers("/patienthome").hasAuthority("ROLE_PATIENT")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .successHandler(successHandler) // Gestionnaire de succès personnalisé
//                .permitAll()
//                .and()
//                .logout()
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/login?logout")
//                .permitAll();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // Autoriser l'accès sans authentification à ces pages
                .antMatchers("/home", "/login", "/registration**", "/js/**", "/static/css/**", "/img/**", "/images/**")
                .permitAll()
                // Restreindre l'accès à certaines pages en fonction des rôles
                .antMatchers("/admine").hasAuthority("ADMIN")
                .antMatchers("/doctorhome").hasAuthority("ROLE_DOCTOR")
                .antMatchers("/pharmacyhome").hasAuthority("ROLE_PHARMACY")
                .antMatchers("/patienthome").hasAuthority("ROLE_PATIENT")
                .antMatchers("/patient/**").permitAll() // Test temporaire

                // Toutes les autres pages nécessitent une authentification
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(successHandler)  // Gestionnaire de succès personnalisé
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }


}