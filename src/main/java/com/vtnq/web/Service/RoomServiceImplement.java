package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Amenities.AmenitiesList;
import com.vtnq.web.DTOs.Room.RoomDTO;
import com.vtnq.web.DTOs.Room.RoomDetailHotel;
import com.vtnq.web.Entities.Hotel;
import com.vtnq.web.Entities.Picture;
import com.vtnq.web.Entities.Room;
import com.vtnq.web.Entities.Type;
import com.vtnq.web.Helper.FileHelper;
import com.vtnq.web.Repositories.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@Service
public class RoomServiceImplement implements RoomService{
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AmenityRepository amenityRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Override
    public boolean addRoom(List<Integer> roomTypes, List<Integer> roomCapacities, int idHotel,List<List<MultipartFile>>roomImages)
    {
        try {
            Hotel hotel=hotelRepository.findById(idHotel)
                    .orElseThrow(() -> new Exception("Hotel Not Found"));
            for (int i = 0; i < roomTypes.size(); i++) {
                Room room=new Room();
                Type type=typeRepository.findById(roomTypes.get(i)).orElseThrow(() -> new Exception("Type Not Found"));
                room.setType(type);

                room.setOccupancy(roomCapacities.get(i));
                room.setHotel(hotel);
                room.setStatus(false);
                Room saveRoom=roomRepository.save(room);

                String uploadDir = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", "images", "rooms").toString();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                List<MultipartFile>images=roomImages.get(i);
                for (MultipartFile item : images) {
                    String newFileName = FileHelper.generateImageName(item.getOriginalFilename());
                    if(saveFileWithStream(item,uploadDir,newFileName)){
                        Picture picture=new Picture();
                        picture.setImageUrl(newFileName);
                        picture.setRoomId(saveRoom.getId());
                        picture.setIsMain(false);
                        pictureRepository.save(picture);
                    }


                }

            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean saveFileWithStream(MultipartFile file, String uploadDir, String fileName) {
        try {
            // Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Đường dẫn tệp đích
            Path filePath = uploadPath.resolve(fileName);

            // Thực hiện trì hoãn 5 giây trước khi lưu file


            // Lưu file vào đĩa
            file.transferTo(filePath.toFile());

            // Kiểm tra xem file đã được lưu thành công chưa
            if (Files.exists(filePath)) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            // Nếu có lỗi, in thông báo lỗi
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Room> findAll(int id) {
        try {
            return roomRepository.findAll(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RoomDTO findById(int id) {
        try {
            return modelMapper.map(roomRepository.findById(id), new TypeToken<RoomDTO>(){}.getType());
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(RoomDTO roomDTO) {
        try {
            Hotel hotel=hotelRepository.findById(roomDTO.getIdHotel())
                    .orElseThrow(() -> new Exception("Hotel Not Found"));
            Type type=typeRepository.findById(roomDTO.getType())
                    .orElseThrow(() -> new Exception("Type Not Found"));
            Room room=modelMapper.map(roomDTO, Room.class);
            room.setType(type);
            room.setHotel(hotel);
            roomRepository.save(room);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            roomRepository.deleteById(id);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<RoomDetailHotel> ShowDetailHotel(int id) {
        try {
            List<RoomDetailHotel>roomDetailHotels=roomRepository.findRoomDetailHotel(id);
            for (RoomDetailHotel roomDetailHotel:roomDetailHotels) {
                List<AmenitiesList>amenitiesLists=amenityRepository.FindAmenitiesByRoomId(roomDetailHotel.getId());
                roomDetailHotel.setAmenitiesLists(amenitiesLists);
            }
            return roomDetailHotels;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RoomDetailHotel> findRoomDetailHotelWeb(int id, int QuantityRoom) {
        try {
            List<RoomDetailHotel>roomDetailHotels=roomRepository.findRoomDetailHotelWeb(id,QuantityRoom);
            for (RoomDetailHotel roomDetailHotel:roomDetailHotels) {
                Room room=roomRepository.FindRoomType(roomDetailHotel.getId()).stream().findFirst().orElse(null);
               Picture picture=pictureRepository.findByRoomId(room.getId()).stream().findFirst().orElse(null);
               if(picture!=null && picture.getImageUrl()!=null){
                   roomDetailHotel.setImageUrl(picture.getImageUrl());
               }

            }
            return roomDetailHotels;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Picture> FindPictureByRoomId(int id) {
        try {
            return pictureRepository.findByRoomId(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean UpdateMultipleImages(int id, List<MultipartFile> files) {
        try {
            String uploadDir = Paths.get(System.getProperty("user.dir"),  "src", "main", "resources", "static", "images", "rooms").toString();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            for (MultipartFile file : files) {
                String filenameImages=FileHelper.generateImageName(file.getOriginalFilename());
                Path filePathImages=uploadPath.resolve(filenameImages);
                file.transferTo(filePathImages.toFile());
                Picture pictureImage=new Picture();
                pictureImage.setRoomId(id);
                pictureImage.setImageUrl(filenameImages);
                pictureImage.setIsMain(false);
                pictureRepository.save(pictureImage);
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMultipleImagesRoom(int id) {
        try {
            Picture picture=pictureRepository.findById(id).get();
            String uploadDir = Paths.get(System.getProperty("user.dir"),  "src", "main", "resources", "static", "images", "rooms").toString();
            String oldFilename=picture.getImageUrl();
            File oldFile=new File(uploadDir,oldFilename);
            if(oldFile.exists() && oldFile.isFile()){
                oldFile.delete();
            }
            pictureRepository.deleteById(id);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
