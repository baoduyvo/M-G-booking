package com.vtnq.web.Controllers;

import com.vtnq.web.DTOs.Flight.SearchFlightDTO;
import com.vtnq.web.DTOs.Rating.RatingDTO;
import com.vtnq.web.DTOs.RatingDto;
import com.vtnq.web.Entities.Account;
import com.vtnq.web.Entities.Airport;
import com.vtnq.web.Entities.Hotel;
import com.vtnq.web.Entities.Rating;
import com.vtnq.web.Repositories.AirportRepository;
import com.vtnq.web.Repositories.HotelRepository;
import com.vtnq.web.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping({"","/"})
public class DetailHotelController {
    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private AmenitiesService amenitiesService;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private RoomService roomService;
    public void DetailSearch(HttpServletRequest request,ModelMap modelMap,int id,@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int size) {
        Account currentAccount=(Account) request.getSession().getAttribute("currentAccount");
        SearchFlightDTO searchFlightDTO=(SearchFlightDTO) request.getSession().getAttribute("searchFlightDTO");

        modelMap.put("Hotel",hotelService.FindDetailHotel(id));
        modelMap.put("Image",hotelService.FindImageInDetailHotel(id));
        modelMap.put("Amenities",amenitiesService.FindAmenitiesByHotel(id));
        modelMap.put("DetailRoom",roomService.findRoomDetailHotelWeb(id,searchFlightDTO.getQuantityRoom()));
        modelMap.put("Hotels",hotelService.ShowHotelsAll(id));
        modelMap.put("avgRating",ratingService.getAverageRating(id));
        Rating rating=new Rating();
        Hotel hotel=hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        rating.setHotel(hotel);
        if(currentAccount!=null){
            rating.setUserId(currentAccount.getId());
        }
        modelMap.put("search",searchFlightDTO);
        modelMap.put("Service",serviceService.findAll(id));

        List<RatingDTO>ratingDtoList=ratingService.FindRatingByHotelId(id);
        int start = (page - 1) * size;
        int end = Math.min(start + size, ratingDtoList.size());
        List<RatingDTO>paginatedRating=ratingDtoList.subList(start, end);
        modelMap.put("RatingList",paginatedRating);
        modelMap.put("totalPages", (int) Math.ceil((double) ratingDtoList.size() / size));
        modelMap.put("currentPage", page);
        modelMap.put("Rating",rating);

    }
    @GetMapping({"DetailHotel/{id}"})
    public String DetailController(ModelMap modelMap, @PathVariable int id, HttpServletRequest request, @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size, HttpSession session) {
        try {

            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");
            SearchFlightDTO searchFlightDTO=(SearchFlightDTO) request.getSession().getAttribute("searchFlightDTO");
            Integer idRoom=(Integer)request.getSession().getAttribute("idRoom");
            if(idRoom!=null){
                session.removeAttribute("idRoom");
            }
            if(searchFlightDTO==null){
                return "redirect:/Login";
            }
            if(currentAccount == null || !"ROLE_USER".equals(currentAccount.getAccountType())) {
                DetailSearch(request, modelMap, id,page,size);
                return "User/DetailHotel/DetailHotel";
            }else{
                DetailSearch(request, modelMap, id,page,size);
                return "User/DetailHotel/DetailHotelLogin";
            }

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
