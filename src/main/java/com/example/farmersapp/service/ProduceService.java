package com.example.farmersapp.service;

import com.example.farmersapp.model.Produce;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduceService {

    private final ProduceRepository produceRepository;

    public ProduceService(ProduceRepository produceRepository) {
        this.produceRepository = produceRepository;
    }

    public Produce addProduce(Produce produce) {
        return produceRepository.save(produce);
    }

    public List<Produce> getAllProduce() {
        return produceRepository.findAll();
    }

}

