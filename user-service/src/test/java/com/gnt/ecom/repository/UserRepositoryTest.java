package com.gnt.ecom.repository;

import com.gnt.ecom.user.entity.User;
import com.gnt.ecom.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User activeUser1;
    private User activeUser2;
    private User deletedUser1;

    private UUID activeUser1Id;
    private UUID activeUser2Id;
    private UUID deletedUser1Id;
    private final UUID nonExistentUserId = UUID.randomUUID();

    private User createUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("password123");
        return user;
    }

    @BeforeEach
    void setUp() {
        activeUser1 = createUser("activeUser1", "active1@example.com");
        activeUser2 = createUser("activeUser2", "active2@example.com");
        deletedUser1 = createUser("deletedUser1", "deleted1@example.com");

        // Persist and get managed instances with generated IDs and timestamps
        activeUser1 = entityManager.persist(activeUser1);
        activeUser2 = entityManager.persist(activeUser2);

        // Persist deletedUser1 first, then soft delete it
        deletedUser1 = entityManager.persist(deletedUser1);
        deletedUser1.softDelete(); // Use the method from BaseEntity
        deletedUser1 = entityManager.persistAndFlush(deletedUser1); // Persist the change (deletedAt and updatedAt)

        // Store IDs for later use
        activeUser1Id = activeUser1.getId();
        activeUser2Id = activeUser2.getId();
        deletedUser1Id = deletedUser1.getId();

        entityManager.flush(); // Ensure all changes are written
    }

    // --- Testing methods inherited from BaseRepository ---
    @Test
    void findAllNotDeleted_shouldReturnOnlyActiveUsers_withPagination() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> resultPage = userRepository.findAllNotDeleted(pageable);

        assertThat(resultPage.getContent()).hasSize(2);
        assertThat(resultPage.getContent()).extracting(User::getUsername)
                .containsExactlyInAnyOrder(activeUser1.getUsername(), activeUser2.getUsername());
        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        // Verify that none of the returned users are marked as deleted
        resultPage.getContent().forEach(user -> assertThat(user.getDeletedAt()).isNull());
        resultPage.getContent().forEach(user -> assertThat(user.isDeleted()).isFalse());
    }

    @Test
    void findAllNotDeleted_shouldReturnEmptyPage_whenAllUsersAreEffectivelyDeleted() {
        // Soft delete the remaining active users for this test
        User userToModify1 = userRepository.findById(activeUser1Id).orElseThrow();
        userToModify1.softDelete();
        entityManager.persist(userToModify1);

        User userToModify2 = userRepository.findById(activeUser2Id).orElseThrow();
        userToModify2.softDelete();
        entityManager.persist(userToModify2);

        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 5);
        Page<User> resultPage = userRepository.findAllNotDeleted(pageable);

        assertThat(resultPage.getContent()).isEmpty();
        assertThat(resultPage.getTotalElements()).isEqualTo(0);
    }

    @Test
    void findByIdNotDeleted_shouldReturnUser_whenUserExistsAndNotDeleted() {
        Optional<User> foundUser = userRepository.findByIdNotDeleted(activeUser1Id);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(activeUser1.getUsername());
        assertThat(foundUser.get().getDeletedAt()).isNull();
        assertThat(foundUser.get().isDeleted()).isFalse();
    }

    @Test
    void findByIdNotDeleted_shouldReturnEmpty_whenUserIsDeleted() {
        Optional<User> foundUser = userRepository.findByIdNotDeleted(deletedUser1Id);
        assertThat(foundUser).isNotPresent();
    }

    @Test
    void findByIdNotDeleted_shouldReturnEmpty_whenUserDoesNotExist() {
        Optional<User> foundUser = userRepository.findByIdNotDeleted(nonExistentUserId);
        assertThat(foundUser).isNotPresent();
    }

    @Test
    void findAllByIdNotDeleted_shouldReturnOnlyActiveUsersFromList() {
        List<UUID> idsToSearch = Arrays.asList(activeUser1Id, deletedUser1Id, activeUser2Id, nonExistentUserId);
        List<User> foundUsers = userRepository.findAllByIdNotDeleted(idsToSearch);

        assertThat(foundUsers).hasSize(2);
        assertThat(foundUsers).extracting(User::getUsername)
                .containsExactlyInAnyOrder(activeUser1.getUsername(), activeUser2.getUsername());
        foundUsers.forEach(user -> assertThat(user.isDeleted()).isFalse());
    }

    @Test
    void findAllByIdNotDeleted_shouldReturnEmptyList_whenAllMatchingUsersAreDeletedOrNonExistent() {
        List<UUID> idsToSearch = Arrays.asList(deletedUser1Id, nonExistentUserId);
        List<User> foundUsers = userRepository.findAllByIdNotDeleted(idsToSearch);
        assertThat(foundUsers).isEmpty();
    }

    // --- Testing methods specific to UserRepository ---

    @Test
    void findByUsername_shouldReturnUser_whenUserExists_regardlessOfDeletedStatus() {
        // Current findByUsername does not filter by deletedAt
        Optional<User> foundActiveUser = userRepository.findByUsername(activeUser1.getUsername());
        assertThat(foundActiveUser).isPresent();
        assertThat(foundActiveUser.get().getId()).isEqualTo(activeUser1Id);

        Optional<User> foundDeletedUser = userRepository.findByUsername(deletedUser1.getUsername());
        assertThat(foundDeletedUser).isPresent(); // This will find the soft-deleted user
        assertThat(foundDeletedUser.get().getId()).isEqualTo(deletedUser1Id);
        assertThat(foundDeletedUser.get().isDeleted()).isTrue();
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameNotExists() {
        Optional<User> foundUser = userRepository.findByUsername("nonExistentUser");
        assertThat(foundUser).isNotPresent();
    }

    // --- Optional: Test @PrePersist and @PreUpdate from BaseEntity implicitly ---
    @Test
    void whenUserIsPersisted_createdAtAndUpdatedAtShouldBeSet() {
        User newUser = createUser("newUser", "new@example.com");
        User persistedUser = entityManager.persistFlushFind(newUser); // Persist, flush, and retrieve

        assertThat(persistedUser.getId()).isNotNull();
        assertThat(persistedUser.getCreatedAt()).isNotNull();
        assertThat(persistedUser.getUpdatedAt()).isNotNull();
        assertThat(persistedUser.getCreatedAt()).isEqualTo(persistedUser.getUpdatedAt());
    }

    @Test
    void whenUserIsUpdated_updatedAtShouldChange() throws InterruptedException {
        User userToUpdate = userRepository.findById(activeUser1Id).orElseThrow();

        // Ensure there's a slight time difference for updatedAt to change
        // In a real scenario, this might not be needed if the operation takes time,
        // but in tests, operations can be too fast.
        LocalDateTime originalUpdatedAt = userToUpdate.getUpdatedAt();

        // Make a change
        userToUpdate.setPhoneNumber("1234567890");
        User updatedUser = entityManager.persistFlushFind(userToUpdate); // Persist, flush, and retrieve

        assertThat(updatedUser.getUpdatedAt()).isNotNull();
        assertThat(updatedUser.getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt); // Should be after or equal if very fast
        // If you can guarantee a time difference: assertThat(updatedUser.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    void softDelete_shouldSetDeletedAt_andIsDeletedShouldBeTrue() {
        User userToSoftDelete = userRepository.findById(activeUser1Id).orElseThrow();
        assertThat(userToSoftDelete.isDeleted()).isFalse();
        assertThat(userToSoftDelete.getDeletedAt()).isNull();

        userToSoftDelete.softDelete();
        User softDeletedUser = entityManager.persistFlushFind(userToSoftDelete);

        assertThat(softDeletedUser.isDeleted()).isTrue();
        assertThat(softDeletedUser.getDeletedAt()).isNotNull();
        // Also, updatedAt should have been modified by @PreUpdate through persist
        assertThat(softDeletedUser.getUpdatedAt()).isAfterOrEqualTo(softDeletedUser.getCreatedAt());
    }
}