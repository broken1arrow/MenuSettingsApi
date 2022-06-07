package org.brokenarrow.library.menusettings.settings;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.getPLUGIN;


/**
 * GetCollections all files inside resorurses inside a folder and create if it not exist.
 */

public abstract class AllYamlFilesInFolder {

	private final String folderName;
	private final String fileName;
	private final boolean shallGenerateFiles;
	private FileConfiguration customConfig;
	private String extension;
	private File customConfigFile;
	private final Plugin plugin = getPLUGIN();

	public AllYamlFilesInFolder(String folderName, boolean shallGenerateFiles) {
		this(folderName, "", shallGenerateFiles);
	}

	public AllYamlFilesInFolder(String folderName, String filename, boolean shallGenerateFiles) {
		if (this.plugin == null)
			throw new RuntimeException("You have not set the plugin, becuse it is null");
		this.folderName = folderName;
		this.fileName = filename;
		this.shallGenerateFiles = shallGenerateFiles;
	}

	public abstract void saveDataToFile(File file);

	protected abstract void loadSettingsFromYaml(File file);

	public FileConfiguration getCustomConfig() {
		return customConfig;
	}

	public File getCustomConfigFile() {
		return customConfigFile;
	}

	public String getExtension() {
		if (this.extension == null) {
			return "yml";
		} else {
			String extension = this.extension;
			if (extension.startsWith("."))
				extension = extension.substring(1);
			return extension;
		}
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public void reload() {
		if (customConfigFile == null) {
			File[] files = getAllFiles();
			for (File file : files) {
				customConfigFile = file;
				customConfig = YamlConfiguration.loadConfiguration(file);
				try {
					customConfig.load(file);
					loadSettingsFromYaml(file);
				} catch (IOException | InvalidConfigurationException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("file reload(   dsddddd ");
			try {
				for (File file : getYamlFiles(getName())) {
					if (file == null) continue;
					if (!file.exists()) {
						this.plugin.saveResource(file.getName(), false);
					}
					customConfig.load(file);
					loadSettingsFromYaml(file);
				}
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	public void save() {
		save(null);
	}

	public boolean removeFile(String filename, String extension) {
		if (extension.startsWith("."))
			extension = extension.substring(1);

		final File dataFolder = new File(getParent(), filename + "." + extension);
		return dataFolder.delete();
	}

	public void save(String fileToSave) {
		final File dataFolder = new File(getParent());
		final File[] dataFolders = dataFolder.listFiles();
		if (dataFolder.exists() && dataFolders != null) {
			if (fileToSave != null) {
				if (!checkFolderExist(fileToSave, dataFolders)) {
					final File newDataFolder = new File(getParent(), fileToSave + ".yml");
					try {
						newDataFolder.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						saveDataToFile(newDataFolder);
					}
				} else {
					for (File file : dataFolders) {
						if (getFileName(file.getName()).equals(fileToSave)) {
							saveDataToFile(file);
							return;
						}
					}
				}
			} else
				for (File file : dataFolders) {
					saveDataToFile(file);
				}
		}
	}

	public String getName() {
		if (this.folderName != null && !this.folderName.isEmpty())
			return this.folderName;
		else
			return this.fileName;
	}

	public String getFolderName() {
		return folderName;
	}

	public File[] getAllFiles() {
		Set<String> map = new HashSet<>();
		List<String> filenamesFromDir = null;
		try {
			filenamesFromDir = getFilenamesForDirnameFromCP(getName());

		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		File[] files = getYamlFiles(getName());

		if (this.shallGenerateFiles) {

			if (filenamesFromDir == null) return null;

			if (files != null && files.length > 0) {
				for (File file : files) {
					map.add(file.getName().replace(".yml", ""));
				}

				for (String file : filenamesFromDir) {
					if (map.contains(getFileName(file))) {

						File outFile = new File(getParent(), file);
						if (!outFile.exists())
							this.plugin.saveResource(file, false);
					}
				}
			} else {
				for (String file : filenamesFromDir) {
					File outFile = new File(getParent(), file);
					if (!outFile.exists())
						this.plugin.saveResource(file, false);
				}
				files = getYamlFiles(getName());
			}
		}

		return files;
	}

	public List<String> getFiles() {
		List<String> filenamesFromDir = null;
		try {
			filenamesFromDir = getFilenamesForDirnameFromCP(getName());
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		assert filenamesFromDir != null;

		return new ArrayList<>(filenamesFromDir);
	}

	public boolean checkFolderExist(String fileToSave, File[] dataFolders) {
		if (fileToSave != null)
			for (File file : dataFolders) {
				String fileName = getFileName(file.getName());
				if (fileName.equals(fileToSave))
					return true;
			}
		return false;
	}

	public String getParent() {
		if (this.fileName != null && !this.fileName.isEmpty())
			return this.plugin.getDataFolder().getPath();
		else
			return this.plugin.getDataFolder() + "/" + this.folderName;
	}

	public File[] getYamlFiles(String directory) {

		if (this.fileName != null && !this.fileName.isEmpty() && directory.equals(this.fileName))
			return new File(this.plugin.getDataFolder() + "").listFiles(file -> !file.isDirectory() && file.getName().equals(this.fileName));

		final File dataFolder = new File(this.plugin.getDataFolder(), directory);
		if (!dataFolder.exists() && !directory.isEmpty())
			dataFolder.mkdirs();

		return dataFolder.listFiles(file -> !file.isDirectory() && file.getName().endsWith("." + getExtension()));
	}

	public String getFileName(String path) {
		Valid.checkBoolean(path != null && !path.isEmpty(), "The given path must not be empty!");
		int pos;

		if (path.lastIndexOf("/") == -1)
			pos = path.lastIndexOf("\\");
		else
			pos = path.lastIndexOf("/");

		if (pos > 0)
			path = path.substring(pos + 1);

		pos = path.lastIndexOf(".");

		if (pos > 0)
			path = path.substring(0, pos);
		return path;
	}

	public List<String> getFilenamesForDirnameFromCP(String directoryName) throws URISyntaxException, IOException {
		List<String> filenames = new ArrayList<>();

		URL url = this.plugin.getClass().getClassLoader().getResource(directoryName);


		System.out.println("url.getProtocol()  ttttttttt" + url);
		if (url != null) {
			if (url.getProtocol().equals("file")) {
				File file = Paths.get(url.toURI()).toFile();
				if (file != null) {
					File[] files = file.listFiles();
					if (files != null) {
						for (File filename : files) {
							filenames.add(filename.toString());
						}
					}
				}
			} else if (url.getProtocol().equals("jar")) {

				String dirname = this.fileName != null && !this.fileName.isEmpty() ? directoryName : directoryName + "/";
				String path = url.getPath();
				String jarPath = path.substring(5, path.indexOf("!"));
				try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))) {
					Enumeration<JarEntry> entries = jar.entries();
					while (entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						//System.out.println("name " + name + "entry " + entry + jarPath);
						if (this.fileName != null && !this.fileName.isEmpty() && name.startsWith(this.fileName)) {
							filenames.add(name);
						} else if (name.startsWith(dirname) && !dirname.equals(name)) {
							URL resource = this.plugin.getClass().getClassLoader().getResource(name);
							if (resource != null) {
								filenames.add(name);
							} else
								this.plugin.getLogger().warning("Missing files in plugins/" + this.plugin + ".jar/" + directoryName + "/, contact the author of " + this.plugin.getName() + ".");
						}
					}
				}
			}
		}
		return filenames;
	}


	private static class Valid extends RuntimeException {
		public static void checkBoolean(boolean b, String s) {
			if (!b)
				throw new CatchExceptions(s);
		}

		private static class CatchExceptions extends RuntimeException {
			public CatchExceptions(String message) {
				super(message);
			}
		}
	}
}