package com.ems.bpm_comparisontable.service;

import com.ems.bpm_comparisontable.model.Contractor;
import com.ems.bpm_comparisontable.model.Project;
import com.ems.bpm_comparisontable.model.ProjectItem;
import com.ems.bpm_comparisontable.pojos.*;
import com.ems.bpm_comparisontable.repository.ContractorRepository;
import com.ems.bpm_comparisontable.repository.ProjectItemRepository;
import com.ems.bpm_comparisontable.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectDataService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectItemRepository projectItemRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    public Project createNewProject(OwnerContractHeader  ownerContract, String amount, String userId, LocalDateTime now) {
        Project project = new Project();
        project.setProjectName(ownerContract.getProjectName());
        project.setConstructionSite(ownerContract.getConstructionSite());
        project.setAmount(amount);
        project.setCreatedBy(userId);
        project.setCreatedDate(now);
        return projectRepository.save(project);
    }

    public ProjectItem createNewProjectItem(int projectId, OwnerContractItem ownerContractItem, String userId, LocalDateTime now) {
        ProjectItem projectItem = new ProjectItem(projectId, ownerContractItem);
        projectItem.setCreatedBy(userId);
        projectItem.setCreatedDate(now);
        return projectItemRepository.save(projectItem);
    }

    public Optional<ReturnProjectAllData> getProjectAllData(int projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(optionalProject.isPresent()) {
            ReturnProjectAllData projectAllData = new ReturnProjectAllData();
            projectAllData.setProject(new ReturnProjectMain(optionalProject.get()));


            List<ProjectItem> projectItems = projectItemRepository.findByIdProjectIdOrderByIdSequenceAsc(projectId);
            List<ReturnProjectItem> returnProjectItems = new LinkedList<>();
            for (ProjectItem projectItem : projectItems) {
                returnProjectItems.add(new ReturnProjectItem(projectItem));
            }
            projectAllData.setProjectItems(returnProjectItems);

            return Optional.of(projectAllData);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Project> findProject(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }

    public Contractor saveContractor(String contractorName, String userId) {
        Optional<Contractor> opt = contractorRepository.findByName(contractorName);
        if(opt.isPresent()) {
            return opt.get();
        } else {
            Contractor contractor = new Contractor();
            contractor.setName(contractorName);
            contractor.setCreatedBy(userId);
            contractor.setCreatedDate(LocalDateTime.now());
            return contractorRepository.save(contractor);
        }
    }
}
