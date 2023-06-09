package git.monthly.reports.application;

import git.monthly.reports.domain.entities.GitOrganization;
import git.monthly.reports.domain.entities.GitUserMonthlyReport;
import git.monthly.reports.domain.exceptions.ReportClientConnectionException;
import git.monthly.reports.domain.interfaces.OrgMonthlyReportRepository;

import java.util.ArrayList;
import java.util.List;

public class GetGitOrgMonthlyReportsFromRepository {
    private static GitOrganization gitOrganization;
    private static OrgMonthlyReportRepository orgMonthlyReportRepository;
    private static String date;

    public GetGitOrgMonthlyReportsFromRepository(GitOrganization gitOrganization,
                                                 OrgMonthlyReportRepository orgMonthlyReportRepository, String date) {
        this.gitOrganization = gitOrganization;
        this.orgMonthlyReportRepository = orgMonthlyReportRepository;
        this.date = date;
    }

    public List<GitUserMonthlyReport> getOrgMonthlyReport() throws ReportClientConnectionException {
        if (findOrgMonthlyReport()){
            return  orgMonthlyReportRepository.getOrgMonthlyReport(gitOrganization.getOrgName(), date);
        }
        else return new ArrayList<GitUserMonthlyReport>();
    }

    private boolean findOrgMonthlyReport() throws ReportClientConnectionException {
        return orgMonthlyReportRepository.findOrgMontlyReport(gitOrganization.getOrgName(), date);
    }
}
