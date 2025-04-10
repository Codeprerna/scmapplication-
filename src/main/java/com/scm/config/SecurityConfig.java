package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   
    // user create and login page using java code with in memory service
    // @Bean
    // public UserDetailsService UserDetailsService(){

    //     UserDetails user1 = User.withDefaultPasswordEncoder().username("admin123").password("admin123").roles("ADMIN","USER").build();
    //     UserDetails user2 = User.withDefaultPasswordEncoder().username("user123").password("password").build();
    //     var inMemoryUserDetailsManager= new InMemoryUserDetailsManager(user1, user2);
    //     return inMemoryUserDetailsManager;

    // }
    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    // Configuration of authentication provider for spring security
    @Bean
    DaoAuthenticationProvider authenticationProvider(){
     DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
     //user details service object
    daoAuthenticationProvider.setUserDetailsService(userDetailService);
    //password encoder object
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
     return daoAuthenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity HttpSecurity) throws Exception{
        // configuration
        HttpSecurity.requiresChannel(channel ->
        channel.anyRequest().requiresSecure()
    );

        //urls configuration
        HttpSecurity.authorizeHttpRequests(authorize->{
            // authorize.requestMatchers("/home","/register","/services").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });
        // form default login
        HttpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/profile");
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            // formLogin.failureHandler(new AuthenticationFailureHandler() {

            //     @Override
            //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            //             AuthenticationException exception) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
            //     }
                
            // });

            // formLogin.successHandler(new AuthenticationSuccessHandler() {

            //     @Override
            //     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            //             Authentication authentication) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
            //     }
                
            // });
        });
        HttpSecurity.csrf(AbstractHttpConfigurer::disable);
        HttpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });


        //oauth configurations

        HttpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });
       
        return HttpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
