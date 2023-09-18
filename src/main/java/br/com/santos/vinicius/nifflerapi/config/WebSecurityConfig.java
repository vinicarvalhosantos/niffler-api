package br.com.santos.vinicius.nifflerapi.config;

import br.com.santos.vinicius.nifflerapi.filter.AuthenticationFilter;
import br.com.santos.vinicius.nifflerapi.filter.MyAuthenticationEntryPoint;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    final UserDetailsService userDetailsService;

    final AuthenticationFilter authenticationFilter;

    public WebSecurityConfig(MyAuthenticationEntryPoint myAuthenticationEntryPoint, UserDetailsService userDetailsService, AuthenticationFilter authenticationFilter) {
        Assert.notNull(myAuthenticationEntryPoint, "MyAuthenticationEntryPoint must not be null");
        Assert.notNull(userDetailsService, "UserDetailsService must not be null");
        Assert.notNull(authenticationFilter, "AuthenticationFilter must not be null");
        this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/v2/user-auth/authenticate").permitAll()
                //.antMatchers("/v2/user-auth/register").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated().and().exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("**"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
