package com.pluralsight.conferencedemo.controllers;

import com.pluralsight.conferencedemo.models.Speaker;
import com.pluralsight.conferencedemo.models.Speaker;
import com.pluralsight.conferencedemo.repositories.SpeakerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakersController  {

    @Autowired
    private SpeakerRepository speakerRepository;

    @GetMapping
    public List<Speaker> list() {
        return speakerRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Speaker get(@PathVariable Long id) {
        return speakerRepository.getOne(id);
    }

    @PostMapping
    public Speaker create(@RequestBody final Speaker Speaker) {
        return speakerRepository.saveAndFlush(Speaker);
    }

    // Spring only supports postmapping and get mapping as annotations
    // All other verbs will have to be specified in the method
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        // Have to deal with cascades here - will only allow you to delete Speakers without any children
        speakerRepository.deleteById(id);
    }

    // Updates can choose between PUT and Patch. Normally;
    // -- PUT will replace all attributes in the object
    // -- Patch will allow updating of just a few
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Speaker update(@PathVariable Long id, @RequestBody Speaker Speaker) {
        // TODO: Add validation to check all attributes are passed, return 400 bad payload if not
        Speaker existingSpeaker = speakerRepository.getOne(id);
        BeanUtils.copyProperties(Speaker, existingSpeaker, "session_id");
        return speakerRepository.saveAndFlush(existingSpeaker);
    }
}
