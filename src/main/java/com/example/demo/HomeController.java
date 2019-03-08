package com.example.demo;


import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    CarRepository carRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listCars(Model model){
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("categories", typeRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String newCar(Model model){
        model.addAttribute("car", new Car());
        model.addAttribute("categories", typeRepository.findAll());
        return "form";
    }

    @GetMapping("/addCategory")
    public String newType(Model model){
        model.addAttribute("type",new Type());
        return "category";
    }

    @PostMapping("/add")
    public String processCar(@Valid @ModelAttribute("car") Car car,
                             @RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            car.setImage(uploadResult.get("url").toString());

            carRepository.save(car);
        }catch (IOException e) {
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }

    @PostMapping("/addCategory")
    public String processCategory(@Valid @ModelAttribute("type") Type type){
        typeRepository.save(type);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showCar(@PathVariable("id") long id, Model model){
        model.addAttribute("car", carRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateCar(@PathVariable("id") long id, Model model){
        model.addAttribute("car", carRepository.findById(id));
        return "form";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") long id){
        carRepository.deleteById(id);
        return "redirect:/";
    }












}
