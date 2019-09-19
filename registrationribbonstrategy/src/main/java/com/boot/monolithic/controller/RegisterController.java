package com.boot.monolithic.controller;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
	@Value("${pivotal.tradeservice.name}")
	private String tradeService;
	@Autowired
	private LoadBalancerClient loadBalancerClient;

	public RegisterController() {
		System.out.println("RegisterController");
		users.put("prasad", new User("prasad", "prasad", "prasad@gmail.com"));

	}

	@RequestMapping(value = "user/register", method = RequestMethod.POST)
	@ResponseBody
	public String getRegisterUser(@ModelAttribute("userid") String userId, @ModelAttribute("password") String password,
			@ModelAttribute("email") String email) {
		users.put(userId, new User(userId, password, email));

		return "<HTML> <body bgclor= 'coral'>You have Successfully Registered..!!"
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
			HttpServletRequest request, HttpServletResponse response) {
		User user = users.get(userId);
		request.getSession().setAttribute("user", user);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				// Client Side load balancing using Spring Cloud Netflix Ribbon
				ServiceInstance instance = loadBalancerClient.choose(tradeService);
				URI uri = URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));

				System.out.println(" Trade Service URI=" + uri);

				String url = uri.toString() + "/Trade.html";
				System.out.println(" Trade Service url=" + url);
				try {
					response.sendRedirect(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				return "PasswordError";
			}
		}
		return "Sorry";
	}
}
