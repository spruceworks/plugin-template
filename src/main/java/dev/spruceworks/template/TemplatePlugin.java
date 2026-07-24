package dev.spruceworks.template;

import dev.spruceworks.template.command.TemplateCommand;
import dev.spruceworks.template.config.ConfigManager;
import dev.spruceworks.template.config.Messages;
import dev.spruceworks.template.util.SchedulerAdapter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class TemplatePlugin extends JavaPlugin {

    /** bStats service id — create one for your plugin at https://bstats.org and put it here. */
    private static final int BSTATS_SERVICE_ID = 0;

    private ConfigManager configManager;
    private Messages messages;
    private SchedulerAdapter scheduler;
    private Metrics metrics;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.configManager.load();
        this.messages = new Messages(this.configManager);
        this.scheduler = new SchedulerAdapter(this);

        TemplateCommand.register(this);

        if (BSTATS_SERVICE_ID > 0 && this.configManager.config().getBoolean("metrics", true)) {
            this.metrics = new Metrics(this, BSTATS_SERVICE_ID);
        }

        if (this.configManager.config().getBoolean("debug", false)) {
            getSLF4JLogger().info("Debug mode is enabled.");
        }
    }

    @Override
    public void onDisable() {
        if (this.metrics != null) {
            this.metrics.shutdown();
        }
    }

    public ConfigManager configManager() {
        return this.configManager;
    }

    public Messages messages() {
        return this.messages;
    }

    public SchedulerAdapter scheduler() {
        return this.scheduler;
    }
}
