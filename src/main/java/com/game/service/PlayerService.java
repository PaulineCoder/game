package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayersRepository;
import com.game.util.PlayerBadRequest;
import com.game.util.PlayerNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;


@Service
@Transactional()
public class PlayerService {
    private final PlayersRepository playersRepository;

    @Autowired
    public PlayerService(PlayersRepository playersRepository){
        this.playersRepository = playersRepository;
    }

  public List<Player> findAll(Integer pageNumber, Integer pageSize, PlayerOrder order) {
      return playersRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))).getContent();
  }
public List<Player> findAllByName(Integer pageNumber, Integer pageSize, PlayerOrder order, String name){
    return playersRepository.findAllByNameContaining(name, (PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))));
}
    public List<Player> findAllByTitle(Integer pageNumber, Integer pageSize, PlayerOrder order, String title){
         return playersRepository.findAllByTitleContaining(title, (PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))));
    }
    public List<Player> findAllByBL(Integer pageNumber, Integer pageSize,PlayerOrder order, Boolean banned, Integer min, Integer max ){
        return playersRepository.findAllByBannedAndLevelBetween(banned, min, max, (PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))));
    }
    public List<Player> findAllByBannedAndLevel(Integer pageNumber, Integer pageSize,PlayerOrder order, Boolean banned, Integer max ){
        return playersRepository.findAllByBannedAndLevelBefore(banned, max, (PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))));
    }
    public List<Player> findAllByPRE(Integer pageNumber, Integer pageSize, PlayerOrder order, Race race, Profession profession,Integer min, Integer max){
        return playersRepository.findAllByRaceAndProfessionAndExperienceBetween(race,profession,min,max, (PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))));
    }
    public List<Player> findAllByRPAF(Integer pageNumber, Integer pageSize, PlayerOrder order, Race race, Profession profession, Long after, Long before){
        return playersRepository.findAllByRaceAndProfessionAndBirthdayBetween(race,profession,new Date (after),new Date(before), PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName())));
    }
    public List<Player> findAllByEAB(Integer pageNumber, Integer pageSize, PlayerOrder order, Integer minExperience,Integer maxExperience,Long after,Long before){
       return playersRepository.findAllByExperienceBetweenAndBirthdayBetween(minExperience,maxExperience,new Date(after),new Date(before), (PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))));
        }
    public Player findOne(long id){
        Player foundPlayer = null;
        boolean exist = false;
       for (Player player : playersRepository.findAll()){
           if(player.getId() == id){
               foundPlayer = player;
               exist = true;
           }
       }
       if(id == 0){
           throw new PlayerBadRequest();
       }
       if(!exist ){
        throw new PlayerNotFound();}
       return foundPlayer;
    }

    public Player save(Player player){
        if(player.getBanned() == null & player.getName() == null & player.getBirthday() == null &
                player.getProfession() == null & player.getRace() == null & player.getTitle() == null & player.getExperience() == null){
            throw new PlayerBadRequest();
        }
        if(player.getTitle().toCharArray().length > 30){
            throw new PlayerBadRequest();
        }
        if (player.getBirthday().getTime() < 0){
            throw new PlayerBadRequest();
        }
        if(player.getBanned() == null){
            player.setBanned(false);
        }
        if(player.getExperience() < 0 || player.getExperience() > 10000000){
            throw new PlayerBadRequest();
        }
        int exp = player.getExperience();
        int level = (int) ((Math. sqrt(2500 + 200 * exp) - 50)/100);
        player.setLevel(level);
        int untilNextLevel = 50 *(level + 1) * (level + 2) - exp;
        player.setUnitNextLevel(untilNextLevel);
        return playersRepository.save(player);
    }

    public ResponseEntity<Player> update(Long id, Player player){
        Player player1 = findOne(id);
        if(player1 == null){
            throw  new PlayerNotFound();}

        if(player.getTitle() != null){
                if(player.getTitle().toCharArray().length > 30){
                    throw new PlayerBadRequest();
                }
                player1.setTitle(player.getTitle());
            }
        if(player.getBirthday() != null ){
            if (player.getBirthday().getTime() < 0){
                throw new PlayerBadRequest();
            }
            player1.setBirthday(player.getBirthday());
        }
        if (player.getBanned() != null){
            player1.setBanned(player.getBanned());
        }
        if(player.getRace() != null){
            player1.setRace(player.getRace());
        }
        if(player.getProfession() != null){
            player1.setProfession(player.getProfession());
        }
        if(player.getName() != null){
            player1.setName(player.getName());
        }
     if(player.getExperience() != null){
          if(player.getExperience() < 0 || player.getExperience() > 10000000){
                throw new PlayerBadRequest();
            }
           player1.setExperience(player.getExperience());
            int exp = player1.getExperience();
            int level = (int) ((Math. sqrt(2500 + 200 * exp) - 50)/100);
            player1.setLevel(level);
            int untilNextLevel = 50 *(level + 1) * (level + 2) - exp;
            player1.setUnitNextLevel(untilNextLevel);
        }
       playersRepository.save(player1);
     return new ResponseEntity<>(player1, HttpStatus.OK);
    }

    public long count(){
        return playersRepository.count();
    }
    public long countAllByTitle(String title){
        return playersRepository.countAllByTitleContaining(title);
    }

    public long countAllByBanned(Boolean banned){
        return playersRepository.countAllByBanned(banned);
    }

    public long countAllByLE(Integer minLevel, Integer minExp){
        return playersRepository.countAllByLevelAfterAndExperienceAfter(minLevel, minExp);
    }
    public long countAllByNAL(String name, Long after, Integer max){
        return playersRepository.countAllByNameContainingAndBirthdayAfterAndLevelBefore(name,new Date(after),max);
    }
    public long countAllByRPB(Race race, Profession profession, Boolean banned){
        return playersRepository.countAllByRaceAndProfessionAndBanned(race, profession, banned);
    }
    public long countAllByRPE(Race race, Profession profession, Integer maxExp){
        return playersRepository.countAllByRaceAndProfessionAndExperienceBefore(race, profession, maxExp);
    }
    public long countAllByRPBefore(Race race, Profession profession, Long before){
        return playersRepository.countAllByRaceAndProfessionAndBirthdayBefore(race, profession, new Date(before));
    }

    public void delete(long id){
        playersRepository.delete(findOne(id));
    }
}
