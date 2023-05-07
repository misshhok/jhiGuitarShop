package com.example.guitarshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.guitarshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GuitarDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuitarDTO.class);
        GuitarDTO guitarDTO1 = new GuitarDTO();
        guitarDTO1.setId(1L);
        GuitarDTO guitarDTO2 = new GuitarDTO();
        assertThat(guitarDTO1).isNotEqualTo(guitarDTO2);
        guitarDTO2.setId(guitarDTO1.getId());
        assertThat(guitarDTO1).isEqualTo(guitarDTO2);
        guitarDTO2.setId(2L);
        assertThat(guitarDTO1).isNotEqualTo(guitarDTO2);
        guitarDTO1.setId(null);
        assertThat(guitarDTO1).isNotEqualTo(guitarDTO2);
    }
}
