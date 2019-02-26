package bg.elsys.jobche.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class WebSecurityConfig(val userDetailsService: PostgreUserDetailsService) : WebSecurityConfigurerAdapter() {

    private val WHITELISTED_URLS = arrayOf("/v2/api-docs",
            "/swagger-resources",
            "/csrf",
            "/",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/actuator/**")

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    public override fun configure(builder: AuthenticationManagerBuilder) {
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())

    }

    override fun configure(http: HttpSecurity) {
        http.run {
            authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.POST, "/users/login").permitAll()
                .antMatchers(*WHITELISTED_URLS).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
            csrf().disable()
        }
    }
}