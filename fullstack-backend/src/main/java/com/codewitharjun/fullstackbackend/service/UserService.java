package com.codewitharjun.fullstackbackend.service;

import org.springframework.stereotype.Service;
import com.codewitharjun.fullstackbackend.model.User;
import com.codewitharjun.fullstackbackend.repository.UserRepository;
import com.codewitharjun.fullstackbackend.exception.UserNotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;


@Service
public class UserService {
    private final UserRepository _userRepository;

    public UserService(UserRepository userRepository) {
        this._userRepository = userRepository;
    }

    public List<User> getAll() {
        return _userRepository.findAll();
    }

    public User get(Long id) {
        return _userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User add(User user) {
        return _userRepository.save(user);
    }

    public User update(User user, Long id) {
        return _userRepository.findById(id)
            .map(u -> {
                u.setName(user.getName());
                u.setEmail(user.getEmail());
                u.setUsername(user.getUsername());
                u.setDepartment(user.getDepartment());
                return _userRepository.save(user);
            }).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getSortedItems(String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);
        return _userRepository.findAll(sort);
    }

    public String delete(Long id) {
        if(!_userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        _userRepository.deleteById(id);

        return "User with id " + id + " has been deleted successfully.";
    }
}
