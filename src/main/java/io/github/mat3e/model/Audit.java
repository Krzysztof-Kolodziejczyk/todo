package io.github.mat3e.model;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
public class Audit {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    // above annotation makes that function will invoke just before saving to database
    @PrePersist
    void prePersist() {
        createdOn = LocalDateTime.now();
    }

    // above annotation makes that function will invoke while database is updating
    @PreUpdate
    void preMerge() {
        updatedOn = LocalDateTime.now();
    }
}
