package com.example.demo.servis;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class UserService implements UserDetailsService {

    private final UserRepo userData;
    private final DefaultEmailService emailService;
    private final CodeGeneration codeGeneration;

    public UserService(UserRepo userData, DefaultEmailService emailService, CodeGeneration codeGeneration) {
        this.userData = userData;
        this.emailService = emailService;
        this.codeGeneration = codeGeneration;
    }

    public List<User> getAll() {
        return userData.findAll();
    }

    public Optional<User> getByLogin(String login) {
        return Optional.ofNullable(userData.getByLogin(login));
    }

    public List<User> findUser(String name) throws InterruptedException, ExecutionException {
        int pageSize = 100;
        int totalPages = (int) Math.ceil(userData.countByNameContaining(name) / (double) pageSize);
        ExecutorService executor = Executors.newFixedThreadPool(totalPages);
        List<CompletableFuture<List<User>>> futures = new ArrayList<>();
        for (int page = 0; page < totalPages; page++) {
            int finalPage = page;
            CompletableFuture<List<User>> future = CompletableFuture.supplyAsync(() -> userData.findByNameContaining(name, PageRequest.of(finalPage, pageSize)), executor);
            futures.add(future);
        }
        List<User> users = new ArrayList<>();
        for (CompletableFuture<List<User>> future : futures) {
            users.addAll(future.get());
        }
        executor.shutdown();
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = getByLogin(login).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s is not found", login)));
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), true, true, true, true, new HashSet<>());
    }

    public void registration(String email, String name, String login, String password) throws Exception {
        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password) || StringUtils.isEmpty(email) || StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("login, password, email, and name cannot be null or empty");
        }
        if (getByLogin(login).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User newUser = new User(email, name, login, password);
        String code = codeGeneration.generate(6);
        newUser.setCodeActive(code);
        emailService.send(email, "Activation Code", code);
        userData.save(newUser);
    }

    public Optional<User> activeUser(String login, String code) throws Exception {
        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("login and code cannot be null or empty");
        }
        User user = getByLogin(login).orElseThrow(() -> new Exception("User not found"));
        if (Objects.equals(user.getCodeActive(), code)) {
            user.setCodeActive(null);
            user.setStatusActive(true);
            userData.save(user);
            return Optional.of(user);
        } else {
            throw new Exception("Invalid activation code");
        }
    }
}
