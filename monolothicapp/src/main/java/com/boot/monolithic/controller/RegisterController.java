package com.boot.monolithic.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.monolithic.model.User;

@Controller
public class RegisterController {

	private Map<String, User> users = new HashMap<>();

	public RegisterController() {
		System.out.println("RegisterController");
		users.put("prasad", new User("prasad", "prasad", "prasad@gmail.com"));

	}

	@RequestMapping(value = "user/register", method = RequestMethod.POST)
	@ResponseBody
	public String getRegisterUser(@ModelAttribute("userid") String userId, @ModelAttribute("password") String password,
			@ModelAttribute("email") String email) {
		users.put(userId, new User(userId, password, email));

		return "<HTML> <body bgclor= 'coral'>You have Successfully Registered.trtr.!!"
				+ " <a href='../index.html'> Click Here to Login</a>" + "</body></HTML>";
	}

	@RequestMapping(value = "users/all", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, User> getAllRegisteredUsers() {
		return users;
	}

	@RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public User getUsers(@PathVariable("userId") String userId) {
		return users.get(userId);
	}

	@RequestMapping(value = "user/login", method = RequestMethod.POST)
	public String getLoginUser(@ModelAttribute("userid") String userId, @ModelAttribute("password") String password,
			HttpServletRequest request) {
		User user = users.get(userId);
		request.getSession().setAttribute("user", user);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				return "Trade";
			}else {
				return "PasswordError";
			}
		}
		return "Sorry";
	}
}
