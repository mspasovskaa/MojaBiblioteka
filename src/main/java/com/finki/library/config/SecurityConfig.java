package com.finki.library.config;


import com.finki.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;
    private UserRepository userRepository;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/library","/library/listbooks","/library/map","/assets/**","/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/library/myBooks","/library/accountInfo","/library/clients","/library/add-book",
                        "/library/add-client","/library/delete-client/{id}",
                        "/library/edit/{id}","/library/borrowed","/library/edit-acc/{id}",
                        "/library/delete/{id}","/library/deleteborrow/{id}","/library/reserve/delete/{id}","/library/books",
                        "/library/user/{id}","/library/reserve/{bookid}","/library/borrow/{bookid}","/library/createborrow").authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/library", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService);
    }
}
