package com.github.Garden.controllers;

import com.github.Garden.entities.Tree;
import com.github.Garden.dto.TreeDTO;
import com.github.Garden.dto.TreeMapper;
import com.github.Garden.services.TreeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/trees")
@RequiredArgsConstructor
public class TreeController {

    private final TreeService treeService;
    private final TreeMapper treeMapper;

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> createTree(@Valid @RequestBody TreeDTO treeDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Tree tree = treeMapper.toEntity(treeDto);
        treeService.createTree(tree, email);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/trees/{id}").
                buildAndExpand(tree.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TreeDTO> treeDetails(@PathVariable Long id) {
        Tree tree = treeService.getById(id);
        TreeDTO dto = treeMapper.toDTO(tree);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> deleteTree(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        treeService.deleteTree(id, email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> updateTree(@Valid @RequestBody TreeDTO newTreeDto, @PathVariable Long id) {
        Tree newTree = treeMapper.toEntity(newTreeDto);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        treeService.updateTree(newTree, id, email);
        return ResponseEntity.noContent().build();
    }
    
}
