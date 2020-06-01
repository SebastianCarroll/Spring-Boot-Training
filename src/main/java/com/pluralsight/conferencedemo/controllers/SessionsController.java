package com.pluralsight.conferencedemo.controllers;

import com.pluralsight.conferencedemo.models.Session;
import com.pluralsight.conferencedemo.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {
    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping
    public List<Session> list() {
        return sessionRepository.findAll();
    }

    @GetMapping("{id}")
    public Session get(@PathVariable Long id) {
        return sessionRepository.getOne(id);
    }

    @PostMapping
    public Session create(@RequestBody final Session session) {
        return sessionRepository.saveAndFlush(session);
    }

    // Spring only supports postmapping and get mapping as annotations
    // All other verbs will have to be specified in the method
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        // Have to deal with cascades here - will only allow you to delete sessions without any children
        sessionRepository.deleteById(id);
    }

    // Updates can choose between PUT and Patch. Normally;
    // -- PUT will replace all attributes in the object
    // -- Patch will allow updating of just a few
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Session update(@PathVariable Long id, @RequestBody Session session) {
        // TODO: Add validation to check all attributes are passed, return 400 bad payload if not
        Session existingSession = sessionRepository.getOne(id);
        BeanUtils.copyProperties(session, existingSession, "session_id");
        return sessionRepository.saveAndFlush(existingSession);
    }
}
