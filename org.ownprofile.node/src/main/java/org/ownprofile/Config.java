package org.ownprofile;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: testcase
public class Config {
	
	final Logger logger = LoggerFactory.getLogger(Config.class);
	
	public static final String DEFAULT_CONFIGFILE_NAME = "config.properties";
	public static final String PORT_PROPERTYKEY = "port";
	public static final String PERSISTENCE_UNIT_NAME_PROPERTYKEY = "persistence-unit.name";
	
	public static final int DEFAULT_PORT = 9080;
	public static final String DEFAULT_PERSISTENCE_UNIT_NAME = "ownProfilePU";
	
	private final String configSource;
	public final int port;
	public final String persistenceUnitName;
	
	public static Config parseDefaultConfigFile() {
		return parseConfigFile(DEFAULT_CONFIGFILE_NAME);
	}
	
	public static Config parseConfigFile(String fileName) {
		final Properties props = readProperties(fileName);
		
		final int port = Integer.parseInt(props.getProperty(PORT_PROPERTYKEY));
		final String persistenceUnitName = props.getProperty(PERSISTENCE_UNIT_NAME_PROPERTYKEY);
		
		final Config cfg = new Config(fileName, port, persistenceUnitName);
		return cfg;
	}
	
	private static Properties readProperties(String fileName) {
		final Properties props = new Properties();
		try {
			final FileReader reader = new FileReader(fileName);
			props.load(reader);
			return props;
			
		} catch (IOException e) {
			// TODO 
			throw new RuntimeException(e);
		}		
	}

	// TODO: builder?
	public Config(String configSource, int port, String persistenceUnitName) {
		this.configSource = checkNotNull(configSource);
		this.port = port;
		this.persistenceUnitName = checkNotNull(persistenceUnitName);
	}
	
	public void log() {
		logger.info("==[ config ]=================================");
		logger.info("  config-source: {}", configSource);
		logger.info("           port: {}", port);
		logger.info("persistenceUnit: {}", persistenceUnitName);
		logger.info("=============================================");
	}

	@Override
	public String toString() {
		return String.format("port: %d, persistenceUnit: %s", port, persistenceUnitName);
	}

}
