package com.auth;

import com.auth.service.security.MongoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Configuration
    @EnableWebSecurity
    protected static class webSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private MongoUserDetailsService userDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .csrf().disable();
            // @formatter:on
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(new BCryptPasswordEncoder());


//			auth
//					.inMemoryAuthentication()
//					.withUser("user").password("123").roles("USER")
//					.and()
//					.withUser("admin").password("123").roles("USER", "ADMIN");
        }

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

        /*
        The default InMemoryTokenStore is perfectly fine for a single server (i.e. low traffic and no hot swap to a backup server in the case of failure).
         Most projects can start here, and maybe operate this way in development mode,
         to make it easy to start a server with no dependencies.
         */
        private TokenStore tokenStore = new InMemoryTokenStore();

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private MongoUserDetailsService userDetailsService;

        @Autowired
        private Environment env;


        /*
        Configuring Client Details

a configurer that defines the client details service. Client details can be initialized,
or you can just refer to an existing store.

The ClientDetailsServiceConfigurer (a callback from your AuthorizationServerConfigurer) can be used to define an in-memory or JDBC implementation of the client details service. Important attributes of a client are

clientId: (required) the client id.AuthorizationServerTokenServices) the client secret, if any.
scope: The scope to which the client is limited. If scope is undefined or empty (the default) the client is not limited by scope.
authorizedGrantTypes: Grant types that are authorized for the client to use. Default value is empty.
authorities: Authorities that are granted to the client (regular Spring Security authorities).
Client details can be updated in a running application by access the underlying store directly (e.g. database tables in the case of JdbcClientDetailsService) or through the ClientDetailsManager interface (which both implementations of ClientDetailsService also implement).

         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

            // TODO persist clients details

            // @formatter:off
            clients.inMemory()
                    .withClient("browser")
                    .authorizedGrantTypes("refresh_token", "password")
                    .scopes("ui")

			/*
                    Grant Type: Client Credentials

					https://oauth.example.com/token?grant_type=client_credentials&client_id=CLIENT_ID&client_secret=CLIENT_SECRET
					 */
                    .and()
                    .withClient("account-service")
                    .secret("ACCOUNT_SERVICE_PASSWORD")
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("server")
                    .and()
                    .withClient("statistics-service")
                    .secret("STATISTICS_SERVICE_PASSWORD")
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("server")
                    .and()
                    .withClient("notification-service")
                    .secret("NOTIFICATION_SERVICE_PASSWORD")
                    .authorizedGrantTypes("client_credentials", "refresh_token")
                    .scopes("server");
            // @formatter:on
        }

        //defines the authorization and token endpoints and the token services.
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .tokenStore(tokenStore)
//					.tokenStore(tokenStore())
                    .authenticationManager(authenticationManager)
                    .userDetailsService(userDetailsService);


        }

//		@Bean
//		public TokenStore tokenStore() {
//			return new JdbcTokenStore(dataSource);
//		}


        //defines the security constraints on the token endpoint.

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()");
        }
    }
}
