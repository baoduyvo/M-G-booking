package com.vtnq.web.Service;

import com.vtnq.web.Entities.Type;

import java.util.List;

public interface TypeService {
    public boolean addType(Type type);
    public List<Type>findByHotel(int id);
}
