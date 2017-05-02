package business.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import general.utility.SerializationUtils;

public class DataPersistence {
	private static final String DIRECTORY = "storage";
	private static final String FILE_EXTENSION = "dat";


	public static void store(String identifier, Object object) throws FileNotFoundException {
		final File file = getFile(identifier);
		// Make directories if required
		file.getParentFile().mkdirs();
		// Create output stream
		final FileOutputStream fos = new FileOutputStream(file);
		SerializationUtils.serialize(object, fos);
		try {
			fos.close();
		} catch (final IOException e) {
			// close failed, ignore
		}
	}

	public static Object get(String identifier) throws FileNotFoundException {
		// Open file input stream using identifiers respective file
		final FileInputStream fis = new FileInputStream(getFile(identifier));
		// Get object from file input stream
		final Object object = SerializationUtils.deserialize(fis);
		try {
			fis.close();
		} catch (final IOException e) {
			// close failed, ignore
		}
		return object;
	}

	private static File getFile(String identifier) {
		return new File(String.format("%s/%s.%s", DIRECTORY, identifier, FILE_EXTENSION));
	}

}
