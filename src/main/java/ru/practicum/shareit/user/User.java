package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class User {
     protected int id;
     protected String name;
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
