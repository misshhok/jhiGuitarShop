package com.example.guitarshop.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.example.guitarshop.domain.GuitarOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GuitarOrderDTO implements Serializable {

    private Long id;

    private BigDecimal totalPrice;

    private UserDTO orderOwner;

    private Set<GuitarDTO> guitarsInOrders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public UserDTO getOrderOwner() {
        return orderOwner;
    }

    public void setOrderOwner(UserDTO orderOwner) {
        this.orderOwner = orderOwner;
    }

    public Set<GuitarDTO> getGuitarsInOrders() {
        return guitarsInOrders;
    }

    public void setGuitarsInOrders(Set<GuitarDTO> guitarsInOrders) {
        this.guitarsInOrders = guitarsInOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuitarOrderDTO)) {
            return false;
        }

        GuitarOrderDTO guitarOrderDTO = (GuitarOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, guitarOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GuitarOrderDTO{" +
            "id=" + getId() +
            ", totalPrice=" + getTotalPrice() +
            ", orderOwner=" + getOrderOwner() +
            ", guitarsInOrders=" + getGuitarsInOrders() +
            "}";
    }
}
