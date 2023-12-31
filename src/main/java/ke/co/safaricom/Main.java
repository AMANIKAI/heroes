package ke.co.safaricom;

import ke.co.safaricom.dao.HeroDao;
import ke.co.safaricom.dao.SquadDao;
import ke.co.safaricom.models.Hero;
import ke.co.safaricom.models.Squad;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;

import java.util.Map;

import static spark.Spark.*;

public abstract class Main {

    public static void main(String[] args) {

        staticFileLocation("/public");
        HandlebarsTemplateEngine views = new HandlebarsTemplateEngine();

        //LANDING PAGE
        get("/", (req,res) -> new ModelAndView(new HashMap<>(),"landing-page.hbs"), views );

        //HOME PAGE
        get("/home", (req,res) -> {

            Map<String, Object> combinedList = new HashMap<>();
            combinedList.put("heroObject", HeroDao.getAllHeroes());
            combinedList.put("squadObject", SquadDao.getAllSquads());
            return new ModelAndView(combinedList, "home.hbs");

        },views);

        //TO ADD A HERO FORM PAGE
        get("/add-hero", (req,res) -> new ModelAndView(new HashMap<>(),"add-hero.hbs"), views );

        //TO UPDATE HERO DETAILS TO DATABASE
        post("/add-hero", (req,res)-> {

            String hero = req.queryParams("hero");
            Integer age = Integer.parseInt(req.queryParams("age"));
            String power = req.queryParams("power");
            Integer power_score = Integer.parseInt(req.queryParams("power_score"));
            String weakness = req.queryParams("weakness");
            Integer weakness_score = Integer.parseInt(req.queryParams("weakness_score"));
            String squad = "";

            Hero newHero = new Hero(hero.toUpperCase(),age,power,power_score,weakness,weakness_score,squad);
            HeroDao.addHero(newHero);
            res.redirect("/home");
            return null ;
        });

        //TO ADD A SQUAD FORM
        get("/add-squad", (req,res) -> new ModelAndView(new HashMap<>(),"add-squad.hbs"), views );

        //TO SEND SQUAD DETAILS TO DATABASE
        post("/add-squad", (req,res)-> {

            String squad = req.queryParams("squad");
            String cause = req.queryParams("cause");
            Integer size = Integer.parseInt(req.queryParams("size"));
            Squad newSquad = new Squad(squad,cause,size);
            SquadDao.addSquad(newSquad);
            res.redirect("/home");
            return null;

        });

        //ASSIGNING A HERO TO SQUAD ASSIGNMENT FORM
        get("/assign-squad/:squad", (req,res) -> {

            String squad =  req.params("squad");
            Map<String, Object> combinedList = new HashMap<>();

            if(HeroDao.heroCount(squad) < SquadDao.maxSize(squad)) {
                combinedList.put("querySquad", squad);
                combinedList.put("heroObject", HeroDao.membership(squad));
            } else res.redirect("/full-squad");

            return new ModelAndView(combinedList, "assign-squad.hbs");
        },views);

        //ASSIGNING A HER0 TO AN EXISTING SQUAD
        post("/assign-squad/:squad", (req,res) -> {

            String hero = req.queryParams("hero");
            String squad = req.queryParams("squad");
            HeroDao.updateMembership(hero, squad);
            res.redirect("/home");
            return null;

        },views);

        //ADDING A HERO FROM THE HERO LIST
        get("/delete-hero/:hero", (req,res)-> {

            String name = req.params(":hero");
            HeroDao.deleteHero(name);
            res.redirect("/home");
            return null;

        },views);

        //DELETING A SQUAD FROM THE SQUAD LIST
        get("/delete-squad/:squad", (req,res)-> {

            String name = req.params(":squad");
            SquadDao.deleteSquad(name);
            res.redirect("/home");
            return null;

        },views);

        //TO CREATE A PAGE WITH A LIST OF ALL HEROES AND SQUADS
        get("/all", (req,res) -> {

            Map<String, Object> combinedList = new HashMap<>();
            combinedList.put("heroObject", HeroDao.getAllHeroes());
            combinedList.put("squadObject", SquadDao.getAllSquads());
            return new ModelAndView(combinedList, "all.hbs");

        },views);

        //DISPLAYING FULL SQUAD PAGE
        get("/full-squad", (req,res) -> new ModelAndView(new HashMap<>(),"full-squad.hbs"), views );

    }
}