package com.ross.filesystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DriveTests {

	@Test
	void testGetSize() {
		Drive drive = new Drive("bogusDrive");
		drive.getChildren().put("bogusEntity1", generateAbstractChild(100));
		drive.getChildren().put("bogusEntity2", generateAbstractChild(200));

		assertEquals(300, drive.getSize());
	}

	@Test
	void testGetPath() {
		Drive drive = new Drive("testdrive");
		assertEquals("testdrive", drive.getPath());
	}

	private Entity generateAbstractChild(int size) {
		return new Entity() {
			@Override
			public String getPath() {
				return null;
			}

			@Override
			public int getSize() {
				return size;
			}

			@Override
			public boolean canAddChildOfType(EntityType type) {
				return false;
			}
		};
	}
}
