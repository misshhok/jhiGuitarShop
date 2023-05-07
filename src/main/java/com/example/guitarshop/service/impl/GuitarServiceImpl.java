package com.example.guitarshop.service.impl;

import com.example.guitarshop.domain.Guitar;
import com.example.guitarshop.repository.GuitarRepository;
import com.example.guitarshop.service.GuitarService;
import com.example.guitarshop.service.dto.GuitarDTO;
import com.example.guitarshop.service.mapper.GuitarMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Guitar}.
 */
@Service
@Transactional
public class GuitarServiceImpl implements GuitarService {

    private final Logger log = LoggerFactory.getLogger(GuitarServiceImpl.class);

    private final GuitarRepository guitarRepository;

    private final GuitarMapper guitarMapper;

    public GuitarServiceImpl(GuitarRepository guitarRepository, GuitarMapper guitarMapper) {
        this.guitarRepository = guitarRepository;
        this.guitarMapper = guitarMapper;
    }

    @Override
    public GuitarDTO save(GuitarDTO guitarDTO) {
        log.debug("Request to save Guitar : {}", guitarDTO);
        Guitar guitar = guitarMapper.toEntity(guitarDTO);
        guitar = guitarRepository.save(guitar);
        return guitarMapper.toDto(guitar);
    }

    @Override
    public GuitarDTO update(GuitarDTO guitarDTO) {
        log.debug("Request to update Guitar : {}", guitarDTO);
        Guitar guitar = guitarMapper.toEntity(guitarDTO);
        guitar = guitarRepository.save(guitar);
        return guitarMapper.toDto(guitar);
    }

    @Override
    public Optional<GuitarDTO> partialUpdate(GuitarDTO guitarDTO) {
        log.debug("Request to partially update Guitar : {}", guitarDTO);

        return guitarRepository
            .findById(guitarDTO.getId())
            .map(existingGuitar -> {
                guitarMapper.partialUpdate(existingGuitar, guitarDTO);

                return existingGuitar;
            })
            .map(guitarRepository::save)
            .map(guitarMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GuitarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Guitars");
        return guitarRepository.findAll(pageable).map(guitarMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuitarDTO> findOne(Long id) {
        log.debug("Request to get Guitar : {}", id);
        return guitarRepository.findById(id).map(guitarMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Guitar : {}", id);
        guitarRepository.deleteById(id);
    }
}
