package model.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TsvFileWriter implements AutoCloseable {
	private final BufferedWriter bw;

	public TsvFileWriter(String filename) throws FileNotFoundException {
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8));
	}

	public void writeLine(List<String> line) throws IOException {
		bw.write(String.join("\t", line));
		bw.newLine();
	}

	@Override
	public void close() throws IOException {
		bw.close();
	}
}
