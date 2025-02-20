package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Room.RoomDTO;
import com.vtnq.web.DTOs.Room.RoomDetailHotel;
import com.vtnq.web.Entities.Picture;
import com.vtnq.web.Entities.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface RoomService {
    public boolean addRoom(List<Integer>roomTypes, List<Integer>roomCapacities, int idHotel,
                           List<List<MultipartFile>>roomImages);
    public List<Room>findAll(int id);
    public RoomDTO findById(int id);
    public boolean update(RoomDTO roomDTO);
    public boolean delete(int id);
    public List<RoomDetailHotel>ShowDetailHotel(int id);
    public List<RoomDetailHotel>findRoomDetailHotelWeb(int id,int QuantityRoom);
    public List<Picture>FindPictureByRoomId(int id);
    public boolean UpdateMultipleImages(int id,List<MultipartFile>files);
    public boolean deleteMultipleImagesRoom(int id);
}
