package com.example.guitarshop.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.guitarshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GuitarOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuitarOrder.class);
        GuitarOrder guitarOrder1 = new GuitarOrder();
        guitarOrder1.setId(1L);
        GuitarOrder guitarOrder2 = new GuitarOrder();
        guitarOrder2.setId(guitarOrder1.getId());
        assertThat(guitarOrder1).isEqualTo(guitarOrder2);
        guitarOrder2.setId(2L);
        assertThat(guitarOrder1).isNotEqualTo(guitarOrder2);
        guitarOrder1.setId(null);
        assertThat(guitarOrder1).isNotEqualTo(guitarOrder2);
    }
}
