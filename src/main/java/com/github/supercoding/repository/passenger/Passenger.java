package com.github.supercoding.repository.passenger;

import com.github.supercoding.repository.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "passengerId")
@Builder
@Entity
@ToString
@Table(name = "passenger")
public class Passenger {
    @Id
    @Column(name = "passenger_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passengerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;
    @Column(name = "passport_num", length = 50)
    private String passportNum;
}