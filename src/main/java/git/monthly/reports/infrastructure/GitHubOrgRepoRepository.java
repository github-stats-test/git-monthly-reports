package git.monthly.reports.infrastructure;

import git.monthly.reports.domain.exceptions.EmptyOrganizationRepoException;
import git.monthly.reports.domain.exceptions.GitClientConnectionException;
import git.monthly.reports.domain.interfaces.GitRepoRepository;
import git.monthly.reports.domain.interfaces.GitRepositoryClientConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubOrgRepoRepository implements GitRepoRepository {
    private static GitRepositoryClientConnection gitHubConnection;


    public GitHubOrgRepoRepository(GitRepositoryClientConnection gitHubConnection) {
        this.gitHubConnection = gitHubConnection;
    }

    @Override
    public List<String> getOrgRepos(String orgName) throws GitClientConnectionException, EmptyOrganizationRepoException {
        System.out.println("Fetching Organization Repository Data");
        return executeGetOrgRepoCall(orgName);
    }

    private List<String> executeGetOrgRepoCall(String orgName) throws GitClientConnectionException, EmptyOrganizationRepoException {
        List<String> repos = new ArrayList<>();
        String query = "orgs/"+orgName+"/repos";
        String responseJson = gitHubConnection.execute(query);

        JSONArray reposArray = new JSONArray(responseJson);

        for (int i = 0; i < reposArray.length(); i++) {
            JSONObject repoObject = reposArray.getJSONObject(i);
            try {
                var repoName = repoObject.getString("name");
                repos.add(repoName);
            } catch (Exception e){
                throw new EmptyOrganizationRepoException();
            }

        }
        return repos;
    }

}
