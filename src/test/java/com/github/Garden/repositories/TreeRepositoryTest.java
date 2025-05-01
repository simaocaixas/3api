package com.github.Garden.repositories;

import com.github.Garden.domain.LeafType;
import com.github.Garden.domain.Point;
import com.github.Garden.entities.Tree;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TreeRepositoryTest {

    @Autowired
    TreeRepository treeRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should return true when tree exists with given Point")
    void existsByPoint_existing_returnsTrue() {
        Tree tree = createAndPersistTree("Ficus");
        boolean exists = treeRepository.existsByPoint_XAndPoint_Y(1,1);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when tree doesnt exists with given Point")
    void existsByPoint_withNonExistingPoint_returnsFalse() {
        boolean exists = treeRepository.existsByPoint_XAndPoint_Y(1,1);
        assertThat(exists).isFalse();
    }


    private Tree createAndPersistTree(String specie) {
        Tree tree = Tree.builder()
                .specie(specie)
                .height(specie.length() * 1.0)
                .age(specie.length() + 2)
                .leafType(LeafType.BROADLEAF)
                .build();

        tree.setPoint(new Point(1,1));

        entityManager.persist(tree);
        entityManager.flush();
        return tree;
    }

}