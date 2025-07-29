package org.example.vkedupractice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "segments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "segments", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 