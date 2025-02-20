package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Service.ServiceDTO;
import com.vtnq.web.Entities.Hotel;
import com.vtnq.web.Repositories.HotelRepository;
import com.vtnq.web.Repositories.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceServiceImplement implements ServiceService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Override
    public boolean addService(ServiceDTO serviceDTO) {
        try {
            Hotel hotel=hotelRepository.findById(serviceDTO.getHotelId())
                    .orElseThrow(()-> new RuntimeException("Hotel not found"));
            com.vtnq.web.Entities.Service service = modelMapper.map(serviceDTO, com.vtnq.web.Entities.Service.class);
            service.setHotel(hotel);
            serviceRepository.save(service);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<com.vtnq.web.Entities.Service> findAll(int id) {
        try {
            return serviceRepository.findByHotel(id);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ServiceDTO findById(int id) {
        return modelMapper.map(serviceRepository.findById(id), new TypeToken<ServiceDTO>(){}.getType());
    }

    @Override
    public boolean deleteService(int id) {
        try {
            serviceRepository.deleteById(id);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
