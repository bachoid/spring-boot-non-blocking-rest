package io.eras.sbwfup.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.eras.sbwfup.exception.MyException;
import io.eras.sbwfup.model.Post;
import io.eras.sbwfup.model.User;
import io.eras.sbwfup.model.UserPost;
import io.eras.sbwfup.service.UserPostWebClient;
import reactor.core.publisher.Mono;

@Component
public class UserPostHandler {

	@Autowired
	private UserPostWebClient userWebClient;

	public Mono<ServerResponse> userPosts(ServerRequest request) {
		Long id = Long.valueOf(request.pathVariable("id"));
		Mono<User> userM = this.userWebClient.user(id)
				.onErrorMap(e -> new MyException("Error when getting user", e));
		Mono<List<Post>> postsM = this.userWebClient.posts(id)
				.onErrorMap(e -> new MyException("Error when getting posts", e))
				.collectList();

		return userM.zipWith(postsM)
				.map(p -> {
					User user = p.getT1();
					List<Post> posts = p.getT2();
					UserPost userPost = new UserPost();
					userPost.setUser(user);
					userPost.setPosts(posts);
					return userPost;
				})
				.flatMap(up -> ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(up))
				.switchIfEmpty(ServerResponse.notFound().build()));
		
	}
	
	public Mono<ServerResponse> allUsersWithPosts(ServerRequest request) {
		return this.userWebClient.users()
				.onErrorMap(e -> new MyException("Error when getting users", e))
				.flatMap(u -> {
					return this.userWebClient.posts(u.getId())
							.onErrorMap(e -> new MyException("Error when getting posts by user", e))
							.collectList()
							.map(ps -> {
								return new UserPost(u, ps);
							});
							
				})
				.collectList()
				.flatMap(up -> ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(up))
						.switchIfEmpty(ServerResponse.notFound().build()));

	}
}
