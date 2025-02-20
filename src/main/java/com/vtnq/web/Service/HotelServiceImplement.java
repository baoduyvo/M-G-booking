package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Booking.BookingHotel;
import com.vtnq.web.DTOs.Hotel.*;
import com.vtnq.web.Entities.*;
import com.vtnq.web.Helper.FileHelper;
import com.vtnq.web.Repositories.*;
import org.modelmapper.ModelMapper;
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
public class HotelServiceImplement implements HotelService{
    @Autowired
    private ModelMapper model;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private HotelsOwnerRepository hotelsOwnerRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private ContractOwnerRepository contractOwnerRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Override
    public boolean addHotel(HotelDto hotel) {
        try {
            Account account=accountRepository.findById(hotel.getOwnerId())
                    .orElseThrow(()-> new RuntimeException("Account not found"));
            District district=districtRepository.findById(hotel.getDistrictId())
                    .orElseThrow(()-> new RuntimeException("District not found"));
            Hotel hotelEntity =new Hotel();
            hotelEntity.setName(hotel.getName());
            hotelEntity.setDecription(hotel.getDecription());
            hotelEntity.setAddress(hotel.getAddress());
            hotelEntity.setCityId(hotel.getCityId());
            hotelEntity.setDistrict(district);

            Hotel insertHotel=hotelRepository.save(hotelEntity);
            String uploadDir = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "static", "images", "hotels").toString();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String newFileName = FileHelper.generateImageName(hotel.getImage().getOriginalFilename());
            if(saveFileWithStream(hotel.getImage(), uploadDir, newFileName)){
                Picture picture=new Picture();
                picture.setHotelId(insertHotel.getId());
                picture.setImageUrl(newFileName);
                picture.setIsMain(true);
                pictureRepository.save(picture);
            }else{
                return false;
            }

            if(hotel.getImages() != null && !hotel.getImages().isEmpty()){
                for(MultipartFile multipartFile :hotel.getImages()){
                    String filenameImages=FileHelper.generateImageName(multipartFile.getOriginalFilename());
                    Path filePathImages=uploadPath.resolve(filenameImages);
                    multipartFile.transferTo(filePathImages.toFile());
                    Picture pictureImage=new Picture();
                    pictureImage.setHotelId(insertHotel.getId());
                    pictureImage.setImageUrl(filenameImages);
                    pictureImage.setIsMain(false);
                    pictureRepository.save(pictureImage);
                }

            }
            HotelsOwner hotelsOwner=new HotelsOwner();
            hotelsOwner.setHotel(insertHotel);
            hotelsOwner.setOwner(account);
            hotelsOwnerRepository.save(hotelsOwner);
            ContractOwner contractOwner=new ContractOwner();
            contractOwner.setOwnerId(hotel.getOwnerId());
            contractOwner.setHotelId(insertHotel.getId());
            contractOwner.setCommissionRate(5.0);
            contractOwner.setStatus(false);
            contractOwnerRepository.save(contractOwner);
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
    public List<HotelListDto> FindHotelsByOwner(int id) {
        try {
            return hotelRepository.FindHotelByHotelId(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public HotelUpdateDTO findHotels(int id) {
        try {
            return hotelRepository.findHotelById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ImageHotelListDTO> findImage(int id) {
        try {
        return pictureRepository.findImageHotel(id);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean UpdateHotel(HotelUpdateDTO hotel,MultipartFile file) {
        try {
            District district=districtRepository.findById(hotel.getDistrict_id())
                    .orElseThrow(()-> new RuntimeException("District not found"));
        Hotel hotelEntity =model.map(hotel,Hotel.class);
            if (file == null |file.isEmpty()) {
                hotelEntity.setDistrict(district);
                hotelRepository.save(hotelEntity);
                return true;
            }
        hotelEntity.setDistrict(district);
        hotelRepository.save(hotelEntity);
            HotelUpdateDTO updateDTO = hotelRepository.findHotelById(hotel.getId());
            if (updateDTO == null) {
                throw new RuntimeException("Hotel not found!");
            }
            String uploadDir = Paths.get(System.getProperty("user.dir"),  "src", "main", "resources", "static", "images", "hotels").toString();
            String oldFilename = updateDTO.getImageUrl();
            File oldFile = new File(uploadDir, oldFilename);
            if (oldFile.exists() && oldFile.isFile()) {
                oldFile.delete();
            }
            String newFileName = FileHelper.generateImageName(file.getOriginalFilename());
            if (saveFileWithStream(file, uploadDir, newFileName)) {
                Picture image= pictureRepository.findByHotelId(hotel.getId());

                image.setHotelId(updateDTO.getId());
                image.setImageUrl(newFileName);
                image.setIsMain(true);
                pictureRepository.save(image);
            }
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteImageHotel(int id) {
        try {
            Picture picture=pictureRepository.findById(id).get();
            String uploadDir = Paths.get(System.getProperty("user.dir"),  "src", "main", "resources", "static", "images", "hotels").toString();
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

    @Override
    public boolean updateMultipleImages(int id, List<MultipartFile> files) {
       try {
           String uploadDir = Paths.get(System.getProperty("user.dir"),  "src", "main", "resources", "static", "images", "hotels").toString();
            Path uploadPath = Paths.get(uploadDir);
           if (!Files.exists(uploadPath)) {
               Files.createDirectories(uploadPath);
           }
           for (MultipartFile file : files) {
               String filenameImages=FileHelper.generateImageName(file.getOriginalFilename());
               Path filePathImages=uploadPath.resolve(filenameImages);
               file.transferTo(filePathImages.toFile());
               Picture pictureImage=new Picture();
               pictureImage.setHotelId(id);
               pictureImage.setImageUrl(filenameImages);
               pictureImage.setIsMain(false);
               pictureRepository.save(pictureImage);
           }
           return true;
       }catch (Exception ex){
           ex.printStackTrace();
           return false;
       }
    }

    @Override
    public List<HotelSearchDTO> SearchHotels(int id,int quantityRoom,BigDecimal minPrice,BigDecimal maxPrice) {
        try {
            return hotelRepository.SearchHotel(id,quantityRoom,minPrice,maxPrice);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<HotelSearchDTO> SearchHotelsMobile(int id, int quantityRoom) {
        try {
            return hotelRepository.SearchHotelMoBile(id,quantityRoom);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ShowDetailHotel FindDetailHotel(int id) {
        try {
            return hotelRepository.showDetailHotel(id);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Picture> FindImageInDetailHotel(int id) {
        try {
            return pictureRepository.FindImageInDetailHotel(id);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public BookingHotel FindBookingHotel(int id) {
        try {
            return hotelRepository.FindBookingHotel(id).stream().findFirst().orElse(null);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<HotelList> ShowHotelsAll(int id) {
        try {
            return hotelRepository.showHotelList(id);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public BigDecimal FindMinPriceHotel() {
        try {
            return hotelRepository.FindMinHotel();
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public BigDecimal FindMaxPriceHotel() {
        try {
            return hotelRepository.FindMaxHotel();
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
