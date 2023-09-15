package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getClientsDTO();

    ClientDTO getClientDTOById(long id);

    Client findClientById(long id);

    void saveClient(Client client);

    ClientDTO getCurrentClient(String email);

    Client findClientByEmail(String email);



}
