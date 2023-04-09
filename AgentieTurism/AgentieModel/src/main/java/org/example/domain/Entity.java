package org.example.domain;

import java.io.Serializable;

public abstract class Entity<ID> implements Serializable {
    private static final long serialVersionID = 7331115341259248461L;
    private ID id;
    public ID getId() {return id;}
    public void setId(ID id) {this.id = id;}
}
