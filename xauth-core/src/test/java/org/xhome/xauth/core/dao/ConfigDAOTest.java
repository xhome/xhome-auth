package org.xhome.xauth.core.dao;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Config;
import org.xhome.xauth.core.AbstractTest;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Nov 14, 201312:50:10 AM
 * @describe
 */
public class ConfigDAOTest extends AbstractTest {

	private ConfigDAO configDAO;

	public ConfigDAOTest() {
		configDAO = context.getBean("configDAO", ConfigDAO.class);
	}

	@Test
	public void testAddConfig() {
		logger.debug("test add config");

		Config config = new Config(Config.CONFIG_COMMON, "common_test", "test");
		config.setOwner(1L);
		config.setModifier(1L);

		// configDAO.addConfig(config);

	}

	@Test
	public void testUpdateConfig() {
		logger.debug("test update config");

		Config config = configDAO.queryConfigById(1L);
		printConfig(config);
		config.setVersion((short) 3);

		configDAO.updateConfig(config);

		config = configDAO.queryConfigById(1L);
		printConfig(config);
	}

	@Test
	public void testQueryConfig() {
		logger.debug("test query config by id");

		Config config = configDAO.queryConfigById(1L);
		printConfig(config);

		logger.debug("test query config by item");

		config = configDAO.queryConfigByItem("common_test");
		printConfig(config);

		QueryBase query = new QueryBase();
		query.addParameter("name", "A");
		List<Config> configs = configDAO.queryConfigs(query);
		printConfig(configs);

	}

}
