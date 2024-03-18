package com.example.demo.configuration;

import com.example.demo.security.jwt.JwtEntryPoint;
import com.example.demo.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // todo: inject qua contructor dùng annotation @RequiredArgsConstructor
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEntryPoint jwtEntryPoint;


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager Bean
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder); // cung cấp password encoder
    }

    // todo: Dạnh sách URL api dùng để khi cần phân quyền
    private static final String[] AUTH_USER = {
            "/api/cart/**",
            "/product/get",
            "/product/view-detail/*",
            "/api/v1/product/**",
            "/api/v1/order/**"
    };

    private static final String[] AUTH_ADMIN= {
            "/api/v1/product/**",
            "/api/v1/order/**"
    };

    // danh sách api các role được toàn quyền truy cập
    private static final String[] AUTH_WHITELIST= {
            "/api/cart/**",
            "/api/auth/**",
            "/api/auth/login",
            "/api/v1/product/search",
            "/api/vnPay/**",
            "/api/**",
            "/api/v1/product/**",
            "/api/v1/order/**",
    };

    private static final String[] AUTH_STAFF= {
            "/product/create",
            "/product/update-new/*",
            "/product/delete/*",
            "/api/v1/product/**",
            "/api/v1/order/**",
    };

    private static final String[] AUTH_CUSTOMER= {
            "/api/v1/product/search",
            "/api/v1/product/**",
            "/api/**",
            "/api/v1/product/**",
            "/api/v1/order/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(AUTH_ADMIN).hasAnyAuthority("ADMIN")
                .antMatchers(AUTH_USER).hasAnyAuthority("USER")
                .antMatchers(AUTH_STAFF).hasAnyAuthority("STAFF")
                .antMatchers(AUTH_CUSTOMER).hasAnyAuthority("CUSTOMER")
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}
