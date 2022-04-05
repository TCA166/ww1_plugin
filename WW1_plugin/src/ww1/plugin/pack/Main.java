package ww1.plugin.pack;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
    // Fired when plugin is first enabled
	@Override
    public void onEnable() {
    	getServer().getPluginManager().registerEvents(new myListener(), this);
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    }
    

}

