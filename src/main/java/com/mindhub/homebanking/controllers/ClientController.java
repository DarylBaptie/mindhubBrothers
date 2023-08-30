package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController

public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String randomNumber() {
        String random = "VIN" + getRandomNumber(0, 99999999);
        return random;
    }

    private int getRandomNumber(int i, int i1) {
        return (int) ((Math.random() * (i - i1)) + i);
    }


    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());

        }

    @RequestMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){

        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);

    }


    @RequestMapping("/api/clients/current")
    public ClientDTO getClient(Authentication authentication) {

        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));

    }







    @RequestMapping(path = "/api/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {



        if (firstName.isBlank()) {

            return new ResponseEntity<>("First name is missing", HttpStatus.FORBIDDEN);

        }

        if (lastName.isBlank()) {

            return new ResponseEntity<>("Surname is missing", HttpStatus.FORBIDDEN);

        }
        if (email.isBlank()) {

            return new ResponseEntity<>("Email is missing", HttpStatus.FORBIDDEN);

        }
        if (password.isBlank()) {

            return new ResponseEntity<>("Password is missing", HttpStatus.FORBIDDEN);

        }


        if (clientRepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }


        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account(randomNumber(), LocalDateTime.now(), 0);
        newClient.addAccount(newAccount);
        clientRepository.save(newClient);
        accountRepository.save(newAccount);

        return new ResponseEntity<>("Client created", HttpStatus.CREATED);

    }


}
