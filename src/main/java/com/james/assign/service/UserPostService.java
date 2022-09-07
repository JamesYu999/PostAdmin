package com.james.assign.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.james.assign.dto.Post;
import com.james.assign.dto.User;
import com.james.assign.dto.UserPost;
import com.james.assign.util.Constants;

@Service
public class UserPostService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplate restTemplate;

	public UserPostService() {
		super();
	}

	public List<UserPost> getUserPostList(@Nullable Long id) {

		List<UserPost> userPosts = new ArrayList<>();
		List<Post> posts = new ArrayList<>();

		List<User> users = getUserList(id);

		if (!CollectionUtils.isEmpty(users)) {
			//Query for 1 user only
			if (id != null) {
				logger.info("Performing query for user id: " + id);
				posts = getPostList(id);
			} else {
				logger.info("Performing query for all users.");
				posts = getPostList(null);
			}
		}

		if (users.size() == 1) {
			userPosts.add(new UserPost(users.get(0), posts));
		} else {
			Map<Long, List<Post>> mapByUserId = posts.stream().collect(Collectors.groupingBy(Post::getUserId));
			users.stream().forEach(user -> {
				userPosts.add(new UserPost(user, mapByUserId.get(user.getId())));
			});
		}

		logger.info("Total " + (CollectionUtils.isEmpty(userPosts) ? "0" : userPosts.size()) + " records found.");
		return userPosts;

	}

	private List<User> getUserList(@Nullable Long id) {
		List<User> users = new ArrayList<>();
		
		URI uri = getUserURI(id);
		ResponseEntity<User[]> res = restTemplate.getForEntity(uri.toString(), User[].class);
		if (res != null && res.getStatusCode().value() == 200) {
			users = Arrays.asList(res.getBody());
		}

		return users;

	}

	private List<Post> getPostList(@Nullable Long id) {
		List<Post> posts = new ArrayList<>();

		URI uri = getPostURI(id);
		ResponseEntity<Post[]> res = restTemplate.getForEntity(uri.toString(), Post[].class);
		if (res != null && res.getStatusCode().value() == 200) {
			posts = Arrays.asList(res.getBody());
		}

		return posts;

	}

	private URI getUserURI(@Nullable Long id) {

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(Constants.USER_API);
		if (id != null) {
			uriBuilder.queryParam("id", id);
		}

		return uriBuilder.build().toUri();
	}

	private URI getPostURI(@Nullable Long id) {

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(Constants.POST_API);
		if (id != null) {
			uriBuilder.queryParam("userId", id);
		}

		return uriBuilder.build().toUri();
	}

}
