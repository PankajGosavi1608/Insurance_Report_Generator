package com.pankaj.runner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.pankaj.entity.CitizenPlan;
import com.pankaj.repository.CitizenPlanRepository;



@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private CitizenPlanRepository citizenPlanRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		citizenPlanRepository.deleteAll();

		CitizenPlan plan1 = new CitizenPlan("Pankaj", "pankaj@dev.in", 706612, "Male", 86800, "Cash", "Approved",
				LocalDate.now(), LocalDate.now().plusMonths(6));

		CitizenPlan plan2 = new CitizenPlan("Pankaj2", "pankaj2@dev.in", 806612, "Male", 88200, "Cash", "Denied", null,
				null);

		CitizenPlan plan3 = new CitizenPlan("Pankaj3", "pankaj18@dev.in", 906612, "Male", 88010, "Food", "Denied", null,
				null);

		CitizenPlan plan4 = new CitizenPlan("Pankaj4", "pankaj6@dev.in", 1006612, "Male", 88090, "Food", "Approved",
				LocalDate.now(), LocalDate.now().plusMonths(6));

		CitizenPlan plan5 = new CitizenPlan("Pankaj5", "pankaj2@dev.in", 7106612, "Male", 8880, "Medical", "Denied",
				null, null);

		CitizenPlan plan6 = new CitizenPlan("Pankaj6", "pankaj6@dev.in", 75106612, "Male", 8880, "Medical", "Approved",
				LocalDate.now(), LocalDate.now().plusMonths(6));
		
		List<CitizenPlan> records = Arrays.asList(plan1, plan2, plan3, plan4, plan5, plan6);

		citizenPlanRepository.saveAll(records);
	}
}
