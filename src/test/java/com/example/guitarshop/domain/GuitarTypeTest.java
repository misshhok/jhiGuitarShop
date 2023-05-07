package com.example.guitarshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.guitarshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GuitarTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuitarType.class);
        GuitarType guitarType1 = new GuitarType();
        guitarType1.setId(1L);
        GuitarType guitarType2 = new GuitarType();
        guitarType2.setId(guitarType1.getId());
        assertThat(guitarType1).isEqualTo(guitarType2);
        guitarType2.setId(2L);
        assertThat(guitarType1).isNotEqualTo(guitarType2);
        guitarType1.setId(null);
        assertThat(guitarType1).isNotEqualTo(guitarType2);
    }
}
