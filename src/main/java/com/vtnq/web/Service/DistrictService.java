package com.vtnq.web.Service;

import com.vtnq.web.DTOs.District.DistrictDto;
import com.vtnq.web.Entities.District;

import java.util.List;

public interface DistrictService {
    public boolean addDistrict(DistrictDto districtDto);
    public List<District>findDistrict(int id);
    public boolean existName(String name);
    public DistrictDto findDistrictById(int id);
    public boolean delete(int id);
}
