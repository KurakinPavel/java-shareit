package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USERS", schema = "PUBLIC")
@Getter
@Setter
@AllArgsConstructor
public class User {
     @Id
     @Column(name = "USER_ID", nullable = false)
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     protected int id;
     @Column(name = "NAME", nullable = false)
     protected String name;
     @Column(name = "EMAIL", nullable = false)
     protected String email;

     @Override
     public boolean equals(Object object) {
          if (this == object) return true;
          if (!(object instanceof User)) return false;
          User user = (User) object;
          return getId() == user.getId() && Objects.equals(getName(), user.getName()) && Objects.equals(getEmail(),
                  user.getEmail());
     }

     @Override
     public int hashCode() {
          return Objects.hash(getId(), getName(), getEmail());
     }
}
