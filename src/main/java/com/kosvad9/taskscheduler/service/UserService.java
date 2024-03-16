package com.kosvad9.taskscheduler.service;

import com.kosvad9.taskscheduler.dto.UserLoginDto;
import com.kosvad9.taskscheduler.dto.UserDto;
import com.kosvad9.taskscheduler.entity.User;
import com.kosvad9.taskscheduler.exception.EmailTakenException;
import com.kosvad9.taskscheduler.exception.IncorrectUsernamePasswordException;
import com.kosvad9.taskscheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto getUser(Long id){
        return userRepository.findUserById(id)
                .map(user -> new UserDto(user.getId(), user.getLogin()))
                .orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserDto createUser(UserLoginDto userLoginDto){
        if (userRepository.findUserByLogin(userLoginDto.email()).isPresent()) throw new EmailTakenException();
        User newUser = User.builder()
                .login(userLoginDto.email())
                .password(passwordEncoder.encode(userLoginDto.password())).build();
        userRepository.saveAndFlush(newUser);
        return new UserDto(newUser.getId(), newUser.getLogin());
    }

    public UserDto login(UserLoginDto userLoginDto){
        User user = loadUserByUsername(userLoginDto.email());
        boolean authResult = passwordEncoder.matches(userLoginDto.password(),
                                    user.getPassword());
        if (authResult) return new UserDto(user.getId(), user.getLogin());
        else throw new IncorrectUsernamePasswordException("Неверный логин или пароль!");
    }

    private User loadUserByUsername(String username){
        var user = userRepository.findUserByLogin(username);
        return user.orElseThrow(() ->
                                new IncorrectUsernamePasswordException("Неверный логин или пароль!"));
    }
}
