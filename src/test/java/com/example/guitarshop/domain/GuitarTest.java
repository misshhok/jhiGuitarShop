package com.example.guitarshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.guitarshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GuitarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Guitar.class);
        Guitar guitar1 = new Guitar();
        guitar1.setId(1L);
        Guitar guitar2 = new Guitar();
        guitar2.setId(guitar1.getId());
        assertThat(guitar1).isEqualTo(guitar2);
        guitar2.setId(2L);
        assertThat(guitar1).isNotEqualTo(guitar2);
        guitar1.setId(null);
        assertThat(guitar1).isNotEqualTo(guitar2);
    }
}
