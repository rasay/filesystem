package com.ross.filesystem;

import com.ross.filesystem.exceptions.IllegalFileSystemOperationException;
import com.ross.filesystem.exceptions.PathNotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public abstract class ContainerEntity extends Entity {

    private Map<String, Entity> children = new HashMap<>();

    @Override
    public int getSize() {
        return getChildren().values().stream().map(c -> c.getSize()).reduce(0, (a, b) -> a + b);
    }

    /***
     * List all of the children of this container for display purposes.
     * @return
     */
    public List<String> listAllElements() {
        String path = this.getPath();
        if (children.isEmpty())
            return Arrays.asList(path);

        return children.values()
                .stream()
                .map(c -> (c instanceof ContainerEntity) ?
                        ((ContainerEntity)c).listAllElements()
                        : Arrays.asList(c.getPath()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /***
     * Locate the file system entity for the given path.
     * @param path
     * @return
     * @throws PathNotFoundException
     * @throws IllegalFileSystemOperationException
     */
    protected Entity findEntity(String path)
        throws PathNotFoundException, IllegalFileSystemOperationException {

        Entity entity = null;

        if (Strings.isEmpty(path))
            return this;

        if (!path.contains("/")) {
            if (!getChildren().containsKey(path))
                throw new PathNotFoundException();

            entity = getChildren().get(path);
        } else {
            ContainerEntity nextSubFolder = findNextSubFolder(path);
            String newPath = (path.contains("/")) ? path.substring(path.indexOf('/')+1) : "";
            entity = nextSubFolder.findEntity(newPath);
        }
        return entity;
    }

    /***
     * Simple entity factory method.
     * @param type
     * @param name
     */
    protected void instantiateChild(EntityType type, String name) {
        Entity entity = null;

        switch (type) {
            case DRIVE:
                entity = new Drive(name);
                break;
            case TEXT_FILE:
                entity = new TextFile(name);
                break;
            case ZIP_FILE:
                entity = new ZipFile(name);
                break;
            case FOLDER:
                entity = new Folder(name);
                break;
        }
        entity.setParent(this);
        getChildren().put(name, entity);
     }

    private ContainerEntity findNextSubFolder(String path)
            throws PathNotFoundException, IllegalFileSystemOperationException {

        String key = (path.contains("/")) ? path.substring(0, path.indexOf('/')) : path;
        if (!children.containsKey(key))
            throw new PathNotFoundException();

        Entity nextSubFolder = children.get(key);
        if (!(nextSubFolder instanceof ContainerEntity))
            throw new IllegalFileSystemOperationException();

        return (ContainerEntity)nextSubFolder;
    }
}
