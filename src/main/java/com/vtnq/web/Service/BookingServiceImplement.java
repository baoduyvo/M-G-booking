package com.vtnq.web.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vtnq.web.DTOs.Booking.BookingFlightDTO;
import com.vtnq.web.DTOs.Booking.BookingFlightDetail;
import com.vtnq.web.DTOs.Booking.BookingHotelDTO;
import com.vtnq.web.DTOs.BookingFlightDto;
import com.vtnq.web.DTOs.HistoryOrder.HistoryBooking;
import com.vtnq.web.DTOs.HistoryOrder.HistoryOrderFlight;
import com.vtnq.web.DTOs.HistoryOrder.HistoryOrderHotel;
import com.vtnq.web.Entities.*;
import com.vtnq.web.Repositories.*;
import com.vtnq.web.WebSocket.RoomUpdateWebSocketHandler;
import com.vtnq.web.WebSocket.SeatUpdateWebSocketHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class BookingServiceImplement implements BookingService {
    @Autowired
    private BookingFlightRepository bookingFlightRepository;
    @Autowired
    private BookingFlightDetailRepository bookingFlightDetailRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private SeatUpdateWebSocketHandler seatUpdateWebSocketHandler;
    @Autowired
    private RoomUpdateWebSocketHandler roomUpdateWebSocketHandler;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BookingRoomRepository bookingRoomRepository;
    @Autowired
    private BookingHotelDetailRepository hotelDetailRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JavaMailSender javaMailSender;

    private String generateRandomAlphanumericCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
    private Account getAccount(int userId){
    return accountRepository.findById(userId).orElseThrow(()->
            new RuntimeException("Account not found"));
    }
    private List<BookingFlightDetail> parseBookingDetails(String bookings) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(bookings, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, BookingFlightDetail.class));
    }
    private void validateSeats(List<BookingFlightDetail> bookingDetails) {
        try {
            for (BookingFlightDetail detail : bookingDetails) {
                Flight flight=flightRepository.findById(detail.getFlightId()).orElseThrow(()->new RuntimeException("Flight not found"));
                if(flight.getDepartureTime().isBefore(LocalDateTime.now().plusHours(1))){

                    throw new RuntimeException("Bookings must be made at least 1 hour before departure.");
                }
                Seat seat = seatRepository.findById(detail.getId())
                        .orElseThrow(() -> new RuntimeException("Seat Not Found"));
                if (seat.getStatus() != null && seat.getStatus() == 1) {
                    seatUpdateWebSocketHandler.notifySeatStatus(seat, false);
                    throw new RuntimeException("Seat is already booked");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private BookingFlight saveBookingFlight(Account account,BigDecimal totalPrice){
        BookingFlight bookingFlight=new BookingFlight();
        bookingFlight.setTotalPrice(totalPrice);
        bookingFlight.setUser(account);
        return bookingFlightRepository.save(bookingFlight);
    }
    private void saveBookingDetails(BookingFlight bookingFlight,List<BookingFlightDetail>bookingFlightDetails){
        for (BookingFlightDetail bookingFlightDetail : bookingFlightDetails) {
            Flight flight=flightRepository.findById(bookingFlightDetail.getFlightId())
                    .orElseThrow(()->new RuntimeException("Flight not found"));
            Seat seat=seatRepository.findById(bookingFlightDetail.getId())
                    .orElseThrow(()->new RuntimeException("Seat not found"));
            seat.setStatus(1);
            seatRepository.save(seat);
            com.vtnq.web.Entities.BookingFlightDetail bookingFlightDetailEntity=new com.vtnq.web.Entities.BookingFlightDetail();
            bookingFlightDetailEntity.setFlight(flight);
            bookingFlightDetailEntity.setBookingFlight(bookingFlight);
            bookingFlightDetailEntity.setTotalPrice(bookingFlightDetail.getTotalPrice());

            bookingFlightDetailEntity.setSeat(seat);
            bookingFlightDetailRepository.save(bookingFlightDetailEntity);
        }
    }
    private Booking CreateAndSaveBooking(BookingFlight saveBookingFlight,BookingFlightDTO bookingFlightDTO, Account account){
        Booking booking=new Booking();
        booking.setBookingFlight(saveBookingFlight);
        booking.setCreatedAt(Instant.now());
        booking.setUserId(bookingFlightDTO.getUserId());
        booking.setTotalPrice(bookingFlightDTO.getTotalPrice());
        booking.setBookingCode(generateRandomAlphanumericCode(5));
        return bookingRepository.save(booking);
    }
    private void updateAccountLevel(Account account){
        List<Booking>userBookings=bookingRepository.FindBookingByUserId(account.getId());
        BigDecimal totalSpent = userBookings.stream()
                .map(Booking::getTotalPrice)
                .filter(Objects::nonNull) // Loại bỏ các giá trị null
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalSpent.compareTo(new BigDecimal("20000000")) > 0) {
            Level level = levelRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Level Not Found"));
            account.setLevel(level);
            accountRepository.save(account);
        } else if (totalSpent.compareTo(new BigDecimal("10000000")) >= 0) {
            Level level = levelRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("Level Not Found"));
            account.setLevel(level);
            accountRepository.save(account);
        }

    }
    private void sendConfirmationEmail(String recipientEmail, Booking booking, Account account) {
        try {
            // Create MimeMessage object
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set up email information
            helper.setTo(recipientEmail);
            helper.setSubject("Payment Confirmation - Booking Successful");
            helper.setText("Hello " + account.getFullName() + ",\n\n" +  // Add the name of the booker
                    "Thank you for your payment and successful booking.\n\n" +
                    "Booking Information:\n" +
                    "Booking Code: " + booking.getBookingCode() + "\n" +
                    "Amount: " + booking.getTotalPrice().toString() + "\n\n" +
                    "We wish you a pleasant trip!");

            // Send email
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int addBooking(BookingFlightDTO bookingFlightDTO, String bookings) {
        try {

            List<BookingFlightDetail> bookingsDetail =parseBookingDetails(bookings);
            validateSeats(bookingsDetail);

            Account account = getAccount(bookingFlightDTO.getUserId());
            BookingFlight saveBookingFlight=saveBookingFlight(account,bookingFlightDTO.getTotalPrice());
            saveBookingDetails(saveBookingFlight,bookingsDetail);
            Booking booking=CreateAndSaveBooking(saveBookingFlight,bookingFlightDTO,account);
            updateAccountLevel(account);
            sendConfirmationEmail(account.getEmail(),booking,account);
          return booking.getId();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Seat getBookedSeatFromBookings(String bookings) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<BookingFlightDetail> bookingsDetail = objectMapper.readValue(bookings,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, BookingFlightDetail.class));
            if (!bookingsDetail.isEmpty()) {
                int seatId = bookingsDetail.get(0).getId();  // Assuming 'id' in BookingFlightDetail corresponds to seat id
                Seat seat = seatRepository.findById(seatId).orElse(null);
                return seat;// Assuming the first seat in the booking details is the one you're interested in
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void validateRoomAvailability(int typeId,int quantityRoom){
        try {
            List<Room>rooms=roomRepository.FindRoomType(typeId);
            if(rooms==null||rooms.size()<quantityRoom){
                roomUpdateWebSocketHandler.NotifyRoomStatus(typeId,false);
                throw new RuntimeException("Insufficient rooms available");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void validateSeatsBooking(String bookings) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        List<BookingFlightDetail> bookingDetails = objectMapper.readValue(bookings, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, BookingFlightDetail.class));


        for (BookingFlightDetail detail : bookingDetails) {
            Seat seat = seatRepository.findById(detail.getId())
                    .orElseThrow(() -> new RuntimeException("Seat Not Found"));
            Flight flight=flightRepository.findById(detail.getFlightId()).orElseThrow(() -> new RuntimeException("Flight Not Found"));
            if(flight.getDepartureTime().isBefore(LocalDateTime.now().plusHours(1))){

                throw new RuntimeException("Bookings must be made at least 1 hour before departure.");
            }

            if (seat.getStatus() != null && seat.getStatus() == 1) {
                seatUpdateWebSocketHandler.notifySeatStatus(seat, false);
                throw new RuntimeException("Seat is already booked");
            }
        }
    }
    private BookingRoom createBookingRoom(Account account,BigDecimal totalPrice){
        BookingRoom bookingRoom = new BookingRoom();
        bookingRoom.setUser(account);
        bookingRoom.setTotalPrice(totalPrice);
        return bookingRoomRepository.save(bookingRoom);
    }
    private void saveBookingRoomDetails(BookingRoom bookingRoom, BookingHotelDTO bookingHotelDTO, int quantityRoom) {
        for (int i = 0; i < quantityRoom; i++) {
            Room room = roomRepository.findByTypeId(bookingHotelDTO.getTypeId()).stream().findFirst().orElse(null);
            if (room == null) {
                throw new RuntimeException("Room Not Found");
            }
            room.setStatus(true);
            roomRepository.save(room);

            BookingRoomDetail bookingRoomDetail = new BookingRoomDetail();
            bookingRoomDetail.setBookingRoom(bookingRoom);
            bookingRoomDetail.setRoom(room);
            bookingRoomDetail.setTotalPrice(bookingHotelDTO.getPrice());
            bookingRoomDetail.setCheckInDate(bookingHotelDTO.getCheckInDate());
            bookingRoomDetail.setCheckOutDate(bookingHotelDTO.getCheckOutDate());
            hotelDetailRepository.save(bookingRoomDetail);
        }
    }
    private BookingFlight createBookingFlight(BookingFlightDTO flightDTO, String bookings) throws Exception {
        Account account = getAccount(flightDTO.getUserId());
        BookingFlight bookingFlight = new BookingFlight();
        bookingFlight.setUser(account);
        bookingFlight.setTotalPrice(flightDTO.getTotalPrice());
        BookingFlight savedBookingFlight = bookingFlightRepository.save(bookingFlight);

        ObjectMapper objectMapper = new ObjectMapper();
        List<BookingFlightDetail> bookingDetails = objectMapper.readValue(bookings, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, BookingFlightDetail.class));

        for (BookingFlightDetail detail : bookingDetails) {
            Flight flight = flightRepository.findById(detail.getFlightId())
                    .orElseThrow(() -> new RuntimeException("Flight Not Found"));
            Seat seat = seatRepository.findById(detail.getId())
                    .orElseThrow(() -> new RuntimeException("Seat Not Found"));

            seat.setStatus(1);
            seatRepository.save(seat);

            com.vtnq.web.Entities.BookingFlightDetail bookingFlightDetail = new com.vtnq.web.Entities.BookingFlightDetail();
            bookingFlightDetail.setFlight(flight);
            bookingFlightDetail.setBookingFlight(savedBookingFlight);
            bookingFlightDetail.setTotalPrice(detail.getTotalPrice());

            bookingFlightDetail.setSeat(seat);
            bookingFlightDetailRepository.save(bookingFlightDetail);
        }
        return savedBookingFlight;
    }
    private Booking createAndSaveFinalBooking(BookingRoom bookingRoom, BookingFlight bookingFlight, BigDecimal amount) {
        Booking finalBooking = new Booking();
        finalBooking.setBookingRoom(bookingRoom);
        finalBooking.setBookingFlight(bookingFlight);
        finalBooking.setUserId(bookingFlight.getUser().getId());
        finalBooking.setCreatedAt(Instant.now());
        finalBooking.setBookingCode(generateRandomAlphanumericCode(5));
        finalBooking.setTotalPrice(amount);
        return bookingRepository.save(finalBooking);
    }
    @Override
    public int addBookingHotel(BookingHotelDTO bookingHotelDTO, int QuantityRoom, BookingFlightDTO flightDTO, String bookings, BigDecimal amount) {
        try {
            Account account = getAccount(bookingHotelDTO.getUserId());
            validateRoomAvailability(bookingHotelDTO.getTypeId(),QuantityRoom);
            validateSeatsBooking(bookings);
            BookingRoom bookingRoom=createBookingRoom(account,bookingHotelDTO.getTotalPrice());
            saveBookingRoomDetails(bookingRoom,bookingHotelDTO,QuantityRoom);
            BookingFlight bookingFlight = createBookingFlight(flightDTO, bookings);
            Booking finalBooking = createAndSaveFinalBooking(bookingRoom, bookingFlight, amount);
            sendConfirmationEmail(account.getEmail(),finalBooking,account);
            return finalBooking.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Booking> FindBookings(int id) {
        try {
            return bookingRepository.findBookingByCountry(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<com.vtnq.web.Entities.BookingFlightDetail> findBookingFlights(int id) {
        try {
            return bookingRepository.FindBookingByFlight(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public BigDecimal getTotalPrice(int id) {
        try {
            return bookingRepository.getBookingTotalPrice(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BookingRoomDetail> getBookingRooms(int id) {
        try {
            return bookingRepository.getBookingRoomDetails(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public BigDecimal GetTotalPriceHotel(int id) {
        try {
            return bookingRepository.getBookingHotelPrice(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public int CountBookings(int id) {
        try {
            return bookingRepository.CountBooking(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<HistoryOrderFlight> FindHistoryOrderFlights(int id,int page,int size,String flightCode,LocalDate departureTime,LocalDate arrivalTime) {
        try {
            int offset=page*size;
            List<Object[]>results=bookingFlightRepository.FindFlightByUser(id,size,offset,flightCode,departureTime,arrivalTime);
            return HistoryOrderFlight.mapHistoryOrderFlight(results);

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<HistoryOrderHotel> FindHistoryOrderHotels(int id) {
        try {
            List<Object[]>results=bookingRoomRepository.FindHotelById(id);

            List<HistoryOrderHotel>historyOrderHotels=HistoryOrderHotel.mapHistoryOrderHotel(results);
            for (HistoryOrderHotel historyOrderHotel : historyOrderHotels) {
                int total=bookingRoomRepository.CountBookingRoomByRoomId(historyOrderHotel.getId());
                historyOrderHotel.setTotalRoom(total);
            }
            return historyOrderHotels;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        }

    @Override
    public List<HistoryBooking> FindHistoryBookings(int id, int page, int size,String booking) {
        try {
            int offset = page * size;
            List<Object[]>results=bookingRepository.FindBookingByAccount(id,size,offset,booking);
            return HistoryBooking.getHistoryBookings(results);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


}
