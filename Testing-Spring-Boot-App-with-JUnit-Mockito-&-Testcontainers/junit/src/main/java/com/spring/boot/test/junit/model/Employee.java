package com.spring.boot.test.junit.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Employee")
@Table(
        name = "employee"
)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Override
    public String toString() {
        return "{\n" +
                "\t\"id\": " + this.id + ",\n" +
                "\t\"firstName\": \"" + this.firstName + "\",\n" +
                "\t\"lastName\": \"" + this.lastName + "\",\n" +
                "\t\"email\": \"" + this.email + "\"\n" +
                "}";
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;

        Employee that = (Employee) o;

        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getFirstName(), that.getFirstName()) &&
                Objects.equals(this.getLastName(), that.getLastName()) &&
                Objects.equals(this.getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 1;

        hash = prime * hash + ((this.id == null) ? 0 : this.id.hashCode());
        hash = prime * hash + ((this.firstName == null) ? 0 : this.firstName.hashCode());
        hash = prime * hash + ((this.lastName == null) ? 0 : this.lastName.hashCode());
        hash = prime * hash + ((this.email == null) ? 0 : this.email.hashCode());

        if (hash < 0)
            hash *= -1;

        return hash;
    }
}