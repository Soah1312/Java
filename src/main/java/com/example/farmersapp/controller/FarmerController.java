package com.example.farmersapp.controller;

import com.example.farmersapp.model.Produce;
import com.example.farmersapp.service.ProduceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// Controller wiring routes to views and service calls.
@Controller
public class FarmerController {

    private final ProduceService produceService;

    public FarmerController(ProduceService produceService) {
        this.produceService = produceService;
    }

    // GET /farmer -> display the form to add produce
    @GetMapping("/farmer")
    public String getFarmerForm(Model model) {
        model.addAttribute("produce", new Produce());
        return "farmer"; // templates/farmer.html
    }

    // POST /farmer -> submit form to Supabase via service
    @PostMapping("/farmer")
    public String submitProduce(@ModelAttribute("produce") Produce produce, Model model) {
        produceService.addProduce(produce);
        model.addAttribute("success", "Produce Added Successfully");
        return "farmer";
    }

    // GET /home -> list all produce entries
    @GetMapping({"/home", "/"})
    public String getHome(Model model) {
        List<Produce> produceList = produceService.getAllProduce();
        model.addAttribute("produceList", produceList);
        return "home"; // templates/home.html
    }
}

