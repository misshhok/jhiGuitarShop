package com.example.guitarshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GuitarMapperTest {

    private GuitarMapper guitarMapper;

    @BeforeEach
    public void setUp() {
        guitarMapper = new GuitarMapperImpl();
    }
}
