package cl.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.example.model.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long>{


}
