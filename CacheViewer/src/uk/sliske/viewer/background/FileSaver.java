package uk.sliske.viewer.background;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileSaver {

	private PrintWriter	file;

	public FileSaver(String fileName, String path) {
		createFile(path + fileName);
	}

	public FileSaver(String fileName) {
		String directory = Constants.MODEL_PATH;
		File tempFile = new File(directory);
		tempFile.mkdirs();
		createFile(directory + fileName);
	}

	private final void createFile(String s) {

		try {
			file = new PrintWriter(s);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if (file != null) {
			System.out.println("file " + s + " created");
		}
	}

	public void append(String s) {
		file.append(s);
	}

	public void appendln(String s) {
		append(s);
		append("\n");
	}

	public void close() {
		file.close();

	}

}
