package com.example.guitarshop.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.guitarshop.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GuitarTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GuitarTypeDTO.class);
        GuitarTypeDTO guitarTypeDTO1 = new GuitarTypeDTO();
        guitarTypeDTO1.setId(1L);
        GuitarTypeDTO guitarTypeDTO2 = new GuitarTypeDTO();
        assertThat(guitarTypeDTO1).isNotEqualTo(guitarTypeDTO2);
        guitarTypeDTO2.setId(guitarTypeDTO1.getId());
        assertThat(guitarTypeDTO1).isEqualTo(guitarTypeDTO2);
        guitarTypeDTO2.setId(2L);
        assertThat(guitarTypeDTO1).isNotEqualTo(guitarTypeDTO2);
        guitarTypeDTO1.setId(null);
        assertThat(guitarTypeDTO1).isNotEqualTo(guitarTypeDTO2);
    }
}
