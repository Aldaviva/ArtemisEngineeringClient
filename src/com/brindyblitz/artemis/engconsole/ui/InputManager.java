package com.brindyblitz.artemis.engconsole.ui;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import com.brindyblitz.artemis.engconsole.config.ConfigurationLoader;
import com.brindyblitz.artemis.engconsole.config.InputMapping;

import net.dhleong.acl.enums.ShipSystem;

public class InputManager {
    public Map<ShipSystem, InputMapping> mappings = new HashMap<ShipSystem, InputMapping>();

    private static final boolean DBG_PRINT_MAPPINGS = false;

    public InputManager() {
        this.mappings = new ConfigurationLoader().getInputConfiguration();

        if (DBG_PRINT_MAPPINGS && mappings.size() > 0) {
            System.out.println("Custom key bindings loaded:");
            for (ShipSystem s : mappings.keySet()) {
                System.out.println("\t" + s + ": +" + mappings.get(s).increaseKeyStr + " / -" + mappings.get(s).decreaseKeyStr);
            }
        }

        fillEmptyMappingsWithDefaults();
    }

    private void fillEmptyMappingsWithDefaults() {
        for (ShipSystem system : ShipSystem.values()) {
            InputMapping mapping = this.mappings.get(system);
            if (mapping == null) {
                switch (system) {
                    case BEAMS:
                        mapping = new InputMapping(system, KeyEvent.VK_Q, KeyEvent.VK_A);
                        break;

                    case TORPEDOES:
                        mapping = new InputMapping(system, KeyEvent.VK_W, KeyEvent.VK_S);
                        break;

                    case SENSORS:
                        mapping = new InputMapping(system, KeyEvent.VK_E, KeyEvent.VK_D);
                        break;

                    case MANEUVERING:
                        mapping = new InputMapping(system, KeyEvent.VK_R, KeyEvent.VK_F);
                        break;

                    case IMPULSE:
                        mapping = new InputMapping(system, KeyEvent.VK_T, KeyEvent.VK_G);
                        break;

                    case WARP_JUMP_DRIVE:
                        mapping = new InputMapping(system, KeyEvent.VK_Y, KeyEvent.VK_H);
                        break;

                    case FORE_SHIELDS:
                        mapping = new InputMapping(system, KeyEvent.VK_U, KeyEvent.VK_J);
                        break;

                    case AFT_SHIELDS:
                        mapping = new InputMapping(system, KeyEvent.VK_I, KeyEvent.VK_K);
                        break;
                }

                this.mappings.put(system, mapping);
            }
        }
    }
}
