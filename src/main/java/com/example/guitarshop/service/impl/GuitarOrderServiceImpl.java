package com.example.guitarshop.service.impl;

import com.example.guitarshop.domain.GuitarOrder;
import com.example.guitarshop.repository.GuitarOrderRepository;
import com.example.guitarshop.service.GuitarOrderService;
import com.example.guitarshop.service.dto.GuitarOrderDTO;
import com.example.guitarshop.service.mapper.GuitarOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GuitarOrder}.
 */
@Service
@Transactional
public class GuitarOrderServiceImpl implements GuitarOrderService {

    private final Logger log = LoggerFactory.getLogger(GuitarOrderServiceImpl.class);

    private final GuitarOrderRepository guitarOrderRepository;

    private final GuitarOrderMapper guitarOrderMapper;

    public GuitarOrderServiceImpl(GuitarOrderRepository guitarOrderRepository, GuitarOrderMapper guitarOrderMapper) {
        this.guitarOrderRepository = guitarOrderRepository;
        this.guitarOrderMapper = guitarOrderMapper;
    }

    @Override
    public GuitarOrderDTO save(GuitarOrderDTO guitarOrderDTO) {
        log.debug("Request to save GuitarOrder : {}", guitarOrderDTO);
        GuitarOrder guitarOrder = guitarOrderMapper.toEntity(guitarOrderDTO);
        guitarOrder = guitarOrderRepository.save(guitarOrder);
        return guitarOrderMapper.toDto(guitarOrder);
    }

    @Override
    public GuitarOrderDTO update(GuitarOrderDTO guitarOrderDTO) {
        log.debug("Request to update GuitarOrder : {}", guitarOrderDTO);
        GuitarOrder guitarOrder = guitarOrderMapper.toEntity(guitarOrderDTO);
        guitarOrder = guitarOrderRepository.save(guitarOrder);
        return guitarOrderMapper.toDto(guitarOrder);
    }

    @Override
    public Optional<GuitarOrderDTO> partialUpdate(GuitarOrderDTO guitarOrderDTO) {
        log.debug("Request to partially update GuitarOrder : {}", guitarOrderDTO);

        return guitarOrderRepository
            .findById(guitarOrderDTO.getId())
            .map(existingGuitarOrder -> {
                guitarOrderMapper.partialUpdate(existingGuitarOrder, guitarOrderDTO);

                return existingGuitarOrder;
            })
            .map(guitarOrderRepository::save)
            .map(guitarOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GuitarOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GuitarOrders");
        return guitarOrderRepository.findAll(pageable).map(guitarOrderMapper::toDto);
    }

    public Page<GuitarOrderDTO> findAllWithEagerRelationships(Pageable pageable) {
        return guitarOrderRepository.findAllWithEagerRelationships(pageable).map(guitarOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuitarOrderDTO> findOne(Long id) {
        log.debug("Request to get GuitarOrder : {}", id);
        return guitarOrderRepository.findOneWithEagerRelationships(id).map(guitarOrderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GuitarOrder : {}", id);
        guitarOrderRepository.deleteById(id);
    }
}
