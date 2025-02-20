package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Booking.BookingListFly;
import com.vtnq.web.DTOs.Flight.FlightDto;
import com.vtnq.web.DTOs.Flight.FlightListDTO;
import com.vtnq.web.DTOs.Flight.ResultFlightDTO;
import com.vtnq.web.DTOs.Seat.SeatDTO;
import com.vtnq.web.Entities.*;
import com.vtnq.web.Repositories.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class FlightServiceImplement implements FlightService{
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private AirportRepository airportRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ModelMapper modelMapper;
    private String generateRandomAlphanumericCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    @Override
    public boolean save(FlightDto flightDto) {
        try {
            Airline airline=airlineRepository.findById(flightDto.getAirline_id())
                    .orElseThrow(() -> new Exception("Airline not found"));
            Airport depature_AirPort=airportRepository.findById(flightDto.getDeparture_airport())
                    .orElseThrow(() -> new Exception("Airport not found"));
            Airport arrival_AirPort=airportRepository.findById(flightDto.getArrival_airport())
                    .orElseThrow(() -> new Exception("Airport not found"));
            Flight flight=modelMapper.map(flightDto, Flight.class);
            flight.setFlightCode(generateRandomAlphanumericCode(5));
            flight.setArrivalTime(flightDto.getArrivalInstant());
            flight.setDepartureTime(flightDto.getDepartureInstant());
            flight.setAirline(airline);
            flight.setDepartureAirport(depature_AirPort);
            flight.setArrivalAirport(arrival_AirPort);
            Flight insertFlight=flightRepository.save(flight);


            return insertFlight!=null && insertFlight.getId()>0 ;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<FlightListDTO> findAllByCountry(int id) {
        return flightRepository.findFlightListDTO(id);
    }

    @Override
    public FlightDto findById(int id) {
        try {
            return modelMapper.map(flightRepository.findById(id), new TypeToken<FlightDto>() {}.getType());
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean UpdateFlightDto(FlightDto flightDto) {
        try {
        Airline airline=airlineRepository.findById(flightDto.getAirline_id())
                .orElseThrow(() -> new Exception("Airline not found"));
        Airport departure_airport=airportRepository.findById(flightDto.getDeparture_airport())
                .orElseThrow(() -> new Exception("Departure Airport not found"));
        Airport arrival_airport=airportRepository.findById(flightDto.getArrival_airport())
                .orElseThrow(() -> new Exception("Arrival Airport not found"));
        Flight flight=modelMapper.map(flightDto, Flight.class);
        flight.setArrivalTime(flightDto.getArrivalInstant());
        flight.setDepartureTime(flightDto.getDepartureInstant());
        flight.setAirline(airline);
        flight.setDepartureAirport(departure_airport);
        flight.setArrivalAirport(arrival_airport);
        Flight updateFlight=flightRepository.save(flight);
        return updateFlight!=null && updateFlight.getId()>0;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<City> FindTopCity() {
        try {
            List<City> cities = flightRepository.findTopCities();
            return cities.size() > 5 ? cities.subList(0, 5) : cities;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ResultFlightDTO> SearchFlight(int departureAirport, int arrivalAirport, LocalDate departureTime, String TypeFlight, int totalPeople, LocalDateTime currentTime) {
        try {
            return flightRepository.findFlightsByAirportsAndDepartureTime(departureAirport,arrivalAirport,departureTime,TypeFlight,totalPeople,currentTime);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

    }





    @Override
    public ResultFlightDTO FindResultFlightAndHotel(int departureAirport, int arrivalAirport, LocalDate departureTime, String TypeFlight,LocalDateTime currentTime) {
        try {
            return flightRepository.findResulFlightAndHotel(departureAirport,arrivalAirport,departureTime,TypeFlight,currentTime).stream().findFirst().orElse(null);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override

    public boolean CreateSeat(SeatDTO seatDTO) {
        try {
            int seatsPerRow = 6;

            // Lấy tổng số ghế cho mỗi hạng
            int totalFirstClassSeats = seatDTO.getFirstClassSeat();
            int totalBusinessClassSeats = seatDTO.getBusinessClassSeat();
            int totalEconomyClassSeats = seatDTO.getEconomyClassSeat();

            ExecutorService executor = Executors.newFixedThreadPool(3); // Sử dụng 3 threads

            CompletableFuture<List<Seat>> firstClassFuture = CompletableFuture.supplyAsync(() -> {
                return createSeatsForClass(1, totalFirstClassSeats, "First Class", seatDTO.getIdFlight(), seatDTO.getPriceClassSeat(),0);
            }, executor);

            CompletableFuture<List<Seat>> businessClassFuture = CompletableFuture.supplyAsync(() -> {
                int startSeat = totalFirstClassSeats + 1;
                int endSeat = startSeat + totalBusinessClassSeats - 1;
                return createSeatsForClass(startSeat, endSeat, "Business Class", seatDTO.getIdFlight(), seatDTO.getPriceBusinessClassSeat(),0);
            }, executor);

            CompletableFuture<List<Seat>> economyClassFuture = CompletableFuture.supplyAsync(() -> {
                int startSeat = totalFirstClassSeats + totalBusinessClassSeats + 1;
                int endSeat = startSeat + totalEconomyClassSeats - 1;
                return createSeatsForClass(startSeat, endSeat, "Economy Class", seatDTO.getIdFlight(), seatDTO.getPriceEconomyClassSeat(),0);
            }, executor);

            CompletableFuture.allOf(firstClassFuture, businessClassFuture, economyClassFuture).join();

            List<Seat> allSeats = new ArrayList<>();
            allSeats.addAll(firstClassFuture.join());
            allSeats.addAll(businessClassFuture.join());
            allSeats.addAll(economyClassFuture.join());

            seatRepository.saveAll(allSeats);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public BigDecimal FindMinPriceDeparture(int departureAirport, int arrivalAirport, LocalDate departureTime, String TypeFlight, int totalPeople,LocalDateTime currentTime) {
        try {
            return flightRepository.FindMinPriceDeparture(departureAirport,arrivalAirport,departureTime,TypeFlight,totalPeople,currentTime);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public BigDecimal FindMaxPriceDeparture(int departureAirport, int arrivalAirport, LocalDate departureTime, String TypeFlight, int totalPeople,LocalDateTime currentTime) {
        try {
            return flightRepository.FindMaxPriceDeparture(departureAirport,arrivalAirport,departureTime,TypeFlight,totalPeople,currentTime);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    private List<Seat> createSeatsForClass(int startSeatIndex, int endSeatIndex, String seatClass, int flightId, BigDecimal price,int status) {
        List<Seat> seats = new ArrayList<>();
        int seatsPerRow = 6;
        int currentRow = (int) Math.ceil((double) startSeatIndex / seatsPerRow);
        int seatNumberInRow = (startSeatIndex - 1) % seatsPerRow;
        char currentColumn = (char) ('A' + seatNumberInRow);

        for (int i = startSeatIndex; i <= endSeatIndex; i++) {
            Flight flight = flightRepository.findById(flightId).orElse(null);
            seats.add(new Seat(currentColumn + Integer.toString(currentRow), seatClass, flight, price,status));

            currentColumn++;
            if (currentColumn > 'F') {
                currentColumn = 'A';
                currentRow++;
            }
        }
        return seats;
    }


    @Override
    public BookingListFly getResultPaymentFlight(int id) {
        try {
            return flightRepository.findFlightsByAirportsAndDepartureTime(id);
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultFlightDTO FindByIdFlight(int id) {
        try {
            return flightRepository.FindByFlightId(id);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ResultFlightDTO> FindArrivalTime(int departureAirport, int arrivalAirport, LocalDate arrivalTime, String TypeFlight,int totalPeople,LocalDateTime currentTime) {
        try {
        return flightRepository.FindArrivalTimeFlights(departureAirport, arrivalAirport, arrivalTime, TypeFlight,totalPeople,currentTime);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public BigDecimal FindMinPriceArrivalTime(int departureAirport, int arrivalAirport, LocalDate arrivalTime, String TypeFlight, int totalPeople,LocalDateTime currentTime) {
        try {
            return flightRepository.MinArrivalTime(departureAirport,arrivalAirport,arrivalTime,TypeFlight,totalPeople,currentTime);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public int CountFlight(int id) {
        try {
            return flightRepository.CountFlight(id);
        }catch (Exception ex){
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public BigDecimal FindMaxPriceArrivalTime(int departureAirport, int arrivalAirport, LocalDate arrivalTime, String TypeFlight, int totalPeople,LocalDateTime currentTime) {
        try {
            return flightRepository.MaxArrivalTime(departureAirport,arrivalAirport,arrivalTime,TypeFlight,totalPeople,currentTime);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


}
