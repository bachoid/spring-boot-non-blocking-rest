package io.eras.sbwfup.model;

import java.util.List;

public class UserPost {

	private User user;
	private List<Post> posts;

	public UserPost() {
		super();
	}

	public UserPost(User user, List<Post> posts) {
		super();
		this.user = user;
		this.posts = posts;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

}
