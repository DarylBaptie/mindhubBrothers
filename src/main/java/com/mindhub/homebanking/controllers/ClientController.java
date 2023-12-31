package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.util.stream.Collectors.toList;

@org.springframework.web.bind.annotation.RestController

public class ClientController {

    @Autowired
    private ClientRepository repo;

    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return repo.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());

        }

    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){

    Client client = repo.findById(id).orElse(null);

    ClientDTO clientDTO = new ClientDTO(client);

    return clientDTO;

    }

}
