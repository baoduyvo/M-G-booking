package com.vtnq.web.Service;

import com.vtnq.web.DTOs.District.DistrictDto;
import com.vtnq.web.Entities.City;
import com.vtnq.web.Entities.District;
import com.vtnq.web.Repositories.CityRepository;
import com.vtnq.web.Repositories.DistrictRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImplement implements DistrictService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public boolean addDistrict(DistrictDto districtDto) {
        try {
            City city = cityRepository.findById(districtDto.getIdCity())
                    .orElseThrow(() -> new RuntimeException("City not found"));
            District district = modelMapper.map(districtDto, District.class);
            district.setCity(city);
            District insertDistrict = districtRepository.save(district);
            return insertDistrict != null && insertDistrict.getId() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<District> findDistrict(int id) {
        try {
            return districtRepository.findDistrictByCityId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existName(String name) {
        return districtRepository.existsByName(name);
    }

    @Override
    public DistrictDto findDistrictById(int id) {
        return modelMapper.map(districtRepository.findById(id),new TypeToken<DistrictDto>(){}.getType());
    }

    @Override
    public boolean delete(int id) {
        try {
           districtRepository.deleteById(id);
           return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
