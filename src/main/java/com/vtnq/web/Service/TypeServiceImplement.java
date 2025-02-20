package com.vtnq.web.Service;

import com.vtnq.web.Entities.Type;
import com.vtnq.web.Repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImplement implements TypeService {
   @Autowired
   private TypeRepository typeRepository;
    @Override
    public boolean addType(Type type) {
        try {
            Type savedType = typeRepository.save(type);
            return savedType != null && savedType.getId()>0;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Type> findByHotel(int id) {
        try {
            return typeRepository.FindByHotel(id);
        }catch (Exception e) {
            return null;
        }
    }
}
