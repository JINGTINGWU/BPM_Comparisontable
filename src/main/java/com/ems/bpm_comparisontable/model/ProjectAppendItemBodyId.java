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
public class ProjectAppendItemBodyId implements Serializable {

    @Column(name = "head_id")
    private int headId;

    @Column(name = "item_description")
    private String itemDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectAppendItemBodyId that = (ProjectAppendItemBodyId) o;
        return headId == that.headId && itemDescription.equals(that.itemDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headId, itemDescription);
    }
}
