
package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
	    SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(
			PlayerRepository playerRepository,
			GameRepository gameRepository,
			GamePlayerRepository gamePlayerRepository,
            ShipRepository shipRepository,
			SalvoRepository salvoRepository,
			ScoreRepository scoreRepository) {

		return (args) -> {
			Player player1 = new Player("j.bauer@gob.ar", passwordEncoder().encode("123"));
			Player player2 = new Player("c.obrian@gob.ar", passwordEncoder().encode("123"));
			Player player3 = new Player("d.brando@gob.ar", passwordEncoder().encode("123"));
            Player player4 = new Player("g.giovanna@gob.ar", passwordEncoder().encode("123"));

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
            playerRepository.save(player4);

			Game game1 = new Game();
			Game game2 = new Game();
            Game game3 = new Game();

			gameRepository.save(game1);
			gameRepository.save(game2);
            gameRepository.save(game3);

			GamePlayer gp1 = new GamePlayer(player1, game1);
			GamePlayer gp2 = new GamePlayer(player2, game1);
			GamePlayer gp3 = new GamePlayer(player3, game2);
			GamePlayer gp4 = new GamePlayer(player4, game3);

			gamePlayerRepository.save(gp1);
			gamePlayerRepository.save(gp2);
			gamePlayerRepository.save(gp3);
            gamePlayerRepository.save(gp4);

			salvoRepository.save(new Salvo(gp1, 1, Arrays.asList("A1", "H8", "G5")));
			salvoRepository.save(new Salvo(gp2, 2, Arrays.asList("B2", "F1", "D5")));

			scoreRepository.save(new Score(player1, game1, 1));
			scoreRepository.save(new Score(player2, game1, 0));
		};
	}
}

@Configuration
class WebSecurityApplication extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName -> {
			Player player = playerRepository.findByUserName(inputName);
			if(player != null) {
				return new User(
						player.getUserName(),
						player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
				.antMatchers(
						"/web/scripts/*",
						"/web/styles/**",
						"/web/dist/*",
						"/web/games.html",
						"/api/login",
						"/api/games",
						"/api/players",
						"/api/leaderboard",
						"/rest/*")
				.permitAll()
				.antMatchers(
						"/web/game.html",
						"/web/input.html",
						"/api/game/*/players",
						"/api/game_view/*",
						"/api/games/players/*/ships",
						"/api/games/players/*/salvoes",
						"/api/logout")
				.hasAuthority("USER")
				.anyRequest().denyAll();

        http.formLogin()
                .usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling()
                .authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}


