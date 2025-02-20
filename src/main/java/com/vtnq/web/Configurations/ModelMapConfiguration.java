package com.vtnq.web.Configurations;

import com.vtnq.web.DTOs.Account.AccountDto;
import com.vtnq.web.DTOs.Airport.AirportDto;
import com.vtnq.web.DTOs.Flight.FlightDto;
import com.vtnq.web.DTOs.Room.RoomDTO;
import com.vtnq.web.DTOs.Service.ServiceDTO;
import com.vtnq.web.Entities.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapConfiguration {
@Bean
public ModelMapper modelMap() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.typeMap(Airport.class, AirportDto.class).addMappings(mapping -> {
            mapping.map(airport->airport.getCity().getId(),AirportDto::setIdCity);
    });
    modelMapper.typeMap(Service.class, ServiceDTO.class).addMappings(mapping -> {
        mapping.map(service->service.getHotel().getId(),ServiceDTO::setHotelId);
    });
    modelMapper.typeMap(Room.class, RoomDTO.class).addMappings(mapping -> {
        mapping.map(rooms->rooms.getHotel().getId(),RoomDTO::setIdHotel);
        mapping.map(rooms->rooms.getType().getId(),RoomDTO::setType);
    });
    modelMapper.typeMap(Flight.class, FlightDto.class).addMappings(mapping -> {
        mapping.map(flight->flight.getDepartureAirport().getId(),FlightDto::setDeparture_airport);
        mapping.map(flight->flight.getArrivalAirport().getId(),FlightDto::setArrival_airport);
        mapping.map(flight->flight.getAirline().getId(),FlightDto::setAirline_id);
    });
    modelMapper.typeMap(Account.class, AccountDto.class).addMappings(mapping -> {
        mapping.map(account->account.getCountryId(),AccountDto::setCountryId);
    });
return modelMapper;
}
}
