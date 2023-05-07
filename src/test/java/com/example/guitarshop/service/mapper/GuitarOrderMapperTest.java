package com.example.guitarshop.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GuitarOrderMapperTest {

    private GuitarOrderMapper guitarOrderMapper;

    @BeforeEach
    public void setUp() {
        guitarOrderMapper = new GuitarOrderMapperImpl();
    }
}
