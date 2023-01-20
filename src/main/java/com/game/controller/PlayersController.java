package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.util.PlayerErrorResponse;
import com.game.util.PlayerBadRequest;
import com.game.util.PlayerNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("rest/players")
public class PlayersController {
    private final PlayerService playerService;


    @Autowired
    public PlayersController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping()
    public List<Player> getPlayers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                   @RequestParam(value = "pageSize",  required = false) Integer pageSize,
                                   @RequestParam(value = "order", required = false) PlayerOrder order,
                                   @RequestParam (required = false) String name, @RequestParam (required = false) String title, @RequestParam (required = false) Race race,
                                   @RequestParam (required = false) Profession profession, @RequestParam (required = false) Long after, @RequestParam (required = false) Long before,
                                   @RequestParam (required = false) Integer minExperience, @RequestParam (required = false) Integer maxExperience,
                                   @RequestParam (required = false) Boolean banned, @RequestParam (required = false) Integer maxLevel,
                                   @RequestParam (required = false) Integer minLevel

    ){
        if(pageNumber == null){
            pageNumber = 0;
        }
        if(pageSize == null){
            pageSize = 3;
        }
        if(order == null){
            order = PlayerOrder.ID;
        }
        if(name != null){
           return playerService.findAllByName(pageNumber, pageSize, order, name);
        }
        if(title != null){
            return playerService.findAllByTitle(pageNumber,pageSize, order, title);
        }
        if(race != null & profession != null & minExperience != null & maxExperience != null){
            return playerService.findAllByPRE(pageNumber,pageSize, order, race, profession, minExperience, maxExperience);
        }
        if(minExperience != null & maxExperience != null & after != null & before != null){
            return  playerService.findAllByEAB(pageNumber,pageSize,order,minExperience,maxExperience,after,before);
        }
        if(minLevel != null & banned!= null & maxLevel != null){
            return playerService.findAllByBL(pageNumber,pageSize,order,banned,minLevel, maxLevel);
        }
        if(banned != null & maxLevel != null){
            return playerService.findAllByBannedAndLevel(pageNumber,pageSize,order, banned, maxLevel);
        }
        if(race != null & profession != null & after != null & before != null){
            return playerService.findAllByRPAF(pageNumber,pageSize,order,race, profession,after,before);
        }
        else{
        return playerService.findAll(pageNumber,pageSize,order);}
    }

    @GetMapping("/count")
    public long countPlayers(  @RequestParam (required = false) String name, @RequestParam (required = false) String title, @RequestParam (required = false) Race race,
                               @RequestParam (required = false) Profession profession, @RequestParam (required = false) Long after, @RequestParam (required = false) Long before,
                               @RequestParam (required = false) Integer minExperience, @RequestParam (required = false) Integer maxExperience,
                               @RequestParam (required = false) Boolean banned, @RequestParam (required = false) Integer maxLevel,
                               @RequestParam (required = false) Integer minLevel){
        if(title != null){
            return playerService.countAllByTitle(title);
        }
        if(banned != null){
            if(race!=null & profession!=null) {
                return  playerService.countAllByRPB(race,profession,banned);
            }
            return playerService.countAllByBanned(banned);}
        if(race != null & profession != null & maxExperience != null){
            return playerService.countAllByRPE(race, profession, maxExperience);
        }
        if(race !=null & profession != null & before != null){
            return playerService.countAllByRPBefore(race,profession,before);
        }
        if(minLevel != null & minExperience != null){
            return playerService.countAllByLE(minLevel, minExperience);
        }
        if(name!=null&after!=null&maxLevel!=null){
            return playerService.countAllByNAL(name,after,maxLevel);
        }
        return playerService.count();
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable("id") long id) {
            return playerService.findOne(id);

    }


    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Player create(@RequestBody Player player)
    {
        return  playerService.save(player);
    }

    @PostMapping(value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Player> update(@PathVariable Long id, @RequestBody Player player){
        if(id == 0 || id < 0){
            throw new PlayerBadRequest();
        }

        Player player1 = getPlayer(id);
        if(player1 == null){
            throw new PlayerNotFound();}
      if(player.getName() == null&
       player.getProfession() == null&
       player.getBanned() == null &
      player.getRace() == null &
      player.getExperience() == null &
      player.getBirthday() ==null &
      player.getLevel() == null &
      player.getId() == null &
      player.getTitle() == null &
      player.getUnitNextLevel() == null){
        return new ResponseEntity<>(player1,HttpStatus.OK);
      }
      else{
     return playerService.update(id,player);}
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable long id) {
        playerService.delete(id);
    }

    @ExceptionHandler
    private ResponseEntity<PlayerErrorResponse> handleException(PlayerNotFound e){
        PlayerErrorResponse playerErrorResponse = new PlayerErrorResponse("404");
        return new ResponseEntity<>(playerErrorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<PlayerErrorResponse> handleException(PlayerBadRequest e){
        PlayerErrorResponse playerErrorResponse = new PlayerErrorResponse("400");
        return new ResponseEntity<>(playerErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
