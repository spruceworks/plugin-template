package dev.spruceworks.template.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.spruceworks.template.TemplatePlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;

/**
 * /papertemplate — root admin command, registered through Paper's Brigadier
 * lifecycle API so it survives /reload and datapack reloads automatically.
 */
public final class TemplateCommand {

    private final TemplatePlugin plugin;

    private TemplateCommand(TemplatePlugin plugin) {
        this.plugin = plugin;
    }

    public static void register(TemplatePlugin plugin) {
        TemplateCommand command = new TemplateCommand(plugin);
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->
                event.registrar().register(command.build(), "PaperTemplate admin command"));
    }

    private LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("papertemplate")
                .requires(source -> source.getSender().hasPermission("papertemplate.admin"))
                .then(Commands.literal("reload").executes(this::reload))
                .build();
    }

    private int reload(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        boolean success = this.plugin.configManager().reload();
        this.plugin.messages().send(sender, success ? "reload-success" : "reload-failed");
        return Command.SINGLE_SUCCESS;
    }
}
