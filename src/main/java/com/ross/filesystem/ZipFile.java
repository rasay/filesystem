package com.ross.filesystem;

import lombok.Data;

@Data
public class ZipFile extends ContainerEntity {

    public ZipFile(String name) {
        setType(EntityType.ZIP_FILE);
        setName(name);
    }

    @Override
    public int getSize() {
        return super.getSize() / 2;
    }

    @Override
    public boolean canAddChildOfType(EntityType type) {
        return type != EntityType.DRIVE && type != EntityType.FILESYSTEM;
    }
}
