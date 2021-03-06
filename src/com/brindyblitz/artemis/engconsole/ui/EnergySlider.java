package com.brindyblitz.artemis.engconsole.ui;

import com.brindyblitz.artemis.engconsole.EngineeringConsoleManager;
import net.dhleong.acl.enums.ShipSystem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EnergySlider extends SystemStatusSlider {

	private static final long serialVersionUID = 1L;

    private static BufferedImage statusImageWithColor = null, statusImageWhite = null;

    protected static final int
            WIDGET_HEIGHT = 20,
            WIDGET_WIDTH = 400,
            SLIDER_WIDTH = WIDGET_WIDTH / 2,
            SLIDER_HEIGHT = WIDGET_HEIGHT;

    private static final Color ENERGY_COLOR = Color.BLUE;

    public EnergySlider(ShipSystem system, EngineeringConsoleManager engineeringConsoleManager) {
        super(system, engineeringConsoleManager, WIDGET_WIDTH, WIDGET_HEIGHT, SLIDER_WIDTH, SLIDER_HEIGHT);
    }

    @Override
    protected int getStatusPctInt() {
        // TODO: TESTME
        return (int)(100 * (this.engineeringConsoleManager.getTotalEnergyRemaining() / 1000f));
    }

    @Override
    protected void loadIcons() {
        try {
            if (statusImageWithColor == null) {
                statusImageWithColor = ImageIO.read(new File(System.getProperty("user.dir"), "assets/art/textures/status/energy/color.png"));
                statusImageWhite = ImageIO.read(new File(System.getProperty("user.dir"), "assets/art/textures/status/energy/white.png"));
            }
        } catch (IOException e) {
            System.err.println("Unable to locate system status icon(s)");
            e.printStackTrace(System.err);
            throw new RuntimeException();
        }
    }

    @Override
    protected BufferedImage getStatusImageWithColor() {
        return statusImageWithColor;
    }

    @Override
    protected BufferedImage getStatusImageWhite() {
        return statusImageWhite;
    }

    @Override
    protected Color getStatusColor() {
        return ENERGY_COLOR;
    }

    @Override
    protected float getFullHue() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected float getEmptyHue() {
        throw new UnsupportedOperationException();
    }
}
