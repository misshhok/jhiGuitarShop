package com.example.guitarshop.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.example.guitarshop.domain.Guitar} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GuitarDTO implements Serializable {

    private Long id;

    private String title;

    private BigDecimal price;

    private GuitarTypeDTO guitarType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public GuitarTypeDTO getGuitarType() {
        return guitarType;
    }

    public void setGuitarType(GuitarTypeDTO guitarType) {
        this.guitarType = guitarType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuitarDTO)) {
            return false;
        }

        GuitarDTO guitarDTO = (GuitarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, guitarDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GuitarDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", guitarType=" + getGuitarType() +
            "}";
    }
}
