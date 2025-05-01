package com.github.Garden.Repositories;
import com.github.Garden.Entities.Tree;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {
    boolean existsByPoint_XAndPoint_Y(int x, int y);
}
