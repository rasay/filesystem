package com.ross.filesystem;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Entity {

    private ContainerEntity parent;
    private EntityType type;
    private String name;

    public String getPath() {
        return getParent().getPath() + "/" + getName();
    }

    public abstract int getSize();
    public abstract boolean canAddChildOfType(EntityType type);
}
