package com.example.guitarshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A GuitarOrder.
 */
@Entity
@Table(name = "guitar_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GuitarOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "total_price", precision = 21, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne
    private User orderOwner;

    @ManyToMany
    @JoinTable(
        name = "rel_guitar_order__guitars_in_order",
        joinColumns = @JoinColumn(name = "guitar_order_id"),
        inverseJoinColumns = @JoinColumn(name = "guitars_in_order_id")
    )
    @JsonIgnoreProperties(value = { "guitarType" }, allowSetters = true)
    private Set<Guitar> guitarsInOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GuitarOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public GuitarOrder totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getOrderOwner() {
        return this.orderOwner;
    }

    public void setOrderOwner(User user) {
        this.orderOwner = user;
    }

    public GuitarOrder orderOwner(User user) {
        this.setOrderOwner(user);
        return this;
    }

    public Set<Guitar> getGuitarsInOrders() {
        return this.guitarsInOrders;
    }

    public void setGuitarsInOrders(Set<Guitar> guitars) {
        this.guitarsInOrders = guitars;
    }

    public GuitarOrder guitarsInOrders(Set<Guitar> guitars) {
        this.setGuitarsInOrders(guitars);
        return this;
    }

    public GuitarOrder addGuitarsInOrder(Guitar guitar) {
        this.guitarsInOrders.add(guitar);
        return this;
    }

    public GuitarOrder removeGuitarsInOrder(Guitar guitar) {
        this.guitarsInOrders.remove(guitar);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuitarOrder)) {
            return false;
        }
        return id != null && id.equals(((GuitarOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GuitarOrder{" +
            "id=" + getId() +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
