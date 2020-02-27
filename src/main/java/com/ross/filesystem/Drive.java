package com.ross.filesystem;

import lombok.Data;

@Data
public class Drive extends ContainerEntity {

    public Drive(String name) {
        setName(name);
        setType(EntityType.DRIVE);
    }

    @Override
    public String getPath() {
        return getName();
    }

    @Override
    public boolean canAddChildOfType(EntityType type) {
        return type != EntityType.FILESYSTEM && type != EntityType.DRIVE;
    }
}
