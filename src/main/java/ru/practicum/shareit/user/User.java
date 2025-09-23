package ru.practicum.shareit.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 512)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
}