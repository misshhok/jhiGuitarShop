package com.example.guitarshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GuitarTypeMapperTest {

    private GuitarTypeMapper guitarTypeMapper;

    @BeforeEach
    public void setUp() {
        guitarTypeMapper = new GuitarTypeMapperImpl();
    }
}
