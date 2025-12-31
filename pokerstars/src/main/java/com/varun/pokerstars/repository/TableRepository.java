package com.varun.pokerstars.repository;

import com.varun.pokerstars.models.PokerTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<PokerTable, String> {
}
