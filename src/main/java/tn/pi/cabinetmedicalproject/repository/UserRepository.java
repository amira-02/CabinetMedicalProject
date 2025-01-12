package tn.pi.cabinetmedicalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pi.cabinetmedicalproject.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
