package com.github.Garden.Services;

import com.github.Garden.Exceptions.MaximumResourcesReachedException;
import com.github.Garden.Exceptions.ResourceNotFoundException;
import com.github.Garden.Entities.Point;
import com.github.Garden.Entities.Tree;
import com.github.Garden.Repositories.TreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TreeService {

    @Autowired
    private TreeRepository treeRepository;

    private static final int MAXIMUM_ROW_CAPACITY = 10;
    private static final int MAXIMUM_COLUMN_CAPACITY = 10;     // Ten columns  fill the room (idk just to add some business logic)
    private static final int MAXIMUM_ROOM_CAPACITY = MAXIMUM_ROW_CAPACITY * MAXIMUM_COLUMN_CAPACITY; // Each tree occupies one square meter

    public TreeService()  {}

    public TreeService(TreeRepository treeRepository) {
        this.treeRepository = treeRepository;
    }

    public void createTree(Tree tree) {

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

    public void deleteTree(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id " + id));
        treeRepository.delete(tree);
    }

    public void updateTree(Tree newTree, Long id) {

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
}
