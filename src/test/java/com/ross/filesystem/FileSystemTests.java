package com.ross.filesystem;

import com.ross.filesystem.exceptions.IllegalFileSystemOperationException;
import com.ross.filesystem.exceptions.PathAlreadyExistsException;
import com.ross.filesystem.exceptions.PathNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileSystemTests {


    @Test
    void testAddChildSimple() throws Exception {
        FileSystem fs = new FileSystem("testPath");
        fs.create(EntityType.DRIVE, "drive", "");
        fs.create(EntityType.FOLDER, "newfolder", "drive");
        List<String> entities = fs.listAllElements();

        assertEquals(1, entities.size());
        assertEquals("drive/newfolder", entities.get(0));
    }

    @Test
    void testAddNestedChildren() throws Exception {
        FileSystem fs = new FileSystem("testPath");
        fs.create(EntityType.DRIVE, "drive", "");
        fs.create(EntityType.FOLDER, "newfolder", "drive");
        fs.create(EntityType.TEXT_FILE, "newText", "drive/newfolder");
        List<String> entities = fs.listAllElements();

        assertEquals(1, entities.size());
        assertEquals("drive/newfolder/newText", entities.get(0));
    }

    /***
     * Try to add folder to a text file.
     * @throws Exception
     */
    @Test
    void testAddIlligalFileSystemOperation() throws Exception {

        FileSystem fs = new FileSystem("testPath");
        fs.create(EntityType.DRIVE, "drive", "");
        fs.create(EntityType.TEXT_FILE, "newText", "drive");

        Assertions.assertThrows(IllegalFileSystemOperationException.class, () -> {
            fs.create( EntityType.FOLDER, "newfolder","drive/newText");
        });
    }

    /***
     * Attempt to add text file to non-existing path.
     * @throws Exception
     */
    @Test
    void testAddPathNotFound() throws Exception {

        FileSystem fs = new FileSystem("testPath");
        fs.create(EntityType.DRIVE, "drive", "");
        fs.create(EntityType.FOLDER, "newfolder", "drive");

        Assertions.assertThrows(PathNotFoundException.class, () -> {
            fs.create(EntityType.TEXT_FILE, "newText", "bogusfolder");
        });
    }

    /***
     * Attempt to create text file twice.
     * @throws Exception
     */
    @Test
    void testAddPathAlreadyExists() throws Exception {

        FileSystem fs = new FileSystem("testPath");
        fs.create(EntityType.DRIVE, "drive", "");
        fs.create(EntityType.FOLDER, "newfolder", "drive");
        fs.create(EntityType.FOLDER, "subfolder", "drive/newfolder");
        fs.create(EntityType.TEXT_FILE, "newText", "drive/newfolder/subfolder");

        Assertions.assertThrows(PathAlreadyExistsException.class, () -> {
            fs.create(EntityType.TEXT_FILE, "newText", "drive/newfolder/subfolder");
        });
    }

    @Test
    void testToAddNonDriveChild() {
        FileSystem fs = new FileSystem("system");
        Assertions.assertThrows(IllegalFileSystemOperationException.class, () -> {
            fs.create(EntityType.FOLDER, "bogus", "");
        });
    }

    @Test
    void testDelete() throws Exception {
        FileSystem fs = new FileSystem("system");
        fs.create(EntityType.DRIVE, "drive", "");
        fs.create(EntityType.FOLDER, "folder", "drive");
        fs.create(EntityType.FOLDER, "subfolder1", "drive/folder");
        fs.create(EntityType.FOLDER, "subfolder2", "drive/folder/subfolder1");
        fs.create(EntityType.TEXT_FILE, "text1", "drive/folder/subfolder1/subfolder2");
        fs.create(EntityType.TEXT_FILE, "text2", "drive/folder/subfolder1/subfolder2");
        assertEquals(2, fs.listAllElements().size());

        fs.delete("drive/folder/subfolder1");
        assertEquals(1, fs.listAllElements().size());
        assertEquals("drive/folder", fs.listAllElements().get(0));
    }

    @Test
    void testMove() throws Exception {
        FileSystem fs = new FileSystem("system");
        fs.create(EntityType.DRIVE, "drive", "");
        fs.create(EntityType.FOLDER, "folder", "drive");
        fs.create(EntityType.FOLDER, "subfolder1", "drive/folder");
        fs.create(EntityType.FOLDER, "subfolder2", "drive/folder/subfolder1");
        fs.create(EntityType.TEXT_FILE, "text1", "drive/folder/subfolder1/subfolder2");
        fs.create(EntityType.TEXT_FILE, "text2", "drive/folder/subfolder1/subfolder2");
        Set<String> fsSet = new HashSet<>(fs.listAllElements());
        assertFalse(fsSet.contains("drive/folder/subfolder1/text2"));

        fs.move("drive/folder/subfolder1/subfolder2/text2", "drive/folder/subfolder1");
        fsSet = new HashSet<>(fs.listAllElements());
        assertTrue(fsSet.contains("drive/folder/subfolder1/text2"));
    }

    @Test
    void testSetAndGet() throws Exception {
        FileSystem fs = new FileSystem("system");
        fs.create(EntityType.DRIVE, "drive", "");
        fs.create(EntityType.FOLDER, "folder", "drive");
        fs.create(EntityType.TEXT_FILE, "text", "drive/folder");
        fs.writeToFile("drive/folder/text", "this really works");
        assertEquals("this really works", fs.get("drive/folder/text"));
    }
}
