package com.vtnq.web.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "seats")
public class Seat {
    public Seat() {
    }

    public Seat(String index, String type, Flight idFlight, BigDecimal price,Integer status) {
        this.index = index;
        this.type = type;
        this.idFlight = idFlight;
        this.price = price;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "`index`", nullable = false, length = 50)
    private String index;

    @Size(max = 100)
    @Column(name = "type", length = 100)
    private String type;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_flight", nullable = false)
    private Flight idFlight;

    @Column(name = "price", precision = 10)
    private BigDecimal price;

    @Column(name = "status")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Flight getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(Flight idFlight) {
        this.idFlight = idFlight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}