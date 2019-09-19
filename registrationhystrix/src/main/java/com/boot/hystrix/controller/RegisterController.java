package com.boot.hystrix.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.boot.hystrix.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Controller
public class RegisterController {

	private final Map<String, User> users = new HashMap<>();
	@Value("${pivotal.tradeservice.name}")
	private String tradeService;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Autowired
	private RestTemplate restTemplate;

	public RegisterController() {
		System.out.println("RegisterController");
		users.put("prasad", new User("prasad", "prasad", "prasad@gmail.com"));

	}

	@RequestMapping(value = "user/register", method = RequestMethod.POST)
	@ResponseBody
	public String getRegisterUser(@ModelAttribute("userid") final String userId,
			@ModelAttribute("password") final String password, @ModelAttribute("email") final String email) {
		users.put(userId, new User(userId, password, email));

		return "<HTML> <body bgclor= 'coral'>You have Successfully Registered..!!"
				+ " <a href='../index.html'> Click Here to Login</a>" + "</body></HTML>";
	}

	@RequestMapping(value = "users/all", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "getDefaultUsers")
	@ResponseBody
	public Map<String, User> getAllRegisteredUsers() {
		return users;
	}
	
	public Map<String, User> getDefaultUsers() {
		Map<String, User> defaultUsers= new HashMap<String, User>();
		
		defaultUsers.put("Hystrix", new User("Hystrix", "Hystrix", "Hystrix@gmail.com"));
		return defaultUsers;
	}

	@RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public User getUsers(@PathVariable("userId") final String userId) {
		return users.get(userId);
	}

	@RequestMapping(value = "user/login", method = RequestMethod.POST)
	public String getLoginUser(@ModelAttribute("userid") final String userId,
			@ModelAttribute("password") final String password, final HttpServletRequest request,
			final HttpServletResponse response) {
		final User user = users.get(userId);
		request.getSession().setAttribute("user", user);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				// Client Side load balancing using Spring Cloud Netflix Ribbon
				final ServiceInstance instance = loadBalancerClient.choose(tradeService);
				final URI uri = URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));

				System.out.println(" Trade Service URI=" + uri);

				final String url = uri.toString() + "/Trade.html";
				System.out.println(" Trade Service url=" + url);
				try {
					response.sendRedirect(url);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			} else {
				return "PasswordError";
			}
		}
		return "Sorry";
	}

	@RequestMapping(value = "contries/all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@HystrixCommand(fallbackMethod = "getDefaultContries", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "1"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000") })
	public String getAllContries() {
		final Map<String, Object> contriesMap = new HashMap<String, Object>();

		final ResponseEntity<String> result = restTemplate.getForEntity("https://restcountries.eu/rest/v2/all",
				String.class, contriesMap);
		if (result.getStatusCode() == HttpStatus.OK) {
			return result.getBody();
		}
		return "";
	}

	public String getDefaultContries() {
		final Map<String, Object> contriesMap = new HashMap<String, Object>();
		final ResponseEntity<String> result = restTemplate.getForEntity("https://restcountries.eu/rest/v2/name/India",
				String.class, contriesMap);
		if (result.getStatusCode() == HttpStatus.OK) {
			return result.getBody();
		}

		return "An error occurred .. !!! Please try after sometime";
	}
	
	

}
