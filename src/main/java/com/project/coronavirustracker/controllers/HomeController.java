package com.project.coronavirustracker.controllers;

import java.util.List;

import com.project.coronavirustracker.models.LocationStats;
import com.project.coronavirustracker.services.CoronaVirusDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

   @Autowired
   CoronaVirusDataService coronaVirusDataService;

   @GetMapping("/")
   public String home(Model model) {
      List<LocationStats> allStats = coronaVirusDataService.getAllStats();
      int totalCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPreviousDay()).sum();
      model.addAttribute("allStats", allStats);
      model.addAttribute("totalReportedCases", totalCases);
      return "index";
   }
}
