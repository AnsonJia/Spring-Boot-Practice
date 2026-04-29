package com.SpringPractice.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "loyalty_points")
    private Integer loyaltyPoints;


    @OneToOne
    @JoinColumn(name = "id") //profile is the owner, so set join column by the foreign key in profile
    //required for defining one-to-one relationships (for the owner only)
    @MapsId//tells hibernate to use the same col as both the prim and foreign key
    @ToString.Exclude
    private User user;

}
