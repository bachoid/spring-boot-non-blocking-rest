package io.eras.sbwfup.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.eras.sbwfup.exception.MyException;
import io.eras.sbwfup.model.Post;
import io.eras.sbwfup.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserPostWebClient {

	private final WebClient client;

	public UserPostWebClient(@Value("${userpostservice.baseurl}") String baseUrl, WebClient.Builder builder) {
		this.client = builder.baseUrl(baseUrl).build();
	}

	public Mono<User> user(Long id) {
		return this.client.get()
				.uri("/users/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(User.class)
				.onErrorMap(e -> new MyException("Error when getting user", e));
	}
	
	public Flux<User> users() {
		return this.client.get()
				.uri("/users")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(User.class)
				.onErrorMap(e -> new MyException("Error when getting users", e));
	}

	public Flux<Post> posts(Long id) {
		return this.client.get()
				.uri(builder -> builder.path("/posts").queryParam("userId", id).build())
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(Post.class)
				.onErrorMap(e -> new MyException("Error when getting posts", e));
	}
}
