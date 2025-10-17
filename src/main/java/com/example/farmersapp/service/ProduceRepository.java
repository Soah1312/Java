package com.example.farmersapp.service;

import com.example.farmersapp.model.Produce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProduceRepository extends JpaRepository<Produce, UUID> {

}
