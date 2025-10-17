package com.ems.bpm_comparisontable.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProjectItemId implements Serializable {

    @Column(name = "project_id")
    private int projectId;

    private int sequence;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectItemId that = (ProjectItemId) o;
        return projectId == that.projectId && sequence == that.sequence;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, sequence);
    }
}
