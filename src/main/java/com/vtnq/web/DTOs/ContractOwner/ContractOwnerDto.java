package com.vtnq.web.DTOs.ContractOwner;

import java.io.Serializable;

/**
 * DTO for {@link com.vtnq.web.Entities.ContractOwner}
 */
public class ContractOwnerDto implements Serializable {
   private int id;
   private int ownerId;
   private String ownerName;
   private String email;
   private boolean status;
   public ContractOwnerDto(int id, int ownerId, String ownerName, String email, String phone, String address, int idHotel, String hotelName,boolean status) {
      this.id = id;
      this.ownerId = ownerId;
      this.ownerName = ownerName;
      this.email = email;
      this.phone = phone;
      this.address = address;
      this.idHotel = idHotel;
      this.hotelName = hotelName;
      this.status = status;
   }

   public boolean isStatus() {
      return status;
   }

   public void setStatus(boolean status) {
      this.status = status;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getOwnerId() {
      return ownerId;
   }

   public void setOwnerId(int ownerId) {
      this.ownerId = ownerId;
   }

   public String getOwnerName() {
      return ownerName;
   }

   public void setOwnerName(String ownerName) {
      this.ownerName = ownerName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public int getIdHotel() {
      return idHotel;
   }

   public void setIdHotel(int idHotel) {
      this.idHotel = idHotel;
   }

   public String getHotelName() {
      return hotelName;
   }

   public void setHotelName(String hotelName) {
      this.hotelName = hotelName;
   }

   private String phone;
   private String address;
   private int idHotel;
   private String hotelName;

}