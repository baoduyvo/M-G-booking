package com.vtnq.web.Service;

import com.vtnq.web.DTOs.Airline.AirlineDto;
import com.vtnq.web.DTOs.Airline.ListAirlineDto;
import com.vtnq.web.DTOs.Airline.UpdateAirlineDTO;
import com.vtnq.web.Entities.Airline;
import com.vtnq.web.Entities.Picture;
import com.vtnq.web.Helper.FileHelper;
import com.vtnq.web.Repositories.AirlineRepository;
import com.vtnq.web.Repositories.CountryRepository;
import com.vtnq.web.Repositories.PictureRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class AirlineServiceImplement implements AirlineService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Override
    public boolean addAirline(AirlineDto airlineDto) {
        try {
            Airline airline=modelMapper.map(airlineDto, Airline.class);
            airline.setCountryId(airlineDto.getCountry_id());
            Airline savedAirline=airlineRepository.save(airline);
            int airline_id=savedAirline.getId();
            MultipartFile multipartFile=airlineDto.getImage();
            if(multipartFile!=null && !multipartFile.isEmpty()){
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/flight";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String filename = FileHelper.generateImageName(multipartFile.getOriginalFilename());
                Path filePath = uploadPath.resolve(filename);
                multipartFile.transferTo(filePath);
                Picture image=new Picture();
                image.setIsMain(true);
                image.setAirlineId(airline_id);
                image.setImageUrl(filename);
                pictureRepository.save(image);
                return true;
            }else {
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existName(String airlineName) {
        return airlineRepository.IsExistName(airlineName);
    }

    @Override
    public List<ListAirlineDto> findAll() {
        try {
            String flightUrl = "http://localhost:8686/images/flight/";
            List<ListAirlineDto> airlines = airlineRepository.ShowAll();


            List<ListAirlineDto> flightDtos = modelMapper.map(airlines, new TypeToken<List<ListAirlineDto>>(){}.getType());

            for (ListAirlineDto flightDto : flightDtos) {
                flightDto.setImage_url(flightUrl+flightDto.getImage_url()); // Set the base flight URL
            }

            return flightDtos;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public UpdateAirlineDTO findAirlineById(int id) {
        try {
            return airlineRepository.FindById(id);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateArline(UpdateAirlineDTO updateAirlineDTO, MultipartFile file) {
        try {
            String imagecase = file!= null ? file.getOriginalFilename() : "null";
            Airline airline=modelMapper.map(updateAirlineDTO, Airline.class);
            switch (imagecase){
                case "null":

                    airline.setCountryId(updateAirlineDTO.getIdCountry());
                    airlineRepository.save(airline);
                    break;
                default:
                    UpdateAirlineDTO FindID=airlineRepository.FindById(updateAirlineDTO.getId());
                    if (FindID == null) {
                        throw new RuntimeException("Flight not found!");
                    }
                    String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/flight";
                    String oldFilename=FindID.getImage();
                    File oldFile=new File(uploadDir,oldFilename);
                    if(oldFile.exists() && oldFile.isFile()){
                        oldFile.delete();
                    }

                    airline=modelMapper.map(updateAirlineDTO, Airline.class);
                    airline.setCountryId(updateAirlineDTO.getIdCountry());
                    airlineRepository.save(airline);
                    MultipartFile multipartFile=file;

                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    String filename=FileHelper.generateImageName(multipartFile.getOriginalFilename());
                    Path filePath = uploadPath.resolve(filename);
                    multipartFile.transferTo(filePath.toFile());
                    Picture image= pictureRepository.findByImageId(updateAirlineDTO.getId());
                    image.setIsMain(true);
                    image.setAirlineId(updateAirlineDTO.getId());
                    image.setImageUrl(filename);
                    pictureRepository.save(image);
                    break;
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Airline> searchAirline(int departureAirport, int arrivalAirport, LocalDate departureTime, String TypeFlight) {
        return modelMapper.map(airlineRepository.SearchAirline(departureAirport,arrivalAirport,departureTime,TypeFlight),new TypeToken<List<Airline>>(){}.getType());
    }

    @Override
    public List<Airline> SearchAirlineArrival(int departureAirport, int arrivalAirport, LocalDate arrivalTime, String TypeFlight) {
        try {
        return airlineRepository.SearchAirlineArrival(departureAirport,arrivalAirport,arrivalTime,TypeFlight);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Airline> FindByCountryId(int countryId) {
        try {
            return airlineRepository.FindAirlineByCountryId(countryId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int CountAirline() {
        try {
            return airlineRepository.CountAirline();
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
