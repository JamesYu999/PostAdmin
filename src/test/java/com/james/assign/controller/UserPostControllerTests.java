package com.james.assign.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.james.assign.AbstractTest;
import com.james.assign.dto.Address;
import com.james.assign.dto.Company;
import com.james.assign.dto.Geo;
import com.james.assign.dto.Post;
import com.james.assign.dto.User;
import com.james.assign.dto.UserPost;
import com.james.assign.service.UserPostService;
import com.james.assign.util.Constants;

@ExtendWith(MockitoExtension.class)
public class UserPostControllerTests extends AbstractTest {

	@Mock
	private UserPostService mockUserPostService;

	@InjectMocks
	private UserPostController userPostController;

	@BeforeEach
	protected void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userPostController).build();
	}

	@Test
	public void getUserPostsTestWithoutUserIdTest() throws Exception {
		when(mockUserPostService.getUserPostList(null)).thenReturn(getUserPostForAll());
		String uri = Constants.API_WITH_VERSION + "/admin/getUserPosts";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		assertEquals(mapToJson(getUserPostForAll()), mvcResult.getResponse().getContentAsString());

	}

	@Test
	public void getUserPostsTestWithUserIdTest() throws Exception {
		when(mockUserPostService.getUserPostList(1L)).thenReturn(getUserPostForUser1());
		String uri = Constants.API_WITH_VERSION + "/admin/getUserPosts?id=1";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		assertEquals(mapToJson(getUserPostForUser1()), mvcResult.getResponse().getContentAsString());

	}

	@Test
	public void getUserPostsTestWithNonExistingUserIdTest() throws Exception {
		List<UserPost> emptyList = new ArrayList<UserPost>();
		when(mockUserPostService.getUserPostList(0L)).thenReturn(emptyList);
		String uri = Constants.API_WITH_VERSION + "/admin/getUserPosts?id=0";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		assertEquals(mapToJson(emptyList), mvcResult.getResponse().getContentAsString());

	}

	private List<UserPost> getUserPostForAll() {
		List<UserPost> userPosts = new ArrayList<>();

		userPosts.addAll(getUserPostForUser1());

		User user2 = new User(2L, "User2", "username2", "user2@test.com",
				new Address("dev street", "6/19", "Sydney", "023001", new Geo("-45.5641", "82.651")), "+6143049124",
				"http://test2.com", new Company("dev company", "what's that", "yes"));
		UserPost userPost2 = new UserPost();
		userPost2.setUser(user2);
		userPost2.setPosts(Arrays.asList(new Post(102L, 2L, "LOR", "Gandorf."),
				new Post(103L, 2L, "The Crafts", "Let's distroy it")));
		userPosts.add(userPost2);

		return userPosts;
	}

	private List<UserPost> getUserPostForUser1() {
		List<UserPost> userPosts = new ArrayList<>();
		User user1 = new User(1L, "User1", "username1", "user1@test.com",
				new Address("test street", "3A", "Any city", "1000011", new Geo("-37.3159", "81.1496")), "+10010012",
				"http://test.com", new Company("Test company", "catchPhrase", "bs"));
		UserPost userPost1 = new UserPost();
		userPost1.setUser(user1);
		userPost1.setPosts(Arrays.asList(new Post(100L, 1L, "This title", "The body"),
				new Post(101L, 1L, "How to", "It's a sunny day.")));
		userPosts.add(userPost1);

		return userPosts;
	}
}
