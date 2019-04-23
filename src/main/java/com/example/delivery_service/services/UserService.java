package com.example.delivery_service.services;

import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {

    private final StateService stateService;
    private final UserRepository repository;
    private final RoleService roleService;

    @Autowired
    public UserService(StateService stateService, UserRepository repository, RoleService roleService) {
        this.stateService = stateService;
        this.repository = repository;
        this.roleService = roleService;
    }


    public void saveOrUpdate(User user) {

        Optional<User> origOptUser = Optional.empty();

        if(user.getId() != null) {
            origOptUser = getUserById(user.getId(), true);
        }

        /**EXIST*/
        if (origOptUser.isPresent()) {
            User origUser = origOptUser.get();

            user.setCreateDate(origUser.getCreateDate());

            //ROLES
            if (user.getRoles() == null || user.getRoles().size() == 0)
                user.setRoles(origUser.getRoles());
        }
        /**NEW*/
        else{
            user.setCreateDate(new Date());
            user.setRoles(new HashSet<>(Collections.singletonList(roleService.getRoleByName("CUSTOMER").orElse(null))));
        }

        //ADDRESS
        if(user.getAddress() != null) {
            if (user.getAddress().isEmpty())
                //zabrání uložení prázdné adresy
                user.setAddress(null);
            else {
                //nastaví State podle shortcut
                user.getAddress().setState(stateService.getStateByShortcut(user.getAddress().getState().getShortcut()).orElse(null));
                user.getAddress().setUpdateDate(new Date());
                if(user.getAddress().getId() == null)
                    user.getAddress().setCreateDate(new Date());
            }
        }

        user.setUpdateDate(new Date());

        repository.save(user);
    }

    public List<User> getAllUsers() {
        return (List<User>) repository.findAll();
    }

    public Optional<User> getUserById(Long id, boolean withPwd) {
        Optional<User> userOptional = repository.findById(id);

        if(!withPwd){
            userOptional.ifPresent(user -> user.setPassword(""));
        }

        return userOptional;
    }

    public Optional<User> getUserById(Long id) {
        return getUserById(id, false);
    }

    public void removeUser(Long id) {
        repository.deleteById(id);
    }

    public List<User> getByFullname(String surname) {
        return repository.getByFullname(surname);
    }

    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }
}
