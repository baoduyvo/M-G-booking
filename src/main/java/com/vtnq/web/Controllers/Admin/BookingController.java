package com.vtnq.web.Controllers.Admin;

import com.vtnq.web.Entities.Account;
import com.vtnq.web.Entities.Booking;
import com.vtnq.web.Service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller("BookingAdminController")
@RequestMapping({"/Admin"})
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @GetMapping("Booking")
    public String Booking(ModelMap modelMap,@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String code,
                          HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";

            }
            List<Booking>bookings=bookingService.FindBookings(account.getCountryId());
            List<Booking>FilterBooking=bookings.stream().filter(booking->booking.getBookingCode().contains(code)).collect(Collectors.toList());
            int start = (page - 1) * size;
            int end = Math.min(start + size, FilterBooking.size());
            List<Booking>paginatedBookings=FilterBooking.subList(start, end);
            modelMap.put("bookings", paginatedBookings);
            modelMap.put("totalPages", (int) Math.ceil((double) FilterBooking.size() / size));
           modelMap.put("currentPage", page);
           return "Admin/Booking/Booking";
        }catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
    @GetMapping("Booking/detail/{id}")
    public String detailBooking(ModelMap modelMap,@PathVariable int id,HttpServletRequest request) {
        try {
            Account account = (Account) request.getSession().getAttribute("currentAccount");
            if(account==null || !"ROLE_ADMIN".equals(account.getAccountType())){
                return "redirect:/LoginAdmin";
            }
            modelMap.put("flight",bookingService.findBookingFlights(id));
            modelMap.put("totalPriceFlight",bookingService.getTotalPrice(id));
            modelMap.put("Hotel",bookingService.getBookingRooms(id));
            modelMap.put("totalPriceHotel",bookingService.GetTotalPriceHotel(id));
            return "Admin/Booking/DetailBooking";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
