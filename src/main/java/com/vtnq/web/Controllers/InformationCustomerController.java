package com.vtnq.web.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.vtnq.web.DTOs.Booking.*;
import com.vtnq.web.DTOs.Booking.BookingFlightDetail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.vtnq.web.DTOs.Flight.SearchFlightDTO;
import com.vtnq.web.Entities.*;
import com.vtnq.web.Repositories.BookingRepository;
import com.vtnq.web.Repositories.FlightRepository;
import com.vtnq.web.Repositories.SeatRepository;
import com.vtnq.web.Service.*;
import com.vtnq.web.WebSocket.RoomUpdateWebSocketHandler;
import com.vtnq.web.WebSocket.SeatUpdateWebSocketHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"","/"})
public class InformationCustomerController {
    @Autowired
    private PayPalService payPalService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RoomUpdateWebSocketHandler roomUpdateWebSocketHandler;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private SeatService seatService;
    @Autowired
    private SeatUpdateWebSocketHandler seatUpdateWebSocketHandler;

    @Autowired
    private FlightService flightService;
    private static final String SUCCESS_URL = "payFlight/success";
    private static final String CANCEL_URL = "payFlight/cancel";
    private static final String SuccessHotel="payHotelFlight/success";
   @GetMapping("payHotelFlight")
   public RedirectView paymentHotelFlight(@RequestParam(required = true) double amount,
                                          @RequestParam(defaultValue = "JPY") String currency, @ModelAttribute BookingHotelDTO bookingHotelDTO, @ModelAttribute BookingFlightDTO bookingFlightDTO, HttpServletRequest request,
                                          HttpSession session, @RequestParam("bookings") String bookingsJson, RedirectAttributes redirectAttributes) {
       try {
           Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");
           bookingHotelDTO.setUserId(currentAccount.getId());
           SearchFlightDTO resultFlightDTO = (SearchFlightDTO) request.getSession().getAttribute("searchFlightDTO");
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
           LocalDate CheckInDate = LocalDate.parse(resultFlightDTO.getCheckInTime(), formatter);
           LocalDate CheckOutDate = LocalDate.parse(resultFlightDTO.getCheckOutTime(), formatter);
           if(bookingsJson.isEmpty()){
               return new RedirectView(request.getHeader("Referer"));
           }

           ObjectMapper objectMapper = new ObjectMapper();
           List<Map<String, Object>> bookings = objectMapper.readValue(bookingsJson, new TypeReference<List<Map<String, Object>>>() {});
           boolean hasInvalidBooking = bookings.stream()
                   .anyMatch(booking -> booking.containsKey("id") && (Integer) booking.get("id") == 0);

           if (hasInvalidBooking) {
               redirectAttributes.addFlashAttribute("message", "Please select the correct number of seats.");
               redirectAttributes.addFlashAttribute("messageType", "error");
               return new RedirectView(request.getHeader("Referer"));
           }
           if(!resultFlightDTO.getArrivalTime().isEmpty()){
               if(bookings.size()!=resultFlightDTO.getNumberPeopleRight()*2){
                   redirectAttributes.addFlashAttribute("message", "Please select the correct number of seats.");
                   redirectAttributes.addFlashAttribute("messageType", "error");
                   return new RedirectView(request.getHeader("Referer"));
               }
           }else{
               if(bookings.size()!=resultFlightDTO.getNumberPeopleRight()){
                   redirectAttributes.addFlashAttribute("message", "Please select the correct number of seats.");
                    redirectAttributes.addFlashAttribute("messageType", "error");
                   return new RedirectView(request.getHeader("Referer"));

               }
           }
           bookingHotelDTO.setCheckInDate(CheckInDate);
           bookingHotelDTO.setCheckOutDate(CheckOutDate);
           bookingFlightDTO.setUserId(currentAccount.getId());
           session.setAttribute("amount", BigDecimal.valueOf(amount));
           session.setAttribute("Hotel",bookingHotelDTO);
           session.setAttribute("booking", bookingFlightDTO);
           session.setAttribute("bookings", bookingsJson);
           Payment payment = payPalService.createPayment(amount, currency, "paypal",
                   "sale", "Test payment", "http://localhost:8686/" + CANCEL_URL,
                   "http://localhost:8686/" + SuccessHotel);
           for (Links link : payment.getLinks()) {
               if (link.getRel().equals("approval_url")) {
                   return new RedirectView(link.getHref());
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       if (session.getAttribute("idFlight") != null) {
           session.removeAttribute("idFlight");  // Remove the idFlight from session
       }
       if (session.getAttribute("idRoom") != null) {
           session.removeAttribute("idRoom");  // Remove the idFlight from session
       }
       if(session.getAttribute("NumberPeople")!=null){
           session.removeAttribute("NumberPeople");
       }
       if(session.getAttribute("searchFlightDTO")!=null){
           session.removeAttribute("searchFlightDTO");
       }
       return new RedirectView("/");
   }
   @GetMapping("InformationFlightHotel/{id}")
   public String InformationFlightHotel(ModelMap modelMap,@PathVariable int id,HttpServletRequest request,HttpSession session) {
       try {
           BookingFlightDTO bookingDto=new BookingFlightDTO();
           Account account=(Account)request.getSession().getAttribute("currentAccount");
           if(account==null){
               return "redirect:/Login";
           }
           session.setAttribute("idFlightPrevent",id);
           SearchFlightDTO resultFlightDTO=(SearchFlightDTO) request.getSession().getAttribute("searchFlightDTO");
           List<Integer>idFlight=(List<Integer>) request.getSession().getAttribute("idFlight");
           Integer idRoom=(Integer) request.getSession().getAttribute("idRoom");
           BookingHotel bookingHotel=hotelService.FindBookingHotel(idRoom);
           BigDecimal totalFlight=BigDecimal.ZERO;
           BigDecimal total = bookingHotel.getPrice()
                   .multiply(BigDecimal.valueOf(resultFlightDTO.getQuantityRoom()));
           BigDecimal totalAmount=bookingHotel.getPrice().multiply(BigDecimal.valueOf(resultFlightDTO.getNumberPeopleRight()));
           if(idFlight!=null){
               boolean existFlightBooking=idFlight.stream().anyMatch(flight->flight==id);
               if(!existFlightBooking){
                   idFlight.add(id);
               }
           }

           List<BookingListFly>FlightBooking=new ArrayList<>();
           List<Flight>flights=new ArrayList<>();
           if(idFlight!=null){

               for (Integer i:idFlight) {
                   boolean existsFlightTab=flights.stream().anyMatch(flight->flight.getId()==i);
                   if(!existsFlightTab){
                       Flight flight=flightRepository.findById(i).orElse(null);

                       if(flight!=null){
                           flights.add(flight);
                        totalAmount=totalAmount.add(flight.getPrice());
                           BigDecimal currentTotalPrice = bookingDto.getTotalPrice() != null ? bookingDto.getTotalPrice() : BigDecimal.ZERO;
                           bookingDto.setTotalPrice(currentTotalPrice.add(flight.getPrice()));
                        totalFlight=totalFlight.add(flight.getPrice());

                       }

                   }
                   boolean existBookingListFly=FlightBooking.stream().anyMatch(booking->booking.
                           getId()==i);
                   if(!existBookingListFly){
                       BookingListFly bookingListFly=flightService.getResultPaymentFlight(i);
                       FlightBooking.add(bookingListFly);

                   }
               }
               modelMap.put("idFlight",idFlight);
           }else{
               idFlight=new ArrayList<>();
               idFlight.add(id);
               for (Integer i:idFlight) {
                   boolean existsFlightTab=flights.stream().anyMatch(flight->flight.getId()==i);
                   if(!existsFlightTab){
                       Flight flight=flightRepository.findById(i).orElse(null);
                       if(flight!=null){
                           flights.add(flight);
                           totalAmount=totalAmount.add(flight.getPrice());
                           BigDecimal currentTotalPrice = bookingDto.getTotalPrice() != null ? bookingDto.getTotalPrice() : BigDecimal.ZERO;
                           bookingDto.setTotalPrice(currentTotalPrice.add(flight.getPrice()));
                           totalFlight=totalFlight.add(flight.getPrice());

                       }
                   }

                   boolean existBookingListFly=FlightBooking.stream().anyMatch(booking->booking.
                           getId()==i);
                   if(!existBookingListFly){
                       BookingListFly bookingListFly=flightService.getResultPaymentFlight(i);
                       FlightBooking.add(bookingListFly);

                   }
               }
               modelMap.put("idFlight",idFlight);
           }

           modelMap.put("hotel",hotelService.FindBookingHotel(idRoom));


           modelMap.put("flight",FlightBooking);
           modelMap.put("searchFlightDTO",resultFlightDTO);
           int paymentTimeOut=20*60;
           modelMap.put("flightTab",flights);
           modelMap.put("timeout",paymentTimeOut);
           modelMap.put("flight",FlightBooking);
           if(account.getLevel().getId()==2){
               modelMap.put("Voucher","5%");
               BigDecimal discount = new BigDecimal("0.05");
               BigDecimal discountedTotal = total.subtract(total.multiply(discount));
               total=discountedTotal;
               totalAmount=discountedTotal;
           }else if(account.getLevel().getId()==1){
               BigDecimal discount = new BigDecimal("0.10");
               BigDecimal discountedTotal = total.subtract(total.multiply(discount));
               modelMap.put("Voucher","5%");
               total=discountedTotal;
           }
           modelMap.put("total",total);
           modelMap.put("totalAmount",totalAmount);
           BookingHotelDTO hotelDTO=new BookingHotelDTO();
           hotelDTO.setTypeId(idRoom);
           hotelDTO.setPrice(bookingHotel.getPrice());
           hotelDTO.setTotalPrice(total);
           hotelDTO.setQuantity(resultFlightDTO.getQuantityRoom());
           modelMap.put("hotelDTO",hotelDTO);
           Integer NumberPeople = (Integer) request.getSession().getAttribute("NumberPeople");
           SearchFlightDTO searchFlightDTO=(SearchFlightDTO) request.getSession().getAttribute("searchFlightDTO");
           modelMap.put("number",searchFlightDTO.getNumberPeopleRight());


           modelMap.put("payment",bookingDto);
           modelMap.put("totalFlight",totalFlight);
           modelMap.put("DaysCheckIn",SearchFlightDTO.calculateDaysBetween(resultFlightDTO.getCheckInTime(),resultFlightDTO.getCheckOutTime()));
           return "/User/InformationCustomer/InformationCustomer";
       }catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }
    @GetMapping("payFlight")
    public RedirectView payment(@RequestParam(required = true) double amount,
                                @RequestParam(defaultValue = "JPY") String currency, @ModelAttribute BookingFlightDTO bookingFlightDTO, @RequestParam("bookings") String bookingsJson, HttpSession session, HttpServletRequest request,RedirectAttributes redirectAttributes) throws JsonProcessingException {
        try {

            SearchFlightDTO searchFlightDTO=(SearchFlightDTO) request.getSession().getAttribute("searchFlightDTO");

            Account currentAccount = (Account) request.getSession().getAttribute("currentAccount");
            if(bookingsJson.isEmpty()){
                return new RedirectView(request.getHeader("Referer"));
            }
            bookingFlightDTO.setUserId(currentAccount.getId());
            bookingFlightDTO.setTotalPrice(BigDecimal.valueOf(amount));
            session.setAttribute("booking", bookingFlightDTO);
            session.setAttribute("bookings", bookingsJson);
            Integer NumberPeople = (Integer) request.getSession().getAttribute("NumberPeople");
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> bookings = objectMapper.readValue(bookingsJson, new TypeReference<List<Map<String, Object>>>() {});
            boolean hasInvalidBooking = bookings.stream()
                    .anyMatch(booking -> booking.containsKey("id") && (Integer) booking.get("id") == 0);

            if (hasInvalidBooking) {
                redirectAttributes.addFlashAttribute("message", "Please select the correct number of seats.");
                redirectAttributes.addFlashAttribute("messageType", "error");
                return new RedirectView(request.getHeader("Referer"));

            }
            if(!searchFlightDTO.getArrivalTime().isEmpty()){
                if(bookings.size()!=NumberPeople*2){
                    redirectAttributes.addFlashAttribute("message", "Please select the correct number of seats.");
                    redirectAttributes.addFlashAttribute("messageType", "error");
                    return new RedirectView(request.getHeader("Referer"));
                }
            }else{

                if(bookings.size()!=NumberPeople){
                    redirectAttributes.addFlashAttribute("message", "Please select the correct number of seats.");
                    redirectAttributes.addFlashAttribute("messageType", "error");
                    return new RedirectView(request.getHeader("Referer"));
                }
            }

            Payment payment = payPalService.createPayment(amount, currency, "paypal",
                    "sale", "Test payment", "http://localhost:8686/" + CANCEL_URL,
                    "http://localhost:8686/" + SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return new RedirectView(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        if (session.getAttribute("idFlight") != null) {
            session.removeAttribute("idFlight");  // Remove the idFlight from session
        }
        if (session.getAttribute("idRoom") != null) {
            session.removeAttribute("idRoom");  // Remove the idFlight from session
        }
        if(session.getAttribute("NumberPeople")!=null){
            session.removeAttribute("NumberPeople");
        }
        if(session.getAttribute("searchFlightDTO")!=null){
            session.removeAttribute("searchFlightDTO");
        }
        return new RedirectView("/");
    }
    @GetMapping(SuccessHotel)
    public String SuccessHotel(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request,
                               HttpSession session,RedirectAttributes redirectAttributes) {
       try {
           Payment payment = payPalService.executePayment(paymentId, payerId);
           BookingHotelDTO bookingHotelDTO=(BookingHotelDTO)request.getSession().getAttribute("Hotel");
           BookingFlightDTO bookingFlightDto = (BookingFlightDTO) request.getSession().getAttribute("booking");
           String bookings = (String) request.getSession().getAttribute("bookings");
            BigDecimal amount=(BigDecimal) request.getSession().getAttribute("amount");
            int Bookings=bookingService.addBookingHotel(bookingHotelDTO,bookingHotelDTO.getQuantity(),bookingFlightDto,bookings,amount);
           if(payment.getState().equals("approved") && Bookings!=0){
               session.setAttribute("idBooking",Bookings);
               if (session.getAttribute("idFlight") != null) {
                   session.removeAttribute("idFlight");  // Remove the idFlight from session
               }
               if (session.getAttribute("idRoom") != null) {
                   session.removeAttribute("idRoom");  // Remove the idFlight from session
               }
               if(session.getAttribute("NumberPeople")!=null){
                   session.removeAttribute("NumberPeople");
               }
               if(session.getAttribute("searchFlightDTO")!=null){
                   session.removeAttribute("searchFlightDTO");
               }
                return "redirect:/Success";
            }
           try {
               Seat bookedSeat = bookingService.getBookedSeatFromBookings(bookings);
               seatUpdateWebSocketHandler.notifySeatStatus(bookedSeat, false);
               roomUpdateWebSocketHandler.NotifyRoomStatus(bookingHotelDTO.getTypeId(),false);
           } catch (IOException e) {
               e.printStackTrace();  // Log the error or handle it as needed
           }
       }catch (Exception e){
           e.printStackTrace();

       }
        redirectAttributes.addFlashAttribute("messageType", "error");
       redirectAttributes.addFlashAttribute("message","Payment Failed");
        if (session.getAttribute("idFlight") != null) {
            session.removeAttribute("idFlight");  // Remove the idFlight from session
        }
        if (session.getAttribute("idRoom") != null) {
            session.removeAttribute("idRoom");  // Remove the idFlight from session
        }
        if(session.getAttribute("NumberPeople")!=null){
            session.removeAttribute("NumberPeople");
        }
        if(session.getAttribute("searchFlightDTO")!=null){
            session.removeAttribute("searchFlightDTO");
        }
        return "redirect:/";
    }
    @GetMapping(SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request,HttpSession session,RedirectAttributes redirectAttributes) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            BookingFlightDTO bookingFlightDto = (BookingFlightDTO) request.getSession().getAttribute("booking");
            String bookings = (String) request.getSession().getAttribute("bookings");
            int Booking= bookingService.addBooking(bookingFlightDto, bookings);
            if (payment.getState().equals("approved") && Booking>0) {
                session.setAttribute("idBooking",Booking);
                if (session.getAttribute("idFlight") != null) {
                    session.removeAttribute("idFlight");  // Remove the idFlight from session
                }
                if (session.getAttribute("idRoom") != null) {
                    session.removeAttribute("idRoom");  // Remove the idFlight from session
                }
                if(session.getAttribute("NumberPeople")!=null){
                    session.removeAttribute("NumberPeople");
                }
                if(session.getAttribute("searchFlightDTO")!=null){
                    session.removeAttribute("searchFlightDTO");
                }
                return "redirect:/Success";

            }


            try {
                Seat bookedSeat = bookingService.getBookedSeatFromBookings(bookings);
                seatUpdateWebSocketHandler.notifySeatStatus(bookedSeat, false);
            } catch (IOException e) {
                e.printStackTrace();  // Log the error or handle it as needed
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("messageType", "error");
        redirectAttributes.addFlashAttribute("message","Payment Failed");
        if (session.getAttribute("idFlight") != null) {
            session.removeAttribute("idFlight");  // Remove the idFlight from session
        }
        if (session.getAttribute("idRoom") != null) {
            session.removeAttribute("idRoom");  // Remove the idFlight from session
        }
        if(session.getAttribute("NumberPeople")!=null){
            session.removeAttribute("NumberPeople");
        }
        if(session.getAttribute("searchFlightDTO")!=null){
            session.removeAttribute("searchFlightDTO");
        }
        return "redirect:/";
    }
//
//    @GetMapping("InformationCustomer/{id}")
//    public String InformationCustomer(ModelMap modelMap,@PathVariable int id,HttpServletRequest request) {
//        try {
//            SearchFlightDTO resultFlightDTO=(SearchFlightDTO) request.getSession().getAttribute("searchFlightDTO");
//            List<Integer>idFlight=(List<Integer>) request.getSession().getAttribute("idFlight");
//            List<BookingListFly>FlightBooking=new ArrayList<>();
//            List<Flight>flights=new ArrayList<>();
//            if(idFlight!=null){
//                for (Integer i:idFlight) {
//                    boolean existsFlightTab=flights.stream().anyMatch(flight->flight.getId()==i);
//                    if(!existsFlightTab){
//                        Flight flight=flightRepository.findById(i).orElse(null);
//                        if(flight!=null){
//                            flights.add(flight);
//                        }
//
//                    }
//                    boolean existBookingListFly=FlightBooking.stream().anyMatch(booking->booking.
//                            getId()==i);
//                    if(!existBookingListFly){
//                        BookingListFly bookingListFly=flightService.getResultPaymentFlight(i);
//                        FlightBooking.add(bookingListFly);
//                    }
//                }
//                modelMap.put("idFlight",idFlight);
//            }
//            BookingHotel bookingHotel=hotelService.FindBookingHotel(id);
//            modelMap.put("hotel",hotelService.FindBookingHotel(id));
//            BigDecimal total = bookingHotel.getPrice()
//                    .multiply(BigDecimal.valueOf(resultFlightDTO.getNumberPeopleRight()));
//            modelMap.put("flight",FlightBooking);
//            modelMap.put("searchFlightDTO",resultFlightDTO);
//            int paymentTimeOut=20*60;
//            modelMap.put("flightTab",flights);
//            modelMap.put("timeout",paymentTimeOut);
//            modelMap.put("flight",FlightBooking);
//            modelMap.put("total",total);
//            BookingHotelDTO hotelDTO=new BookingHotelDTO();
//            hotelDTO.setTypeId();
//            hotelDTO.setPrice(bookingHotel.getPrice());
//            hotelDTO.setTotalPrice(total);
//            hotelDTO.setQuantity(resultFlightDTO.getQuantityRoom());
//            modelMap.put("hotelDTO",hotelDTO);
//            Integer NumberPeople = (Integer) request.getSession().getAttribute("NumberPeople");
//            modelMap.put("number",NumberPeople);
//            BookingFlightDTO bookingDto=new BookingFlightDTO();
//            modelMap.put("payment",bookingDto);
//        return "/User/InformationCustomer/InformationCustomer";
//        }catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    @GetMapping("InformationFly/{id}")
    public String InformationFly(@PathVariable int id, ModelMap modelMap,HttpServletRequest request,HttpSession session) {
        try {
            Account account=(Account)request.getSession().getAttribute("currentAccount");
            BigDecimal totalFlight=BigDecimal.ZERO;
            if(account==null){
                return "redirect:/Login";
            }
            session.setAttribute("idFlightPrevent",id);

            List<Integer>idFlight=(List<Integer>) request.getSession().getAttribute("idFlight");

            List<BookingListFly>FlightBooking=new ArrayList<>();
            List<Flight>flights=new ArrayList<>();
            BigDecimal total=BigDecimal.ZERO;
            if(idFlight!=null){
                boolean existFlight=idFlight.stream().anyMatch(flight->flight.equals(id));
                if(!existFlight){
                    idFlight.add(id);
                }
                for (Integer i:idFlight) {
                    boolean existsFlightTab=flights.stream().anyMatch(flight->flight.getId()==i);
                    if(!existsFlightTab){
                        Flight flight=flightRepository.findById(i).get();
                        total = total.add(flight.getPrice());
                        totalFlight=totalFlight.add(flight.getPrice());
                        flights.add(flight);
                    }
                    boolean existBookingListFly=FlightBooking.stream().anyMatch(booking->booking.
                            getId()==i);
                    if(!existBookingListFly){
                        BookingListFly bookingListFly=flightService.getResultPaymentFlight(i);
                        FlightBooking.add(bookingListFly);
                    }

                }




            }else {
                idFlight=new ArrayList<>();
                idFlight.add(id);
                Flight flight=flightRepository.findById(id).orElseThrow(()->new RuntimeException("Flight not found"));
                total = total.add(flight.getPrice());
                totalFlight=totalFlight.add(flight.getPrice());
                flights.add(flight);
                FlightBooking.add(flightService.getResultPaymentFlight(id));
            }
            modelMap.put("idFlight",idFlight);

            if(account.getLevel().getId()==2){
                modelMap.put("Voucher","5%");
                BigDecimal discount = new BigDecimal("0.05");
                BigDecimal discountedTotal = total.subtract(total.multiply(discount));
                total=discountedTotal;

            }else if(account.getLevel().getId()==1){
                BigDecimal discount = new BigDecimal("0.10");
                BigDecimal discountedTotal = total.subtract(total.multiply(discount));
                modelMap.put("Voucher","5%");
                total=discountedTotal;
            }
            SearchFlightDTO searchFlightDTO=(SearchFlightDTO) request.getSession().getAttribute("searchFlightDTO");
            Integer NumberPeople = (Integer) request.getSession().getAttribute("NumberPeople");
            modelMap.put("flight",FlightBooking);
            modelMap.put("flightTab",flights);
            BookingFlightDTO bookingDto=new BookingFlightDTO();
            modelMap.put("payment",bookingDto);
            modelMap.put("FlightPrice",totalFlight);
            modelMap.put("number",searchFlightDTO.getNumberPeopleRight());
            int paymentTimeOut=20*60;
            modelMap.put("timeout",paymentTimeOut);
            modelMap.put("total",total);
            return "/User/InformationCustomer/InformationFly";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Payment")
    public String Payment() {
        try {
            return "/User/InformationCustomer/Payment";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @GetMapping("Success")
    public String Success(HttpServletRequest request,ModelMap modelMap) {
        try {
            Account account=(Account)request.getSession().getAttribute("currentAccount");
            if(account==null){
                return "redirect:/Login";
            }
            Integer idBooking=(Integer)request.getSession().getAttribute("idBooking");

            Booking booking=bookingRepository.findById(idBooking).
                    orElseThrow(()->new RuntimeException("Booking not found"));

            modelMap.put("Booking",booking);
            return "/User/InformationCustomer/Success";
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
