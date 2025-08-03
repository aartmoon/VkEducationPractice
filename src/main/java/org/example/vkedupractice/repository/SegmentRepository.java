package org.example.vkedupractice.repository;

import org.example.vkedupractice.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {
    
    Optional<Segment> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT s FROM Segment s JOIN s.users u WHERE u.id = :userId")
    List<Segment> findSegmentsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(u) FROM User u JOIN u.segments s WHERE s.name = :segmentName")
    long countUsersInSegment(@Param("segmentName") String segmentName);
}