package com.ems.bpm_comparisontable.service;

import com.ems.bpm_comparisontable.model.Contractor;
import com.ems.bpm_comparisontable.model.Project;
import com.ems.bpm_comparisontable.model.ProjectItem;
import com.ems.bpm_comparisontable.model.ContractorContractItem;
import com.ems.bpm_comparisontable.pojos.*;
import com.ems.bpm_comparisontable.repository.ContractorContractItemRepository;
import com.ems.bpm_comparisontable.repository.ContractorRepository;
import com.ems.bpm_comparisontable.repository.ProjectItemRepository;
import com.ems.bpm_comparisontable.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectDataService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectItemRepository projectItemRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private ContractorContractItemRepository contractorContractItemRepository;

    public Project createNewProject(OwnerContractHeader ownerContract, String amount, String userId, LocalDateTime now) {
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

    public ContractorContractItem createNewContractorContractItem(int projectId, int contractorId, int sequence, ContractorItem contractorItem, String userId, LocalDateTime now) {
        ContractorContractItem contractorContractItem = new ContractorContractItem(projectId, contractorId, sequence, contractorItem);
        contractorContractItem.setCreatedBy(userId);
        contractorContractItem.setCreatedDate(now);
        return contractorContractItemRepository.save(contractorContractItem);
    }

    public Optional<ReturnProjectAllData> getProjectAllData(int projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(optionalProject.isPresent()) {
            ReturnProjectAllData projectAllData = new ReturnProjectAllData();
            //專案
            projectAllData.setProject(new ReturnProjectMain(optionalProject.get()));
            //專案項目
            List<ProjectItem> projectItems = projectItemRepository.findByIdProjectIdOrderByIdSequenceAsc(projectId);
            projectAllData.setProjectItems(projectItems.stream()
                    .map(ReturnProjectItem::new)
                    .collect(Collectors.toList()));
            //包商
            List<Contractor> contractors = contractorRepository.getAllContractorsOfProject(projectId);
            projectAllData.setContractors(contractors.stream()
                    .map(a -> new ReturnContractor(a.getId(), a.getName()))
                    .collect(Collectors.toList()));
            //包商合約項目
            Map<Integer, List<ReturnContractorContractItem>> returnContractorContractItemMap = new HashMap<>();
            for(Contractor contractor : contractors) {
                returnContractorContractItemMap.put(
                        contractor.getId(),
                        contractorContractItemRepository
                                .getContractorContractItem(projectId, contractor.getId()).stream()
                                .map(ReturnContractorContractItem::new)
                                .collect(Collectors.toList())
                        );
            }
            projectAllData.setContractorContractItemMap(returnContractorContractItemMap);

            return Optional.of(projectAllData);
        } else {
            return Optional.empty();
        }
    }

    public void removeContractorContractItems(int projectId, int contractorId){
        System.out.println("projectId:"+projectId+" contractorId:"+contractorId);
        contractorContractItemRepository.removeAll(projectId, contractorId);
    }
}
