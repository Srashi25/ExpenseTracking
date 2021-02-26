package com.pairlearning.expensetrackerapi.services;

import com.pairlearning.expensetrackerapi.domain.User;
import com.pairlearning.expensetrackerapi.exceptions.EtAuthException;
import com.pairlearning.expensetrackerapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository repository;

    @Override
    public User validateUser(String email, String password) throws EtAuthException {
        if(email !=null ){
            email = email.toLowerCase();
        }
        return repository.findByEmailAndPassword(email,password);
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException {
        Pattern pattern= Pattern.compile("^(.+)@(.+)$");
        if(email != null) email = email.toLowerCase();

        if(!pattern.matcher(email).matches()){
            throw new EtAuthException("Invalid email format");
        }
        Integer count= repository.getCountByEmail(email);
        if(count > 0)
            throw  new EtAuthException("Email already in use");
        Integer userId= repository.create(firstName,lastName,email,password);

        return repository.findById(userId);
    }
}
