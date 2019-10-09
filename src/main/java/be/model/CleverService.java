package be.model;


import be.db.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CleverService {
    @Autowired
    private UserRepository songRepository;
}
