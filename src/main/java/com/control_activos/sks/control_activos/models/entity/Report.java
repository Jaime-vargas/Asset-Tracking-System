package com.control_activos.sks.control_activos.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @OneToMany( cascade = CascadeType.ALL)
    private List<Photo> photos;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
    @Column(nullable = false)
    private Boolean active;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hardware_id", nullable = false)
    private Hardware hardware;
    @ManyToOne
    @JoinColumn(name = "reported_by_id", nullable = false)
    private UserEntity reportedBy;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime closedAt;
}
