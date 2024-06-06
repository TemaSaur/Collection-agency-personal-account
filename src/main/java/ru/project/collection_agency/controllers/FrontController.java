package ru.project.collection_agency.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import ru.project.collection_agency.entities.Debt;
import ru.project.collection_agency.entities.User;
import ru.project.collection_agency.services.ContractService;
import ru.project.collection_agency.services.DebtService;
import ru.project.collection_agency.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class FrontController {
	private final UserService userService;
	private final DebtService debtService;
	private final ContractService contractService;

	@Autowired
	public FrontController(UserService userService, DebtService debtService, ContractService contractService) {
		this.userService = userService;
		this.debtService = debtService;
		this.contractService = contractService;
	}

	@GetMapping(value="/")
	public String index(Model model, Principal currentUser) {
		if (currentUser == null)
			return "index";

		User user = userService.getUserByUsername(currentUser.getName());

		List<Long> debtsId = new ArrayList<>();
		List<Debt> debts = new ArrayList<>();
		List<Long> contractsId = new ArrayList<>();

		double debtsSum = 0.0;
		double debtsSumLeft = 0.0;
		for (int i = 0; i < user.getDebts().size(); i++)
		{
			Debt debt = user.getDebts().get(i);
			if (!user.getDebts().get(i).isRepaid())
				debtsSumLeft += debt.getAmount();
			debtsSum += debt.getAmount();
			debtsId.add(debt.getId());
			debts.add(debt);
			contractsId.add(user.getContracts().get(i).getId());
		}

		if (user.getFirstName() != null) {
			model.addAttribute("totalSum", debtsSum);
			model.addAttribute("name", String.format("%s %s.%s.", user.getLastName(), user.getFirstName().charAt(0), user.getPatronymic().charAt(0)));
			model.addAttribute("debts", debts);
			model.addAttribute("left", debtsSumLeft);
			model.addAttribute("paid", debtsSum - debtsSumLeft);
			model.addAttribute("sorry", 0.0);
		}
		return "index";
	}

	@GetMapping(value="/error")
	public String error(Model model, HttpRequest request) {

		return "error";
	}
}
