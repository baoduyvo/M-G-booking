package com.vtnq.web.Controllers.Admin;

import com.vtnq.web.DTOs.Flight.FlightDto;
import com.vtnq.web.DTOs.Flight.FlightListDTO;
import com.vtnq.web.DTOs.Seat.SeatDTO;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller("FlightAdminController")
@RequestMapping({"/Admin"})
public class FlightController {
    @Autowired
    private AirportService airportService;
    @Autowired
    private AirlineService airlineService;
    @Autowired
    private FlightService flightService;
    @Autowired
    private SeatService seatService;


    @GetMapping("Flight/add")
    public String addFlight(ModelMap model, HttpServletRequest request) {
        try {

            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");

            if(currentAccount==null || !"ROLE_ADMIN".equals(currentAccount.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("DepartureAirPort", airportService.findAll(currentAccount.getCountryId()));
            model.put("ArrivalAirPort", airportService.findAll());
            model.put("Airline", airlineService.FindByCountryId(currentAccount.getCountryId()));
            model.put("flight", new FlightDto());

            return "Admin/Flight/add";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("Flight/edit/{id}")
    public String editFlight(ModelMap model, @PathVariable int id, HttpServletRequest request) {
        try {
            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");

            if(currentAccount==null || !"ROLE_ADMIN".equals(currentAccount.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            model.put("flight", flightService.findById(id));

            model.put("DepartureAirPort", airportService.findAll(currentAccount.getCountryId()));
            model.put("ArrivalAirPort", airportService.findAll());
            model.put("Airline", airlineService.findAll());
            return "Admin/Flight/Edit";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("Flight")
    public String Flight(ModelMap model, HttpServletRequest request, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String name) {
        try {
            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");
            if(currentAccount==null || !"ROLE_ADMIN".equals(currentAccount.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            List<FlightListDTO> flights = flightService.findAllByCountry(currentAccount.getCountryId());
            List<FlightListDTO> filterFlights = flights.stream().filter(flight ->
                    flight.getNameAirline().contains(name)).collect(Collectors.toList());
            int start = (page - 1) * size;
            int end = Math.min(start + size, filterFlights.size());
            List<FlightListDTO> paginatedFlights = flights.subList(start, end);
            model.put("Flight", paginatedFlights);
            model.put("totalPages", (int) Math.ceil((double) filterFlights.size() / size));
            model.put("currentPage", page);
            model.put("Seat",new SeatDTO());

            return "Admin/Flight/Flight";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("Flight/UpdateFlight")
    public String UpdateFlight(@ModelAttribute("flight") @Valid FlightDto flightDto,BindingResult bindingResult,HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if(flightDto.getDepartureInstant().isBefore(flightDto.getArrivalInstant().plusHours(2))){
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addFlashAttribute("message", "Departure time must be at least 2 hours after arrival time.");
                return "redirect:/Admin/Flight/edit/"+flightDto.getId();
            }
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors: ");
                bindingResult.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format("Field : %s. ", error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/Flight/edit/"+flightDto.getId();
            }
            if(flightDto.getDeparture_airport()==flightDto.getArrival_airport()){
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addFlashAttribute("message", "The flight and return are not the same");
                return "redirect:/Admin/Flight/edit/"+flightDto.getId();
            }
            if(flightService.UpdateFlightDto(flightDto)) {
                redirectAttributes.addFlashAttribute("messageType", "success");
                redirectAttributes.addFlashAttribute("message", "Flight updated successfully");
                return "redirect:/Admin/Flight/edit/"+flightDto.getId();
            }else{
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addFlashAttribute("message", "Flight update failed");
                return "redirect:/Admin/Flight/edit/"+flightDto.getId();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostMapping("Flight/addSeat")
    public String addSeat(@ModelAttribute("Seat") SeatDTO seatDTO,RedirectAttributes redirectAttributes,HttpServletRequest request,BindingResult bindingResult) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors: ");
                bindingResult.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format("Field : %s. ", error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/Flight";
            }
             if(seatDTO.hasErrors()){
                redirectAttributes.addFlashAttribute("message",seatDTO.getValidationErrors());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/Flight";
            }
            if (!seatDTO.isValid()) {
                redirectAttributes.addFlashAttribute("message", String.join("\n", seatDTO.getValidationErrors()));
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/Flight";
            }
            if(seatDTO.getFirstClassSeat()==0 &&seatDTO.getBusinessClassSeat()==0 && seatDTO.getEconomyClassSeat()==0){
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addFlashAttribute("message","You have not entered any seats yet.");
                return "redirect:/Admin/Flight";
            }
            if(flightService.CreateSeat(seatDTO)) {
                redirectAttributes.addFlashAttribute("messageType", "success");
                redirectAttributes.addFlashAttribute("message", "Seat added successfully");
                return "redirect:/Admin/Flight";
            }else{
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addFlashAttribute("message", "Seat add failed");
                return "redirect:/Admin/Flight";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        }

    @PostMapping("Flight/add")
    public String addFlight(@ModelAttribute("flight") @Valid FlightDto flightDto,BindingResult bindingResult, ModelMap model, RedirectAttributes redirectAttributes,
                            HttpServletRequest request
            ) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }

            if (bindingResult.hasErrors()) {
                StringBuilder errorMessages = new StringBuilder("Validation errors: ");
                bindingResult.getFieldErrors().forEach(error ->
                        errorMessages.append(String.format("Field : %s. ", error.getDefaultMessage()))
                );
                redirectAttributes.addFlashAttribute("message", errorMessages.toString());
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/Flight/add";
            }
            if(flightDto.getDeparture_airport()==flightDto.getArrival_airport()){
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addFlashAttribute("message", "The flight and return are not the same");
                return "redirect:/Admin/Flight/add";
            }
            if (flightService.save(flightDto)) {
                redirectAttributes.addFlashAttribute("message", "Flight added successfully");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/Admin/Flight/add";
            } else {
                redirectAttributes.addFlashAttribute("message", "Flight added failed");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return "redirect:/Admin/Flight/add";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
