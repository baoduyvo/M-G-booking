package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Service.ServiceDTO;
import com.vtnq.web.Entities.Service;

import java.util.List;

public interface ServiceService {
    public boolean addService(ServiceDTO serviceDTO);
    public List<Service>findAll(int id);
    public ServiceDTO findById(int id);
    public boolean deleteService(int id);
}
