package dk.pokedex.controller;

import dk.pokedex.model.Pokedex;
import dk.pokedex.repository.PokedexRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {
    PokedexRepository pokedexRepository;
    public MyController(PokedexRepository pokedexRepository){
        this.pokedexRepository = pokedexRepository;
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("pokedex", pokedexRepository.getAll());
        return "index";
    }
    //From anchor in index
    @GetMapping("/create")
    public String showCreate(){
        return "create";
    }
    //From form action - if you have autoincrement you don't need to set that specific value
    @PostMapping("/create")
    public String addPokemon(@RequestParam("pokedex_number") int pokedex_number,
                             @RequestParam("name-field") String name_field,
                             @RequestParam("speed") int speed,
                             @RequestParam("special_defence") int special_defence,
                             @RequestParam("special_attack") int special_attack,
                             @RequestParam("defence") int defence,
                             @RequestParam("attack") int attack,
                             @RequestParam("hp") int hp,
                             @RequestParam("primary_type") String primary_type,
                             @RequestParam("secondary_type") String secondary_type){
        Pokedex newPokedex = new Pokedex();
        newPokedex.setPokedex_number(pokedex_number);
        newPokedex.setName(name_field);
        newPokedex.setSpeed(speed);
        newPokedex.setSpecial_defence(special_defence);
        newPokedex.setSpecial_attack(special_attack);
        newPokedex.setDefence(defence);
        newPokedex.setAttack(attack);
        newPokedex.setHp(hp);
        newPokedex.setPrimary_type(primary_type);
        newPokedex.setSecondary_type(secondary_type);

        pokedexRepository.addPokemon(newPokedex);

        return "redirect:/";
    }

    @GetMapping("/update/{pokedex_number}")
    public String showUpdate(@PathVariable("pokedex_number") int update_pokedex_number, Model model){
        //Finding produkt with pokedex_number = update_pokedex_number
        Pokedex pokedexUpdate = pokedexRepository.findPokemonByNumber(update_pokedex_number);

        //Add a pokemon to the viewmodel so it can be used by Thymeleaf
        model.addAttribute("pokedex", pokedexUpdate);

        //Tell spring which html page has to be shown
        return "update";
    }
    @PostMapping("/update")
    public String updatePokemon(@RequestParam("pokedex_number")int update_pokedex_number,
                                @RequestParam("name-field") String update_name_field,
                                @RequestParam("speed") int update_speed,
                                @RequestParam("special_defence") int update_special_defence,
                                @RequestParam("special_attack") int update_special_attack,
                                @RequestParam("defence") int update_defence,
                                @RequestParam("attack") int update_attack,
                                @RequestParam("hp") int update_hp,
                                @RequestParam("primary_type") String update_primary_type,
                                @RequestParam("secondary_type") String update_secondary_type){
        Pokedex updatePokedex = new Pokedex(update_pokedex_number, update_name_field, update_speed, update_special_defence,
                                            update_special_attack, update_defence, update_attack, update_hp,
                                            update_primary_type, update_secondary_type);

        pokedexRepository.updatePokedex(updatePokedex);

        return "redirect:/";
    }
    @GetMapping("/delete/{pokedex_number}")
    public String deletePokemon(@PathVariable("pokedex_number") int pokedex_number){
        //Call respository to delete from it
        pokedexRepository.deletePokemonNumber(pokedex_number);

        //Redirect to index page
        return "redirect:/";
    }
}
