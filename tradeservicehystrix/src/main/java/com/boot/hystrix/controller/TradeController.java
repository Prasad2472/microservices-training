package com.boot.hystrix.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.boot.hystrix.model.Trade;

@Controller
public class TradeController {

	Map<String, Double> companies = new HashMap<>();
	Map<String, Trade> trades = new HashMap<>();
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;
	@Value("${pivotal.registerservice.name}")
	private String registerService;

	public TradeController() {
		companies.put("WIPRO", 298.45);
		companies.put("INFY", 949.45);
		companies.put("TCS", 2713.70);
		companies.put("TECHM", 485.85);

	}

	@RequestMapping(value = "trade/do", method = RequestMethod.POST)
	@ResponseBody
	public String doTrade(@ModelAttribute("ticker") String ticker, @ModelAttribute("qty") int quantity,
			HttpServletRequest request) {

		Double price = companies.get(ticker);

		Trade trade = new Trade(ticker, price, quantity);
		double total = price * quantity;
		trade.setPrice(total);

		trades.put(ticker, trade);

		List<ServiceInstance> instances = discoveryClient.getInstances(registerService);
		URI uri = instances.get(0).getUri();
		System.out.println(" Register Service URI=" + uri);

		String url = uri.toString() + "/users/all";
		System.out.println(" Register Service url=" + url);
		Map<String, Object>result = new HashMap<String, Object>();
		
		ResponseEntity<String> out= restTemplate.getForEntity(url, String.class, result);

		
		if(out.getStatusCode() == HttpStatus.OK) {
			return out.getBody();
		}
		
		return "";
	}

	@RequestMapping(value = "trade/all", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Trade> getAllRegisteredTrades() {
		return trades;
	}

	@RequestMapping(value = "trade/{ticker}", method = RequestMethod.GET)
	@ResponseBody
	public Trade getTrade(@ModelAttribute("ticker") String tiker) {
		return trades.get(tiker);
	}

}
