package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PlayersRepository extends JpaRepository<Player, Integer> {
    @Query
    Long countAllByTitleContaining(String title);
    @Query
    Long countAllByBanned(Boolean banned);

    @Query
    Long countAllByRaceAndProfessionAndBirthdayBefore(Race race, Profession profession, Date bef);
    @Query
    Long countAllByRaceAndProfessionAndExperienceBefore(Race race, Profession profession, Integer maxExp);
    @Query
    Long countAllByRaceAndProfessionAndBanned(Race race, Profession profession, Boolean banned);
    @Query
    Long countAllByLevelAfterAndExperienceAfter(Integer level, Integer exp);

    @Query
    Long countAllByNameContainingAndBirthdayAfterAndLevelBefore(String name, Date after, Integer max);
    @Query
    List<Player> findAllByBannedAndLevelBetween(Boolean ban, Integer min, Integer max,Pageable pageable);

    @Query
    List<Player> findAllByBannedAndLevelBefore(Boolean ban,Integer max, Pageable pageable);

    @Query
    List<Player> findAllByRaceAndProfessionAndBirthdayBetween(Race race, Profession profession,  Date af, Date bef, Pageable pageable);


    @Query
    List<Player> findAllByTitleContaining(String title, Pageable pageable);
    @Query
    List<Player> findAllByNameContaining(String name, Pageable pageable);
    @Query
    List<Player> findAllByRaceAndProfessionAndExperienceBetween(Race race, Profession profession, Integer min, Integer max, Pageable pg);

    @Query
    List<Player> findAllByExperienceBetweenAndBirthdayBetween(Integer min, Integer max, Date af, Date bef, Pageable pageable);
}
