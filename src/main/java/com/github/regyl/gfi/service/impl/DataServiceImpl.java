package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.dto.github.GithubIssueDto;
import com.github.regyl.gfi.dto.github.GithubRepositoryDto;
import com.github.regyl.gfi.dto.github.GithubSearchDto;
import com.github.regyl.gfi.dto.github.IssueData;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import com.github.regyl.gfi.repository.IssueRepository;
import com.github.regyl.gfi.repository.RepoRepository;
import com.github.regyl.gfi.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final IssueRepository issueRepository;
    private final RepoRepository repoRepository;
    private final BiFunction<Map<String, RepositoryEntity>, GithubIssueDto, IssueEntity> issueMapper;
    private final Function<GithubRepositoryDto, RepositoryEntity> repoMapper;

    @Async
    @Override
    public void save(IssueData response) {
        if (response == null || response.getSearch() == null || CollectionUtils.isEmpty(response.getSearch().getNodes())) {
            return;
        }

        GithubSearchDto search = response.getSearch();
        Set<RepositoryEntity> repos = search.getNodes().stream()
                .map(GithubIssueDto::getRepository)
                .map(repoMapper)
                .collect(Collectors.toSet());
        Set<String> existingRepos = repoRepository.findAllSourceIds();
        repos.removeIf(item -> existingRepos.contains(item.getSourceId()));
        repoRepository.saveAll(repos);
        Map<String, RepositoryEntity> repoCollection = repoRepository.findAll().stream()
                .collect(Collectors.toMap(RepositoryEntity::getSourceId, repo -> repo));

        List<IssueEntity> issues = search.getNodes().stream()
                .map(issue -> issueMapper.apply(repoCollection, issue))
                .toList();

        log.info("Issues found: {}", issues.size());
        issueRepository.saveAll(issues);
    }
}
