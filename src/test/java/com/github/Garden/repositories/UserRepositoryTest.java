package com.github.Garden.repositories;
import com.github.Garden.entities.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User successfully from DB by email")
    void findByEmail_withExistingEmail_returnsUser() {
        User user = createAndPersistUser("jondoe@gmail.com");
        Optional<User> result = this.userRepository.findByEmail(user.getEmail());

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
        assertThat(result.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("Should not return User from DB when email does not exist")
    void findByEmail_withNonExistingEmail_returnsEmptyOptional() {
        String email = "jonjon@gmail.com";

        Optional<User> result = this.userRepository.findByEmail(email);

        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("Should return true when user exists with given email")
    void existsByEmail_withExistingEmail_returnsTrue() {
        User user = createAndPersistUser("jane.doe@example.com");
        boolean exists = userRepository.existsByEmail(user.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when user does not exist with given email")
    void existsByEmail_withNonExistingEmail_returnsFalse() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should return correct user count")
    void countUsers_returnsExpectedCount() {
        createAndPersistUser("jhonONE@example.com");
        createAndPersistUser("jhonTWO@example.com");

        long count = userRepository.count();

        assertThat(count).isEqualTo(2);
    }


    private User createAndPersistUser(String email) {
        User user = User.builder()
                .username("user_" + email)
                .email(email)
                .firstName("Test")
                .lastName("User")
                .password("test1234")
                .roles("USER")
                .build();

        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

}