package com.james.assign.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.james.assign.dto.UserPost;
import com.james.assign.service.UserPostService;
import com.james.assign.util.Constants;

@RestController
@RequestMapping(Constants.API_WITH_VERSION)
public class UserPostController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserPostService userPostService;

	@GetMapping("/admin/getUserPosts")
	public ResponseEntity<List<UserPost>> getUserPosts(@RequestParam(value = "id") @Nullable Long id)
			{
		List<UserPost> userPosts = new ArrayList<>();
		userPosts = userPostService.getUserPostList(id);
		return ResponseEntity.status(HttpStatus.OK).body(userPosts);
	}
}
