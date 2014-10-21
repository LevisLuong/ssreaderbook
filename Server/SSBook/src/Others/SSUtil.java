package Others;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

public class SSUtil {
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static String md5(String input) {

		String md5 = null;

		if (null == input)
			return null;

		try {

			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			// Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}

	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param output
	 *            zip file output folder
	 */
	public static String unZipIt(String zipFile, String outputFolder) {
		String arr = "";
		byte[] buffer = new byte[1024];

		try {

			// create output directory is not exists
			File folder = new File(outputFolder);
			try {
				FileUtils.cleanDirectory(folder);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			if (!folder.exists()) {
				folder.mkdir();
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(
					new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = null;
			while ((ze = zis.getNextEntry()) != null) {
				String fileName = ze.getName();
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1,
						fileName.length());
				System.out.println("file unzip: " + fileName);
				if (fileName.endsWith("jpg") || fileName.endsWith("jpeg")
						|| fileName.endsWith("JPEG")
						|| fileName.endsWith("png") || fileName.endsWith("JPG")
						|| fileName.endsWith("PNG")) {
					File newFile = new File(outputFolder + File.separator
							+ fileName);
					System.out.println("file path unzip : "
							+ newFile.getAbsoluteFile());

					// create all non exists folders
					// else you will hit FileNotFoundException for compressed
					// folder
					// new File(newFile.getParent()).mkdirs();
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
					if (arr.equals("")) {
						arr = fileName;
					} else {
						arr = arr + "," + fileName;
					}

				}
			}

			zis.closeEntry();
			zis.close();

			System.out.println("Done");
			// Delete zip file
			File f = new File(zipFile);
			f.delete();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return arr;
	}

	public String convertToChart(List<Integer> lst) {
		// {
		// "cols": [
		// {"id":"","label":"Topping","pattern":"","type":"string"},
		// {"id":"","label":"Slices","pattern":"","type":"number"}
		// ],
		// "rows": [
		// {"c":[{"v":"Mushrooms","f":null},{"v":3,"f":null}]},
		// {"c":[{"v":"Onions","f":null},{"v":1,"f":null}]},
		// {"c":[{"v":"Olives","f":null},{"v":1,"f":null}]},
		// {"c":[{"v":"Zucchini","f":null},{"v":1,"f":null}]},
		// {"c":[{"v":"Pepperoni","f":null},{"v":2,"f":null}]}
		// ]
		// }
		String result = "{\"cols\": [{\"id\":\"\",\"label\":\"Topping\",\"pattern\":\"\",\"type\":\"string\"}, {\"id\":\"\",\"label\":\"Slices\",\"pattern\":\"\",\"type\":\"number\"} ],\"rows\": [";
		for (int i = 0; i < lst.size(); i++) {
			String row = String
					.format("{\"c\":[{\"v\":\"%s\",\"f\":null},{\"v\":%d,\"f\":null}]},",
							(i + 1) + "", lst.get(i));
			result = result + row;
		}
		result = result + "]}";
		System.out.println("convert to char : " + result);
		return result;
	}
}
