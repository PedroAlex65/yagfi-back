package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RepoRepository extends AbstractRepository<RepositoryEntity> {

    @Query("select sourceId from RepositoryEntity where sourceId is not null")
    Set<String> findAllSourceIds();
}
