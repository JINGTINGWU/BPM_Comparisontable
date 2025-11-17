package com.ems.bpm_comparisontable.service;

import com.ems.bpm_comparisontable.model.*;
import com.ems.bpm_comparisontable.pojos.*;
import com.ems.bpm_comparisontable.repository.*;
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

    @Autowired
    private ProjectAppentItemHeadRepository projectAppentItemHeadRepository;

    @Autowired
    private ProjectAppentItemBodyRepository projectAppentItemBodyRepository;

    public Project createNewProject(OwnerContractHeader ownerContract, String amount, String userId, LocalDateTime now) {
        Project project = new Project();
        project.setProjectName(ownerContract.getProjectName());
        project.setConstructionSite(ownerContract.getConstructionSite());
        project.setAmount(amount);
        project.setEnabled("Y");
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

            List<LinkedList<String>> bodyDetail = new LinkedList<>();
            for(ReturnProjectItem item: projectAllData.getProjectItems()) {
                LinkedList<String> detail = new LinkedList<>();
                detail.add(item.getItem());//項 次
                detail.add(item.getItemDescription()); //項 目 及 說 明
                detail.add(item.getItemDescription()); //單 位
                detail.add(item.getItemDescription()); //數 量
                detail.add(item.getItemDescription()); //單 價
                detail.add(item.getItemDescription()); //複 價
                detail.add(item.getItemDescription()); //備 註
                bodyDetail.add(detail);
            }


            //追加工項
            List<ProjectAppendItemHead> appendItemHeads = projectAppentItemHeadRepository.findAllByProjectIdAndEnabled(projectId, true);
            projectAllData.setAppendItemHeads(appendItemHeads.stream()
                    .map(a -> new ReturnProjectAppendItemHead(a.getId(), a.getCreatedBy(), a.getCreatedDate()))
                    .collect(Collectors.toList()));
            //projectAppentItemBodyRepository;


            //包商
            List<Contractor> contractors = contractorRepository.getAllContractorsOfProject(projectId);
            projectAllData.setContractors(contractors.stream()
                    .map(a -> new ReturnProjectContractor(a.getId(), a.getName()))
                    .collect(Collectors.toList()));
            //包商合約項目
            Map<Integer, List<ReturnProjectContractorContractItem>> returnContractorContractItemMap = new HashMap<>();
            for(Contractor contractor : contractors) {
                returnContractorContractItemMap.put(
                        contractor.getId(),
                        contractorContractItemRepository
                                .getContractorContractItem(projectId, contractor.getId()).stream()
                                .map(ReturnProjectContractorContractItem::new)
                                .collect(Collectors.toList())
                        );
            }
            projectAllData.setContractorContractItemMap(returnContractorContractItemMap);

            projectAllData.setBodyDetail(bodyDetail);
            return Optional.of(projectAllData);
        } else {
            return Optional.empty();
        }
    }

    public Optional<ReturnProjectAllData> getProjectAllData(String projectName) {
        Optional<Project> optProject = projectRepository.findByProjectName(projectName);
        if(optProject.isPresent()) {
            return  getProjectAllData(optProject.get().getId());
        } else {
            return Optional.empty();
        }
    }

    public void removeContractorContractItems(int projectId, int contractorId){
        System.out.println("projectId:"+projectId+" contractorId:"+contractorId);
        contractorContractItemRepository.removeAll(projectId, contractorId);
    }

    public void createProjectAppentItem(Project project, List<ContractorItem> items, String userId) {
        LocalDateTime now = LocalDateTime.now();

        ProjectAppendItemHead head = new ProjectAppendItemHead();
        head.setProjectId(project.getId());
        head.setEnabled(true);
        head.setCreatedBy(userId);
        head.setCreatedDate(now);

        ProjectAppendItemHead savedHead = projectAppentItemHeadRepository.save(head);
        System.out.println("savedHead:"+savedHead.getId());
        for (ContractorItem item : items) {
            ProjectAppendItemBodyId id = new ProjectAppendItemBodyId();
            id.setHeadId(savedHead.getId());
            id.setItemDescription(item.getItemDescription());

            ProjectAppendItemBody body = new ProjectAppendItemBody();
            body.setId(id);
            body.setUnitPrice(item.getUnitPrice());
            body.setCreatedBy(userId);
            body.setCreatedDate(now);
            projectAppentItemBodyRepository.save(body);
        }
    }


}
