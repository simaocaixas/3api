package com.github.Garden.controllers;

import com.github.Garden.dto.TreeDTO;
import com.github.Garden.dto.TreeMapper;
import com.github.Garden.entities.Tree;
import com.github.Garden.services.TreeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreeControllerTest {

    @Mock
    private TreeService treeService;

    @Mock
    private TreeMapper treeMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TreeController treeController;

    private TreeDTO treeDTO;
    private Tree tree;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        treeDTO = TreeDTO.builder()
                .specie("Ficus")
                .height(1.0)
                .age(10)
                .build();

        tree = new Tree();
        tree.setId(1L);
    }

    @Test
    @DisplayName("Create tree: should return 201 CREATED with Location header")
    void createTree_shouldCreateTreeAndReturnCreatedStatus() {
        // Given
        when(treeMapper.toEntity(treeDTO)).thenReturn(tree);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        // When
        ResponseEntity<Object> response = treeController.createTree(treeDTO);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("http://localhost/trees/1"));
        verify(treeService).createTree(tree, "test@example.com");
    }

    @Test
    @DisplayName("Get tree details: should return 200 OK and TreeDTO body")
    void getTreeDetails_shouldReturnJsonTreeAndOkStatus() {
        // Given
        when(treeService.getById(1L)).thenReturn(tree);
        when(treeMapper.toDTO(tree)).thenReturn(treeDTO);

        // When
        ResponseEntity<TreeDTO> response = treeController.treeDetails(1L);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(treeDTO);
    }

    @Test
    @DisplayName("Delete tree: should return 204 NO_CONTENT")
    void deleteTree_withValidId_shouldReturnNoContentStatus() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        doNothing().when(treeService).deleteTree(tree.getId(), "test@example.com");

        // When
        ResponseEntity<Void> response = treeController.deleteTree(tree.getId());

        // Then
        assertThat(response.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.NO_CONTENT);
        verify(treeService).deleteTree(tree.getId(), "test@example.com");
    }

    @Test
    @DisplayName("Update tree: should return 204 NO_CONTENT after successful update")
    void updateTree_withValidData_shouldReturnNoContentStatus() {
        // Given
        when(treeMapper.toEntity(treeDTO)).thenReturn(tree);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        doNothing().when(treeService).updateTree(tree, 1L, "test@example.com");

        // When
        ResponseEntity<Void> response = treeController.updateTree(treeDTO, 1L);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.NO_CONTENT);
        verify(treeService).updateTree(tree, 1L, "test@example.com");
    }
}
