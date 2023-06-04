package git.monthly.reports.application;

import git.monthly.reports.domain.entities.*;
import git.monthly.reports.domain.interfaces.GitCommitRepository;

import java.util.ArrayList;
import java.util.List;

public class GetOrgCommitsFromRepository {
    private static GitOrganization gitOrganization;
    private static GitCommitRepository gitCommitRepository;
    private static String date;

    public GetOrgCommitsFromRepository(GitOrganization gitOrganization, GitCommitRepository gitCommitRepository,
                                       String date) {
        this.gitOrganization = gitOrganization;
        this.gitCommitRepository = gitCommitRepository;
        this.date = date;
    }

    public List<GitTeam> getOrgTeamMembersCommits(){
        var commits = getOrgReposCommits();

        for (GitTeam team: gitOrganization.getOrgTeams()) {
            team.setTeamMembers(setGitTeamMembersCommits(commits,team));
        }

        return gitOrganization.getOrgTeams();
    }

    private List<GitUser> setGitTeamMembersCommits(List<Commit> commits, GitTeam team){
        for (GitUser user: team.getTeamMembers()) {
            user.setCommits(getGitUserCommits(commits,user));
        }
        return team.getTeamMembers();
    }

    private List<Commit> getGitUserCommits(List<Commit> commits, GitUser gitUser){
        for (Commit commit: commits) {
            if (gitUser.getUserName().equals(commit.getAuthor()))
                gitUser.getCommits().add(commit);
        }
        return gitUser.getCommits();
    }

    private List<Commit> getOrgReposCommits() {
        List<Commit> commits = new ArrayList<>();
        for (String repoName: gitOrganization.getOrgRepoNames()) {
            commits.addAll(getOrgRepoCommits(repoName));
        }
        return commits;
    }

    private List<Commit> getOrgRepoCommits(String repoName){
        var commits = gitCommitRepository.getOrgCommits(gitOrganization.getOrgName(), repoName, date);
        for (Commit commit : commits) {
            commit.setStats(getOrgCommitStats(commit));
        }
        return commits;
    }

    private CommitStats getOrgCommitStats(Commit commit){
        return gitCommitRepository.getCommitStats(commit.getSHA());
    }

}
