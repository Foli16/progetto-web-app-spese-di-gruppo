package com.exercise.progetto_individuale.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class GroupUser extends BaseEntity
{
    boolean groupAdministrator;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private SpendingGroup spendingGroup;

    @OneToOne(fetch = FetchType.LAZY)
    private Participant participant;

    public GroupUser(boolean admin)
    {
        groupAdministrator = admin;
    }

    public void setParticipant(Participant p)
    {
        this.participant = p;
        p.setGroupUser(this);
    }
}
