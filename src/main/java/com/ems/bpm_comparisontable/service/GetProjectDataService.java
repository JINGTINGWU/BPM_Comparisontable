package com.ems.bpm_comparisontable.service;

import com.ems.bpm_comparisontable.model.Contractor;
import com.ems.bpm_comparisontable.model.Project;
import com.ems.bpm_comparisontable.model.ProjectItem;
import com.ems.bpm_comparisontable.pojos.ReturnContractorContractItem;
import com.ems.bpm_comparisontable.pojos.ReturnProjectAllData;
import com.ems.bpm_comparisontable.pojos.ReturnProjectData;
import com.ems.bpm_comparisontable.repository.*;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetProjectDataService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectItemRepository projectItemRepository;

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private ContractorContractItemRepository contractorContractItemRepository;

    @Autowired
    private ProjectAppentItemRepository projectAppentItemRepository;

    public Optional<ReturnProjectData> getProjectAllData(int projectId) {
        Optional<Project> optProject = projectRepository.findById(projectId);
        if(optProject.isPresent()) {
            ReturnProjectData  returnProjectData = new ReturnProjectData();

            returnProjectData.loadProject(optProject.get());

            //專案項目
            List<ProjectItem> projectItems = projectItemRepository.findByIdProjectIdOrderByIdSequenceAsc(projectId);

            //包商
            List<Contractor> contractors = contractorRepository.getAllContractorsOfProject(projectId);

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


            return Optional.of(returnProjectData);
        }

        return Optional.empty();
    }
}
