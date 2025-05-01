package com.github.Garden.services;

import com.github.Garden.domain.LeafType;
import com.github.Garden.domain.Point;
import com.github.Garden.entities.Tree;
import com.github.Garden.entities.User;
import com.github.Garden.exceptions.MaximumResourcesReachedException;
import com.github.Garden.exceptions.ResourceNotFoundException;
import com.github.Garden.repositories.TreeRepository;
import com.github.Garden.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TreeRepository treeRepository;

    @InjectMocks
    private TreeService treeService;

    private User user;
    private User otherUser;
    private Tree tree;

    private static final Long TREE_ID = 1L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        otherUser = new User();
        otherUser.setEmail("testtwo@example.com");

        tree = new Tree();
        tree.setSpecie("Oak");
        tree.setAge(10);
        tree.setHeight(5.5);
        tree.setPoint(new Point(1, 1));
        tree.setOwner(user);
    }

    // CREATE TREE TESTS

    @Test
    @DisplayName("Creates a tree with the right setup, should save it in the DB")
    void createTree_withCorrectInformation_savesTree() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(treeRepository.count()).thenReturn(TREE_ID);
        when(treeRepository.existsByPoint_XAndPoint_Y(anyInt(), anyInt())).thenReturn(false);

        // When
        treeService.createTree(tree, user.getEmail());

        // Then
        verify(treeRepository).save(any(Tree.class));
    }

    @Test
    @DisplayName("Creates a tree with an email that is not registered, should throw exception")
    void createTree_withUnregisteredEmail_shouldThrowException() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> treeService.createTree(tree, user.getEmail())
        );

        verify(treeRepository, never()).save(any(Tree.class));
    }

    @Test
    @DisplayName("Room is full, should not save any tree and should throw exception")
    void createTree_whenTheresNoSpace_shouldThrowException() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(treeRepository.count()).thenReturn(treeService.getMaximumCapacity());

        // When & Then
        assertThrows(MaximumResourcesReachedException.class, () -> {
            treeService.createTree(tree, user.getEmail());
        });

        verify(treeRepository, never()).save(any(Tree.class));
    }

    @Test
    @DisplayName("Create a tree and verify if the owner is indeed the creator")
    void createTree_verifyOwner() {
        // Given
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(treeRepository.count()).thenReturn(TREE_ID);
        when(treeRepository.existsByPoint_XAndPoint_Y(anyInt(), anyInt())).thenReturn(false);

        // When
        treeService.createTree(tree, user.getEmail());

        // Then
        ArgumentCaptor<Tree> treeCaptor = ArgumentCaptor.forClass(Tree.class);
        verify(treeRepository).save(treeCaptor.capture());
        Tree savedTree = treeCaptor.getValue();

        assertNotNull(savedTree.getOwner());
        assertEquals(user.getEmail(), savedTree.getOwner().getEmail());
    }

    // UPDATE TREE TESTS

    @Test
    @DisplayName("Update tree with correct information, should then save updated tree")
    void updateTree_withValidData_updatesTreeSuccessfully() {
        // Given
        Tree updateTree = Tree.builder()
                .specie("UpdatedSpecie")
                .height(1.0)
                .age(1)
                .leafType(LeafType.NEEDLELEAF)
                .owner(user)
                .build();
        when(treeRepository.findById(TREE_ID)).thenReturn(Optional.of(tree));

        // When
        treeService.updateTree(updateTree, TREE_ID, user.getEmail());

        // Then
        verify(treeRepository).save(tree);
        assertEquals("UpdatedSpecie", tree.getSpecie());
        assertEquals(1.0, tree.getHeight());
        assertEquals(1, tree.getAge());
        assertEquals(LeafType.NEEDLELEAF, tree.getLeafType());
        assertEquals(user, tree.getOwner());
    }

    @Test
    @DisplayName("Update a tree without being its owner, should throw exception")
    void updateTree_testOwnership_dontUpdate() {
        // Given
        Tree updateTree = Tree.builder()
                .specie("UpdatedSpecie")
                .height(1.0)
                .age(1)
                .leafType(LeafType.NEEDLELEAF)
                .owner(user)
                .build();

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            treeService.updateTree(tree, TREE_ID, otherUser.getEmail());
        });

        verify(treeRepository, never()).save(any(Tree.class));
    }

    // DELETE TREE TESTS

    @Test
    @DisplayName("Delete tree with correct owner, should delete successfully")
    void deleteTree_withCorrectOwner_deletesSuccessfully() {
        // Given
        when(treeRepository.findById(TREE_ID)).thenReturn(Optional.of(tree));

        // When
        treeService.deleteTree(TREE_ID, user.getEmail());

        // Then
        verify(treeRepository).delete(tree);
    }

    // GET TREE TESTS

    @Test
    @DisplayName("Get tree by id successfully")
    void getById_withValidId_returnsTree() {
        // Given
        when(treeRepository.findById(TREE_ID)).thenReturn(Optional.of(tree));

        // When
        Tree foundTree = treeService.getById(TREE_ID);

        // Then
        assertEquals(tree, foundTree);
    }

    @Test
    @DisplayName("Get tree by invalid id, should throw exception")
    void getById_withInvalidId_shouldThrowException() {
        // Given
        when(treeRepository.findById(TREE_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            treeService.getById(TREE_ID);
        });
    }

    // FIND POSITION TEST

    @Test
    @DisplayName("Find position when no available spot should throw exception")
    void findPosition_whenNoSpace_shouldThrowException() {
        // Given
        when(treeRepository.existsByPoint_XAndPoint_Y(anyInt(), anyInt())).thenReturn(true);

        // When & Then
        assertThrows(MaximumResourcesReachedException.class, () -> {
            treeService.findPosition();
        });
    }
}
