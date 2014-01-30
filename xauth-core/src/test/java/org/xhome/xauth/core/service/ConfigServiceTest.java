package org.xhome.xauth.core.service;

import java.util.List;

import org.junit.Test;
import org.xhome.db.query.QueryBase;
import org.xhome.xauth.Config;
import org.xhome.xauth.core.AbstractTest;
import org.xhome.xauth.core.listener.TestConfigManageListener;

/**
 * @project xauth-core
 * @author jhat
 * @email cpf624@126.com
 * @date Nov 14, 20131:02:39 AM
 * @describe
 */
public class ConfigServiceTest extends AbstractTest {

	private ConfigService configService;

	public ConfigServiceTest() {
		configService = context.getBean(ConfigServiceImpl.class);
		oper.setId(101L);

		((ConfigServiceImpl) configService)
				.registerConfigManageListener(new TestConfigManageListener());
	}

	@Test
	public void testAddConfig() {
		Config config = new Config("common_test", "abc");
		config.setOwner(1L);
		config.setModifier(1L);
		// configService.addConfig(oper, config);
	}

	@Test
	public void testGetConfig() {
		Config config = configService.getConfig(oper, 1L);
		printConfig(config);

		config = configService.getConfig(oper, "TestConfig");
		printConfig(config);
	}

	@Test
	public void testCountConfigs() {
		QueryBase query = new QueryBase();
		query.addParameter("item", "common_test");
		logger.info("{}", configService.countConfigs(oper, query));
	}

	@Test
	public void testGetConfigs() {
		QueryBase query = new QueryBase();
		query.addParameter("name", "test");
		List<Config> configs = configService.getConfigs(oper, query);
		printConfig(configs);
	}

	@Test
	public void testIsConfigUpdateable() {
		Config config = configService.getConfig(oper, 1L);
		logger.info("{}", configService.isConfigUpdateable(oper, config));
	}

	@Test
	public void testIsConfigDeleteable() {
		Config config = configService.getConfig(oper, 1L);
		// logger.info("{}", configService.isConfigDeleteable(oper, config));
	}

	@Test
	public void testIsConfigLocked() {
		Config config = configService.getConfig(oper, 1L);
		// logger.info("{}", configService.isConfigLocked(oper, config));
	}

	@Test
	public void testLockConfig() {
		Config config = configService.getConfig(oper, 1L);
		// configService.lockConfig(oper, config);
	}

	@Test
	public void testUnlockConfig() {
		Config config = configService.getConfig(oper, 1L);
		// configService.unlockConfig(oper, config);
	}

	@Test
	public void testUpdateConfig() {
		Config config = configService.getConfig(oper, "common_test");
		config.setCategory(Config.CONFIG_XAUTH);
		config.setId(100L);
		// config.setVersion(11);
		int r = configService.updateConfig(oper, config);
		logger.info("result:" + r);
	}

	@Test
	public void testIsConfigExists() {
		logger.info("{}", configService.isConfigExists(oper, new Config(
				"common_test", "abc")));
	}

	@Test
	public void testRemoveConfig() {
		Config config = configService.getConfig(oper, "common_test");
		// configService.removeConfig(oper, config);
		// configService.deleteConfig(oper, config);
	}

}
