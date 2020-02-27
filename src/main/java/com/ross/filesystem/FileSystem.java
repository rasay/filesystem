package com.ross.filesystem;

import com.ross.filesystem.exceptions.IllegalFileSystemOperationException;
import com.ross.filesystem.exceptions.NotATextFileException;
import com.ross.filesystem.exceptions.PathAlreadyExistsException;
import com.ross.filesystem.exceptions.PathNotFoundException;

public class FileSystem extends ContainerEntity {

    public FileSystem(String name) {
        setName(name);
        setType(EntityType.DRIVE);
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean canAddChildOfType(EntityType type) {
        return type == EntityType.DRIVE;
    }

    /***
     * Create a new entity.
     * @param type
     * @param name
     * @param path
     * @throws PathNotFoundException
     * @throws PathAlreadyExistsException
     * @throws IllegalFileSystemOperationException
     */
    public void create(EntityType type, String name, String path)
            throws PathNotFoundException, PathAlreadyExistsException, IllegalFileSystemOperationException {

        Entity parentEntity = this.findEntity(path);
        if (!parentEntity.canAddChildOfType(type))
            throw new IllegalFileSystemOperationException();

        ContainerEntity parentContainer = (ContainerEntity)parentEntity;
        if (parentContainer.getChildren().containsKey(name))
            throw new PathAlreadyExistsException();

        parentContainer.instantiateChild(type, name);
    }

    /***
     * Delete an existing entity
     * @param path
     * @throws PathNotFoundException
     */
    public void delete(String path) throws PathNotFoundException, IllegalFileSystemOperationException {

        Entity entity = this.findEntity(path);
        entity.getParent().getChildren().remove(entity.getName());
    }

    /***
     * Change parent directory of source entity.
     * @param sourcePath
     * @param destinationPath
     * @throws PathNotFoundException
     * @throws PathAlreadyExistsException
     * @throws IllegalFileSystemOperationException
     */
    public void move(String sourcePath, String destinationPath)
            throws PathNotFoundException, PathAlreadyExistsException, IllegalFileSystemOperationException {

        Entity sourceEntity = findEntity(sourcePath);
        Entity destinationEntity = findEntity(destinationPath);
        if (!(destinationEntity instanceof ContainerEntity))
            throw new IllegalFileSystemOperationException();

        ContainerEntity newContainer = (ContainerEntity)destinationEntity;
        if (newContainer.getChildren().containsKey(sourceEntity.getName()))
            throw new PathAlreadyExistsException();

        sourceEntity.getParent().getChildren().remove(sourceEntity.getName());
        newContainer.getChildren().put(sourceEntity.getName(), sourceEntity);
        sourceEntity.setParent(newContainer);
    }

    /***
     * Changes the content of a text file.
     * @param path
     * @param contents
     * @throws PathNotFoundException
     * @throws NotATextFileException
     */
    public void writeToFile(String path, String contents)
            throws PathNotFoundException, NotATextFileException, IllegalFileSystemOperationException {

        Entity textFile = findEntity(path);
        if (textFile.getType() != EntityType.TEXT_FILE)
            throw new NotATextFileException();

        ((TextFile)textFile).setContents(contents);
    }

    public String get(String path)
            throws PathNotFoundException, NotATextFileException, IllegalFileSystemOperationException {

        Entity textFile = findEntity(path);
        if (textFile.getType() != EntityType.TEXT_FILE)
            throw new NotATextFileException();

        return ((TextFile)textFile).getContents();
    }
}
