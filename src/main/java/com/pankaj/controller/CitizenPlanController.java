package com.pankaj.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.pankaj.binding.SearchCriteria;
import com.pankaj.entity.CitizenPlan;
import com.pankaj.service.CitizenPlanService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CitizenPlanController {
	
	@Autowired
	private CitizenPlanService citizenPlanService;
	
	@GetMapping("/")
	public String index(Model model) {
		
		formInIt(model);
		
		model.addAttribute("search", new SearchCriteria());
		
		return "index";
	}

	private void formInIt(Model model) {
		List<String> planNames = citizenPlanService.getPlanNames();
		List<String> planStatus = citizenPlanService.getPlanStatus();
		model.addAttribute("planNames", planNames);
		model.addAttribute("planStatus", planStatus);
	}
	
	@PostMapping("/filter-data")
	public String handleSearchButton(@ModelAttribute ("search") SearchCriteria criteria, Model model) {
		
		System.out.println(criteria);
		
		List<CitizenPlan> citizensInfo = citizenPlanService.searchCitizens(criteria);
		model.addAttribute("citizens", citizensInfo);
			
		formInIt(model);	
	
		return "index";
	}
	
	@GetMapping("/excel")
	public void downloadExcel(HttpServletResponse response) throws Exception{
		
		response.setContentType("application/octet-stream");
		
		String headerKey="Content-Disposition";
		String headerValue="attachment;filename=recordData.xls";
		response.addHeader(headerKey, headerValue);
		
		citizenPlanService.generateExcel(response);
	
		
	}
	@GetMapping("/pdf")
	public void downloadPdf(HttpServletResponse response) throws Exception{
		
		response.setContentType("application/pdf");
		
		String headerKey="Content-Disposition";
		String headerValue="attachment;filename=recordData.pdf";
		response.addHeader(headerKey, headerValue);
		citizenPlanService.generatePdf(response);
		
	}

}

