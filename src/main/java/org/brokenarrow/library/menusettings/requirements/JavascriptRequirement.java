package org.brokenarrow.library.menusettings.requirements;

import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.logging.Level;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.*;

public class JavascriptRequirement extends Requirement {
	private static final ScriptEngineManager engine = getEngineManager();
	private static final ScriptEngine scriptEngine = getScriptEngine();

	private final String expression;


	public JavascriptRequirement(String expresion) {
		this.expression = expresion;
	/*	if (engine == null && factory != null) {
			if (this.manager.isProvidedFor(ScriptEngineManager.class)) {
				RegisteredServiceProvider<ScriptEngineManager> provider = this.manager.getRegistration(ScriptEngineManager.class);
				if (provider == null) {
					getPLUGIN().getLogger().log(Level.WARNING, "ScriptEngineManager exist but registered service provider is null");
					return;
				}
				engine = provider.getProvider();
			} else {
				engine = new ScriptEngineManager();
				this.manager.register(ScriptEngineManager.class, engine, getPLUGIN(), ServicePriority.Highest);
			}
			engine.registerEngineName("javascript", this.factory);
			engine.put("BukkitServer", Bukkit.getServer());
		}*/
		if (engine == null)
			getPLUGIN().getLogger().log(Level.WARNING, "Script Engine Manager is null make sure you have install NashornPlus from https://github.com/broken1arrow/NashornPlusAPI/releases or add <' libraries: -org.openjdk.nashorn:nashorn-core:15.4 '> to your plugin.yml");
	}

	@Override
	boolean estimate(Player wiver) {
		if (engine == null || scriptEngine == null) {
			getPLUGIN().getLogger().log(Level.WARNING, scriptEngine == null ? "Script Engine" : "Script Engine Manager" + " is null make sure you have install NashornPlus from https://github.com/broken1arrow/NashornPlusAPI/releases or add <' libraries: -org.openjdk.nashorn:nashorn-core:15.4 '> to your plugin.yml");
			return false;
		}
		String exp = setPlaceholders(wiver, this.expression);
		try {
			engine.put("BukkitPlayer", wiver);
			Object result = scriptEngine.eval(exp);
			if (!(result instanceof Boolean)) {
				getPLUGIN().getLogger().log(Level.WARNING, "This script [" + this.expression + "] do not return boolean.");
				return false;
			} else {
				return (boolean) result;
			}
		} catch (ScriptException e) {
			e.printStackTrace();
			return false;
		}
	}
}
