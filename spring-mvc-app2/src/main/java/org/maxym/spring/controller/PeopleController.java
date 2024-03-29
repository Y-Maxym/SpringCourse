package org.maxym.spring.controller;

import jakarta.validation.Valid;
import org.maxym.spring.dao.PersonDAO;
import org.maxym.spring.model.Person;
import org.maxym.spring.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDAO personDAO;
    private final PersonValidator personValidator;
    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String getPeople(Model model) {
        model.addAttribute("people", personDAO.getPeople());
        return "people/people";
    }

    @GetMapping("/{id}")
    public String getPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.getPerson(id));
        return "people/person";
    }

    @GetMapping("/new")
    public String getCreateForm(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping
    public String createNewPerson(@ModelAttribute("person") @Valid Person person,
                                  BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.getPerson(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/edit";

        personDAO.update(person, id);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
