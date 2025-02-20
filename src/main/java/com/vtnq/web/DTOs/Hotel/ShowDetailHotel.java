    package com.vtnq.web.DTOs.Hotel;

    import org.jsoup.Jsoup;

    import java.math.BigDecimal;

    public class ShowDetailHotel {
        private int id;
        private String name;
        private String address;
        private String City;
        private String description;
        private String Country;
        private BigDecimal price;

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public ShowDetailHotel(int id, String name, String address, String city, String description, String country,BigDecimal price) {
            this.id = id;
            this.name = name;
            this.address = cleanHtml(address) ;
            City = city;
            this.description = cleanHtml(description) ;
            Country = country;
            this.price = price;
        }
        public String cleanHtml(String input){
        if(input!=null){
            return Jsoup.parse(input).text();
        }
        return "";
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }
    }
