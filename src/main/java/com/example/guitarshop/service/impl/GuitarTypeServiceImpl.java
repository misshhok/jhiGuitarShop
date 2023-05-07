package com.example.guitarshop.service.impl;

import com.example.guitarshop.domain.GuitarType;
import com.example.guitarshop.repository.GuitarTypeRepository;
import com.example.guitarshop.service.GuitarTypeService;
import com.example.guitarshop.service.dto.GuitarTypeDTO;
import com.example.guitarshop.service.mapper.GuitarTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GuitarType}.
 */
@Service
@Transactional
public class GuitarTypeServiceImpl implements GuitarTypeService {

    private final Logger log = LoggerFactory.getLogger(GuitarTypeServiceImpl.class);

    private final GuitarTypeRepository guitarTypeRepository;

    private final GuitarTypeMapper guitarTypeMapper;

    public GuitarTypeServiceImpl(GuitarTypeRepository guitarTypeRepository, GuitarTypeMapper guitarTypeMapper) {
        this.guitarTypeRepository = guitarTypeRepository;
        this.guitarTypeMapper = guitarTypeMapper;
    }

    @Override
    public GuitarTypeDTO save(GuitarTypeDTO guitarTypeDTO) {
        log.debug("Request to save GuitarType : {}", guitarTypeDTO);
        GuitarType guitarType = guitarTypeMapper.toEntity(guitarTypeDTO);
        guitarType = guitarTypeRepository.save(guitarType);
        return guitarTypeMapper.toDto(guitarType);
    }

    @Override
    public GuitarTypeDTO update(GuitarTypeDTO guitarTypeDTO) {
        log.debug("Request to update GuitarType : {}", guitarTypeDTO);
        GuitarType guitarType = guitarTypeMapper.toEntity(guitarTypeDTO);
        guitarType = guitarTypeRepository.save(guitarType);
        return guitarTypeMapper.toDto(guitarType);
    }

    @Override
    public Optional<GuitarTypeDTO> partialUpdate(GuitarTypeDTO guitarTypeDTO) {
        log.debug("Request to partially update GuitarType : {}", guitarTypeDTO);

        return guitarTypeRepository
            .findById(guitarTypeDTO.getId())
            .map(existingGuitarType -> {
                guitarTypeMapper.partialUpdate(existingGuitarType, guitarTypeDTO);

                return existingGuitarType;
            })
            .map(guitarTypeRepository::save)
            .map(guitarTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GuitarTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GuitarTypes");
        return guitarTypeRepository.findAll(pageable).map(guitarTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuitarTypeDTO> findOne(Long id) {
        log.debug("Request to get GuitarType : {}", id);
        return guitarTypeRepository.findById(id).map(guitarTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GuitarType : {}", id);
        guitarTypeRepository.deleteById(id);
    }
}
