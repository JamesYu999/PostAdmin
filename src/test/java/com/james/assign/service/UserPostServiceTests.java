package com.james.assign.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.james.assign.AbstractTest;
import com.james.assign.dto.Address;
import com.james.assign.dto.Company;
import com.james.assign.dto.Geo;
import com.james.assign.dto.Post;
import com.james.assign.dto.User;
import com.james.assign.dto.UserPost;
import com.james.assign.util.Constants;

@ExtendWith(MockitoExtension.class)
public class UserPostServiceTests extends AbstractTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private UserPostService userPostService;

	@BeforeAll
	public static void init() {

	}

	@Test
	public void getUserPostListWithoutUserIdTest() throws IOException, InterruptedException {
		List<UserPost> userPosts = new ArrayList<>();

		String sResponse = mapToJson(getUserPostForAll());

		User[] users = { getUser1(), getUser2() };

		when(restTemplate.getForEntity(Constants.USER_API, User[].class))
				.thenReturn(new ResponseEntity<User[]>(users, HttpStatus.OK));

		Post[] posts = new Post[4];
		posts[0] = getPostForUser1().get(0);
		posts[1] = getPostForUser1().get(1);
		posts[2] = getPostForUser2().get(0);
		posts[3] = getPostForUser2().get(1);
		when(restTemplate.getForEntity(Constants.POST_API, Post[].class))
				.thenReturn(new ResponseEntity<Post[]>(posts, HttpStatus.OK));

		userPosts = userPostService.getUserPostList(null);
		assertEquals(sResponse, mapToJson(userPosts));

	}

	@Test
	public void getUserPostListForUser1Test() throws IOException, InterruptedException {
		List<UserPost> userPosts = new ArrayList<>();

		String sResponse = mapToJson(getUserPostForUser1());

		User[] users = { getUser1() };

		when(restTemplate.getForEntity(Constants.USER_API + "?id=1", User[].class))
				.thenReturn(new ResponseEntity<User[]>(users, HttpStatus.OK));

		Post[] posts = new Post[2];
		posts[0] = getPostForUser1().get(0);
		posts[1] = getPostForUser1().get(1);
		when(restTemplate.getForEntity(Constants.POST_API + "?userId=1", Post[].class))
				.thenReturn(new ResponseEntity<Post[]>(posts, HttpStatus.OK));

		userPosts = userPostService.getUserPostList(1L);
		assertEquals(sResponse, mapToJson(userPosts));

	}

	@Test
	public void getUserPostListWithNoneExistingUserIdTest() throws IOException, InterruptedException {
		List<UserPost> userPosts = new ArrayList<>();

		String sResponse = "[]";

		User[] users = {};

		when(restTemplate.getForEntity(Constants.USER_API + "?id=0", User[].class))
				.thenReturn(new ResponseEntity<User[]>(users, HttpStatus.OK));

		Post[] posts = {};

		lenient().when(restTemplate.getForEntity(Constants.POST_API + "?userId=0", Post[].class))
				.thenReturn(new ResponseEntity<Post[]>(posts, HttpStatus.OK));

		userPosts = userPostService.getUserPostList(0L);
		assertEquals(sResponse, mapToJson(userPosts));

	}

	private User getUser1() {
		return new User(1L, "User1", "username1", "user1@test.com",
				new Address("test street", "3A", "Any city", "1000011", new Geo("-37.3159", "81.1496")), "+10010012",
				"http://test.com", new Company("Test company", "catchPhrase", "bs"));
	}

	private User getUser2() {
		return new User(2L, "User2", "username2", "user2@test.com",
				new Address("dev street", "6/19", "Sydney", "023001", new Geo("-45.5641", "82.651")), "+6143049124",
				"http://test2.com", new Company("dev company", "what's that", "yes"));
	}

	private List<Post> getPostForUser1() {
		return Arrays.asList(new Post(100L, 1L, "This title", "The body"),
				new Post(101L, 1L, "How to", "It's a sunny day."));
	}

	private List<Post> getPostForUser2() {
		return Arrays.asList(new Post(102L, 2L, "LOR", "Gandorf."),
				new Post(103L, 2L, "The Crafts", "Let's distroy it"));
	}

	private List<UserPost> getUserPostForAll() {
		List<UserPost> userPosts = new ArrayList<>();

		userPosts.addAll(getUserPostForUser1());

		UserPost userPost2 = new UserPost();
		userPost2.setUser(getUser2());
		userPost2.setPosts(getPostForUser2());
		userPosts.add(userPost2);

		return userPosts;
	}

	private List<UserPost> getUserPostForUser1() {
		List<UserPost> userPosts = new ArrayList<>();

		UserPost userPost1 = new UserPost();
		userPost1.setUser(getUser1());
		userPost1.setPosts(getPostForUser1());
		userPosts.add(userPost1);

		return userPosts;
	}

}
