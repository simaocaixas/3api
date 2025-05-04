package com.github.Garden.controllers;

import com.github.Garden.entities.Tree;
import com.github.Garden.dto.TreeDTO;
import com.github.Garden.dto.TreeMapper;
import com.github.Garden.services.TreeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Trees", description = "Operations related to Tree CRUD management")
public class TreeController {

    private final TreeService treeService;
    private final TreeMapper treeMapper;

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Create Tree", description = "Creates a new tree for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tree created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "http://localhost:8080/trees/1"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated", content = @Content(schema = @Schema(implementation = Void.class)))
    })
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
    @Operation(summary = "Get Tree", description = "Retrieves tree details by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tree retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TreeDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Tree not found", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    public ResponseEntity<TreeDTO> treeDetails(@PathVariable Long id) {
        Tree tree = treeService.getById(id);
        TreeDTO dto = treeMapper.toDTO(tree);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Delete Tree", description = "Deletes the tree owned by the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tree deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not the owner"),
            @ApiResponse(responseCode = "404", description = "Tree not found")
    })
    public ResponseEntity<Void> deleteTree(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        treeService.deleteTree(id, email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Update Tree", description = "Updates an existing tree's details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tree updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - not the owner"),
            @ApiResponse(responseCode = "404", description = "Tree not found")
    })
    public ResponseEntity<Void> updateTree(@Valid @RequestBody TreeDTO newTreeDto, @PathVariable Long id) {
        Tree newTree = treeMapper.toEntity(newTreeDto);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        treeService.updateTree(newTree, id, email);
        return ResponseEntity.noContent().build();
    }
    
}
