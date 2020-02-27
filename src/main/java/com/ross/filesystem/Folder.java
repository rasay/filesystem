package com.ross.filesystem;

import lombok.Data;

@Data
public class Folder extends ContainerEntity {

    public Folder(String name) {
        setType(EntityType.FOLDER);
        setName(name);
    }

    @Override
    public boolean canAddChildOfType(EntityType type) {
        return type != EntityType.FILESYSTEM && type != EntityType.DRIVE;
    }
}
