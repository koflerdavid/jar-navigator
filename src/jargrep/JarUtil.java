package jargrep;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {

	public static Set<String> getClassesInJarFile(File file) throws IOException {
		JarFile jf = new JarFile(file);

		TreeSet<String> classes = new TreeSet<String>();

		Enumeration<JarEntry> e = jf.entries();
		for (; e.hasMoreElements();) {
			JarEntry je = e.nextElement();
			String name = je.getName();

			if (!je.isDirectory() && name.endsWith(".class")) {
				classes.add(name.substring(0, name.length() - 6).replace('/',
						'.'));
			}
		}

		return classes;
	}
}
