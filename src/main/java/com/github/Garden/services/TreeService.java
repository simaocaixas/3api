package com.github.Garden.services;

import com.github.Garden.entities.User;
import com.github.Garden.exceptions.AccessDeniedException;
import com.github.Garden.exceptions.MaximumResourcesReachedException;
import com.github.Garden.exceptions.ResourceNotFoundException;
import com.github.Garden.domain.Point;
import com.github.Garden.entities.Tree;
import com.github.Garden.repositories.TreeRepository;
import com.github.Garden.repositories.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class TreeService {

    private static final int MAXIMUM_ROW_CAPACITY = 10;
    private static final int MAXIMUM_COLUMN_CAPACITY = 10;     // Ten columns fill the room (just to add some business logic)
    private static final int MAXIMUM_ROOM_CAPACITY = MAXIMUM_ROW_CAPACITY * MAXIMUM_COLUMN_CAPACITY; // Each tree occupies one square meter


    private final TreeRepository treeRepository;
    private final UserRepository userRepository;

    public TreeService(TreeRepository treeRepository, UserRepository userRepository) {
        this.treeRepository = treeRepository;
        this.userRepository = userRepository;
    }

    public void createTree(Tree tree, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long totalTrees = treeRepository.count();
        if (totalTrees >= MAXIMUM_ROOM_CAPACITY) {
            throw new MaximumResourcesReachedException("Tree limit reached, no room for more! Maximum: " + MAXIMUM_ROOM_CAPACITY);
        }

        if (tree.getPoint() == null) {
            tree.setPoint(new Point());
        }

        Point newTreePosition = findPosition();

        tree.getPoint().setX(newTreePosition.getX());
        tree.getPoint().setY(newTreePosition.getY());

        tree.setOwner(user);
        treeRepository.save(tree);
   }

    public Point findPosition() {

        for (int y = 1; y <= MAXIMUM_COLUMN_CAPACITY; y++) {
            for (int x = 1; x <= MAXIMUM_ROW_CAPACITY; x++) {
                Point point = new Point(x,y);
                boolean isOccupied = treeRepository.existsByPoint_XAndPoint_Y(x,y);
                if (!isOccupied) {
                    return point;
                }
            }
        }
        throw new MaximumResourcesReachedException("No available position for the tree!");
    }

    public void deleteTree(Long id, String email) {
        verifyOwnership(id, email);
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id " + id));
        treeRepository.delete(tree);
    }

    public void updateTree(Tree newTree, Long id, String email) {
        verifyOwnership(id, email);

        Tree oldTree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id " + id));
        oldTree.setAge(newTree.getAge());
        oldTree.setHeight(newTree.getHeight());
        oldTree.setLeafType(newTree.getLeafType());
        oldTree.setSpecie(newTree.getSpecie());
        treeRepository.save(oldTree);
    }

    public Tree getById(Long id) {
        return treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id " + id));
    }

    public void verifyOwnership(Long treeId, String email) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found"));

        if (!tree.getOwner().getEmail().equals(email)) {
            throw new AccessDeniedException("You do not own this tree.");
        }
    }

    public long getMaximumCapacity() {
        return MAXIMUM_ROOM_CAPACITY;
    }

}
