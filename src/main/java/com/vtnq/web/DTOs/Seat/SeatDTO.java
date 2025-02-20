package com.vtnq.web.DTOs.Seat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.DecimalMin;

public class SeatDTO {
    int firstClassSeat;
    @Min(value = 0, message = "Business Class seat cannot be negative")
    int businessClassSeat;
    int economyClassSeat;
    int idFlight;
    private List<String>validationErrors=new ArrayList<>();
    public BigDecimal getPriceClassSeat() {
        return priceClassSeat;
    }
    public String getValidationErrors() {
        StringBuilder errorMessages = new StringBuilder("Validation errors:\n");
        validationErrors.forEach(error ->
                errorMessages.append(String.format(" %s\n", error))
        );
        return errorMessages.toString();
    }
    public boolean hasErrors(){
        return !validationErrors.isEmpty();
    }
    public void setPriceClassSeat(BigDecimal priceClassSeat) {
        this.priceClassSeat = priceClassSeat;

    }

    public BigDecimal getPriceBusinessClassSeat() {
        return priceBusinessClassSeat;
    }

    public void setPriceBusinessClassSeat(BigDecimal priceBusinessClassSeat) {
        this.priceBusinessClassSeat = priceBusinessClassSeat;


    }

    public BigDecimal getPriceEconomyClassSeat() {
        return priceEconomyClassSeat;
    }

    public void setPriceEconomyClassSeat(BigDecimal priceEconomyClassSeat) {
        this.priceEconomyClassSeat = priceEconomyClassSeat;

    }

    public int getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(int idFlight) {
        this.idFlight = idFlight;
    }
    @NotNull(message = "Price for First Class seat is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price for First Class seat must be non-negative")
    private BigDecimal priceClassSeat = BigDecimal.ZERO;

    @NotNull(message = "Price for Business Class seat is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price for Business Class seat must be non-negative")
    private BigDecimal priceBusinessClassSeat = BigDecimal.ZERO;

    @NotNull(message = "Price for Economy Class seat is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price for Economy Class seat must be non-negative")
    private BigDecimal priceEconomyClassSeat = BigDecimal.ZERO;
    public int getFirstClassSeat() {
        return firstClassSeat;
    }

    public void setFirstClassSeat(int firstClassSeat) {
        this.firstClassSeat = firstClassSeat;

    }

    public int getBusinessClassSeat() {
        return businessClassSeat;
    }

    public void setBusinessClassSeat(int businessClassSeat) {
        this.businessClassSeat = businessClassSeat;

    }

    public int getEconomyClassSeat() {
        return economyClassSeat;
    }

    public void setEconomyClassSeat(int economyClassSeat) {
        this.economyClassSeat = economyClassSeat;
    }
    public boolean isValid() {
        if (firstClassSeat > 0 && (priceClassSeat == null || priceClassSeat.compareTo(BigDecimal.ZERO) <= 0)) {
            validationErrors.add("If First Class seat count is provided, the price for First Class must also be provided and greater than 0.");
        }
        if (businessClassSeat > 0 && (priceBusinessClassSeat == null || priceBusinessClassSeat.compareTo(BigDecimal.ZERO) <= 0)) {
            validationErrors.add("If Business Class seat count is provided, the price for Business Class must also be provided and greater than 0.");
        }
        if (economyClassSeat > 0 && (priceEconomyClassSeat == null || priceEconomyClassSeat.compareTo(BigDecimal.ZERO) <= 0)) {
            validationErrors.add("If Economy Class seat count is provided, the price for Economy Class must also be provided and greater than 0.");
        }

        // Kiểm tra rằng ít nhất một loại ghế và giá đã được nhập
        boolean hasValidSeat = firstClassSeat > 0 || businessClassSeat > 0 || economyClassSeat > 0;
        boolean hasValidPrice = (priceClassSeat != null && priceClassSeat.compareTo(BigDecimal.ZERO) > 0)
                || (priceBusinessClassSeat != null && priceBusinessClassSeat.compareTo(BigDecimal.ZERO) > 0)
                || (priceEconomyClassSeat != null && priceEconomyClassSeat.compareTo(BigDecimal.ZERO) > 0);

        if (!hasValidSeat || !hasValidPrice) {
            validationErrors.add("At least one seat and its corresponding price must be provided.");
        }

        return validationErrors.isEmpty();
    }

}
