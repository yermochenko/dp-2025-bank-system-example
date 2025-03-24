package model.repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class TsvFileReader implements AutoCloseable {
	private final BufferedReader br;

	public TsvFileReader(String filename) throws FileNotFoundException {
		br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
	}

	public List<String> readLine() throws IOException {
		String line = br.readLine();
		List<String> list = null;
		if(line != null) {
			list = Arrays.asList(line.split("\t"));
		}
		return list;
	}

	@Override
	public void close() throws IOException {
		br.close();
	}
}
