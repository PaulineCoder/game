package com.game.entity;


import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(name = "name")
    private String name;

   @Column(name = "title")
    private String title;
   @Enumerated(EnumType.STRING)
   @Column(name = "race")
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(name = "profession")
    private Profession profession;

   @Column(name = "experience")
    private Integer experience;

   @Column(name = "level")
    private Integer level;

    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;

    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "banned")
    private Boolean banned;

    public Player(){}

    public Player(Long id, String name, String title, Race race, Profession profession, Date birthday,
           Boolean banned, Integer experience, Integer level, Integer untilNextLevel){
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUnitNextLevel() {
        return untilNextLevel;
    }

    public void setUnitNextLevel(Integer unitNextLevel) {
        this.untilNextLevel = unitNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Long birthday) {
        this.birthday = new Date(birthday);
    }
     public void  setBirthday(Date birthday){
        this.birthday =birthday;
     }
    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }


}
