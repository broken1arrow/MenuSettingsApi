package org.brokenarrow.library.menusettings.requirements;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.logging.Level;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.*;

public class JavascriptRequirement extends Requirement {
	private static ScriptEngineManager engine;
	private final ScriptEngineFactory factory = getEngineFactory();
	private final ServicesManager manager = Bukkit.getServer().getServicesManager();

	private final String expression;


	public JavascriptRequirement(String expresion) {
		this.expression = expresion;
		if (engine == null && factory != null) {
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
		}
		if (factory == null)
			getPLUGIN().getLogger().log(Level.WARNING, "ScriptEngineFactory is null make sure you have install NashornPlus from github.You can´t run Javascript with out that plugin");
	}

	@Override
	boolean estimate(Player wiver) {
		if (factory == null) return false;
		String exp = setPlaceholders(wiver, this.expression);
		try {
			engine.put("BukkitPlayer", wiver);
			Object result = engine.getEngineByName("javascript").eval(exp);
			if (!(result instanceof Boolean)) {
				getPLUGIN().getLogger().log(Level.WARNING, "This scrpit [" + this.expression + "] do not return boolean.");
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
