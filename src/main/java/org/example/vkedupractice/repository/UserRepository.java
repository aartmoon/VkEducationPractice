package org.example.vkedupractice.repository;

import org.example.vkedupractice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.segments s WHERE s.name = :segmentName")
    List<User> findUsersBySegmentName(@Param("segmentName") String segmentName);


    @Query("SELECT COUNT(u) FROM User u")
    long countAllUsers();

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.segments")
    List<User> findAllWithSegments();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.segments WHERE u.id IN :ids")
    List<User> findAllWithSegmentsById(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.segments WHERE u.id = :id")
    Optional<User> findByIdWithSegments(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM User u JOIN u.segments s WHERE s.name = :segmentName")
    List<User> findUsersBySegmentNameWithSegments(@Param("segmentName") String segmentName);
}
