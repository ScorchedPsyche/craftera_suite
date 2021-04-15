package modules.com.github.scorchedpsyche.craftera_suite.modules.task;

import com.github.scorchedpsyche.craftera_suite.modules.model.RunnableModel;
import modules.com.github.scorchedpsyche.craftera_suite.modules.main.SpectatorModeManager;

public class ProcessPlayersInSpectatorTask extends RunnableModel {

    private SpectatorModeManager spectatorModeManager;

    public ProcessPlayersInSpectatorTask(String prefix, String name, SpectatorModeManager spectatorModeManager) {
        super(prefix, name);
        this.spectatorModeManager = spectatorModeManager;
    }

    @Override
    public void run() {
        super.setAsRunning();
        spectatorModeManager.calculateSpectatorsDistanceToExecutingLocationAndTeleportBackIfNeeded();
    }
}
