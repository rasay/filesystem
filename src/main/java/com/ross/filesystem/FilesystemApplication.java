package com.ross.filesystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
public class FilesystemApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FilesystemApplication.class, args);
	}


	/***
	 * Really simple console app to experiment with file system.
	 * mvn spring-boot:run
	 * @param args
	 */
	@Override
	public void run(String... args) {
		FileSystem fs = new FileSystem("system");
		BufferedReader reader =
				new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println("\nOptions are:\ntouch [drive|folder|zip|text] [name] [path]\nrm [path]\nmv [src path] [dest path]\nget [path]\nput [path] [contents]\n");
			try {
				String line = reader.readLine();
				String argList[] = line.split(" ");
				if ("touch".equals(argList[0])) {
					fs.create(stringToEntityType(argList[1]), argList[2], ("b".equals(argList[3])) ? "" : argList[3]);
				} else if ("rm".equals(argList[0])) {
					fs.delete(argList[1]);
				} else if ("mv".equals(argList[0])) {
					fs.move(argList[1], argList[2]);
				} else if ("get".equals(argList[0])) {
					System.out.println(fs.get(argList[1]));
				} else if ("put".equals(argList[0])) {
					fs.writeToFile(argList[1], argList[2]);
				}
				System.out.println("\n*******************");
				fs.listAllElements().stream().forEach(s -> System.out.println(s));
				System.out.println("*******************");
			} catch (Exception e) {
				System.out.printf("ERROR: %s\n", e);
				e.printStackTrace();
			}
		}
	}

	private static EntityType stringToEntityType(String s) {
		if ("drive".equals(s))
			return EntityType.DRIVE;

		if ("folder".equals(s))
			return EntityType.FOLDER;

		if ("text".equals(s))
			return EntityType.TEXT_FILE;

		if ("zip".equals(s))
			return EntityType.ZIP_FILE;

		return EntityType.TEXT_FILE;
	}
}
