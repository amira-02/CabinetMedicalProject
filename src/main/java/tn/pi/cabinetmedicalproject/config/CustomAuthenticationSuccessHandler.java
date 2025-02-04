package tn.pi.cabinetmedicalproject.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin"); // Redirection vers la page admin
        } else if (roles.contains("ROLE_DOCTOR")) {
            response.sendRedirect("/index");
        } else if (roles.contains("ROLE_PATIENT")) {
            response.sendRedirect("/Home");
        } else {
            response.sendRedirect("/"); // Redirection par défaut
        }
    }

}
