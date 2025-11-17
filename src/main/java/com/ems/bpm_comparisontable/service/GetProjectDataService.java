package com.ems.bpm_comparisontable.service;

import com.ems.bpm_comparisontable.model.*;
import com.ems.bpm_comparisontable.pojos.*;
import com.ems.bpm_comparisontable.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GetProjectDataService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectItemRepository projectItemRepository;

    @Autowired
    private ProjectAppentItemHeadRepository projectAppentItemHeadRepository;

    @Autowired
    private ProjectAppentItemBodyRepository projectAppentItemBodyRepository;

    @Autowired
    private ProjectContractorRepository projectContractorRepository;

    @Autowired
    private ContractorContractItemRepository contractorContractItemRepository;



    public Optional<ReturnProjectData> getProjectAllData(int projectId) {
        Optional<Project> optProject = projectRepository.findById(projectId);
        if(optProject.isPresent()) {
            ReturnProjectData  returnProjectData = new ReturnProjectData();

            returnProjectData.loadProject(optProject.get());

            //專案項目
            List<ProjectItem> projectItems = projectItemRepository.findByIdProjectIdOrderByIdSequenceAsc(projectId);
            List<LinkedList<String>> bodyDetail = new LinkedList<>();
            for(ProjectItem item: projectItems) {
                LinkedList<String> detail = new LinkedList<>();
                detail.add(item.getItem());//項 次
                detail.add(item.getItemDescription()); //項 目 及 說 明
                detail.add(item.getUnit()); //單 位
                detail.add(item.getQuantity()); //數 量
                detail.add(item.getUnitPrice()); //單 價
                detail.add(item.getComplexPrice()); //複 價
                detail.add(item.getRemarks()); //備 註
                bodyDetail.add(detail);
            }
            returnProjectData.setBodyDetail(bodyDetail);

            //追加工項-Head
            returnProjectData.setAppendItemHeads(projectAppentItemHeadRepository.findAllByProjectIdAndEnabled(projectId, true).stream()
                    .map(a -> new ReturnProjectAppendItemHead(a.getId(), a.getCreatedBy(), a.getCreatedDate()))
                    .collect(Collectors.toList()));
            for(ReturnProjectAppendItemHead head: returnProjectData.getAppendItemHeads()) {
                Map<String, ProjectAppendItemBody> mapBody = projectAppentItemBodyRepository.findAllByIdHeadId(head.getHeadId()).stream()
                        .collect(Collectors.toMap(ProjectAppendItemBody::getIdItemDescription, Function.identity()));
                System.out.println("size:"+mapBody.keySet().size());
                for(LinkedList<String> list: returnProjectData.getBodyDetail()) {
                    ProjectAppendItemBody body = mapBody.get(list.get(1));
                    if(body != null) {
                        list.add(body.getIdItemDescription());
                        list.add(body.getUnitPrice());
                        mapBody.remove(body.getIdItemDescription());
                    } else {
                        list.add("");
                        list.add("");
                    }
                }
                System.out.println("剩下的size:"+mapBody.keySet().size());
                // 將不是原本業主契約的工項加入
                int size = returnProjectData.getBodyDetail().getFirst().size();
                for(ProjectAppendItemBody body: mapBody.values()) {
                    LinkedList<String> list = new LinkedList<>();
                    list.add("AppendItem");
                    list.add(body.getIdItemDescription());
                    for(int i=0; i<size-2; i++) {
                        list.add("");
                    }
                    returnProjectData.getBodyDetail().add(list);
                }
            }

            //內部請款

            System.out.println("projectId:"+projectId);
            //包商請款
            returnProjectData.setContractors(projectContractorRepository.getAllContractorsOfProject(projectId).stream()
                    .map(a -> new ReturnProjectContractor(a.getId(), a.getName()))
                    .collect(Collectors.toList()));
            //包商合約項目
            Map<Integer, List<ReturnProjectContractorContractItem>> returnContractorContractItemMap = new HashMap<>();
            for(ReturnProjectContractor contractor : returnProjectData.getContractors()) {
                returnContractorContractItemMap.put(
                        contractor.getId(),
                        contractorContractItemRepository
                                .getContractorContractItem(projectId, contractor.getId()).stream()
                                .map(ReturnProjectContractorContractItem::new)
                                .collect(Collectors.toList())
                );
            }












            return Optional.of(returnProjectData);
        }

        return Optional.empty();
    }
}
