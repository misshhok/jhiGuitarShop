package com.example.guitarshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.guitarshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GuitarOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuitarOrderDTO.class);
        GuitarOrderDTO guitarOrderDTO1 = new GuitarOrderDTO();
        guitarOrderDTO1.setId(1L);
        GuitarOrderDTO guitarOrderDTO2 = new GuitarOrderDTO();
        assertThat(guitarOrderDTO1).isNotEqualTo(guitarOrderDTO2);
        guitarOrderDTO2.setId(guitarOrderDTO1.getId());
        assertThat(guitarOrderDTO1).isEqualTo(guitarOrderDTO2);
        guitarOrderDTO2.setId(2L);
        assertThat(guitarOrderDTO1).isNotEqualTo(guitarOrderDTO2);
        guitarOrderDTO1.setId(null);
        assertThat(guitarOrderDTO1).isNotEqualTo(guitarOrderDTO2);
    }
}
