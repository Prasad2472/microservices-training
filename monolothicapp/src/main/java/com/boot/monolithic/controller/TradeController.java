package com.boot.monolithic.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.monolithic.model.Trade;
import com.boot.monolithic.model.User;

@Controller
public class TradeController {

	private User user = new User();
	Map<String, Double> companies = new HashMap<>();
	Map<String, Trade> trades = new HashMap<>();

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

		user = (User) request.getSession().getAttribute("user");
		Double price = companies.get(ticker);

		Trade trade = new Trade(ticker, price, quantity);
		double total = price * quantity;
		trade.setPrice(total);

		double availablebalance = user.getBalance() - total;
		user.setBalance(availablebalance);
		return "<HTML> <body bgcolor='coral'> Traded Successfully.." + user.getUserid() + " Your valaible balcance is:"
				+ user.getBalance() + "<BR> <a href=.. /index.html> Exit</a> <br>"
				+ "<a href='../Trade.html'>Trade again</a> </body></html>";
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
