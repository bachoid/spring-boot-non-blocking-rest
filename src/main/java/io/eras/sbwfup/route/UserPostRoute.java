package io.eras.sbwfup.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebFilter;

import io.eras.sbwfup.exception.MyException;
import io.eras.sbwfup.handler.UserPostHandler;
import reactor.core.publisher.Flux;

@Configuration
public class UserPostRoute {

	  @Bean
	  public RouterFunction<ServerResponse> route(UserPostHandler userPostHandler) {

	    return RouterFunctions
	      .route(GET("/userpost/{id}").and(accept(MediaType.APPLICATION_JSON)), userPostHandler::userPosts);
	  }
	  
	  @Bean
	  WebFilter handleException() {
	      return (exchange, next) -> next.filter(exchange)
	              .onErrorResume(MyException.class, e -> {
	                  ServerHttpResponse response = exchange.getResponse();
	                  response.setStatusCode(HttpStatus.NOT_FOUND);
	                  byte[] bytes = e.getMessage().getBytes(StandardCharsets.UTF_8);
	                  DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
	                  return exchange.getResponse().writeWith(Flux.just(buffer));
	              });
	  }
}
