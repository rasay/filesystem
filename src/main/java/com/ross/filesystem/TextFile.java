package com.ross.filesystem;

import lombok.Data;

@Data
public class TextFile extends Entity {

    private String contents;

    public TextFile(String name) {
        setType(EntityType.TEXT_FILE);
        setName(name);
    }

    @Override
    public int getSize() {
        return contents.length();
    }

    @Override
    public boolean canAddChildOfType(EntityType type) {
        return false;
    }
}
