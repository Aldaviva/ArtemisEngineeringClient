package com.brindyblitz.artemis.engconsole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.brindyblitz.artemis.protocol.NonShittyShipSystemGrid;
import com.brindyblitz.artemis.utils.EventEmitter;

import net.dhleong.acl.enums.ShipSystem;
import net.dhleong.acl.protocol.core.eng.EngGridUpdatePacket.DamconStatus;
import net.dhleong.acl.util.GridCoord;
import net.dhleong.acl.util.ShipSystemGrid;
import net.dhleong.acl.vesseldata.Vessel;
import net.dhleong.acl.vesseldata.VesselData;
import net.dhleong.acl.vesseldata.VesselNode;
import net.dhleong.acl.vesseldata.VesselNodeConnection;
import net.dhleong.acl.world.Artemis;

public abstract class BaseEngineeringConsoleManager implements EngineeringConsoleManager {

	protected EventEmitter<Events> eventEmitter;
	private ShipSystemGrid shipSystemGrid;
	private List<VesselNode> grid;
	private Map<GridCoord, VesselNode> gridIndex;
	private List<VesselNodeConnection> gridConnections;
	
	
	public BaseEngineeringConsoleManager() {
		
		this.eventEmitter = new EventEmitter<>();
		NonShittyShipSystemGrid shipSystemGrid = new NonShittyShipSystemGrid();
		this.grid = new ArrayList<>();
		this.gridIndex = new HashMap<>();
		Vessel vessel = VesselData.get().getVessel(0);
		Iterator<VesselNode> nodeIterator = vessel.getInternals().nodeIterator();
		while (nodeIterator.hasNext()) {
			VesselNode node = nodeIterator.next();
			grid.add(node);
			gridIndex.put(node.getGridCoord(), node);
			if (node.getSystem() != null) {
				shipSystemGrid.addNode(node.getSystem(), node.getGridCoord());				
			}
		}
		
		this.gridConnections = new ArrayList<>();
		Iterator<VesselNodeConnection> connectionIterator = vessel.getInternals().connectionIterator();
		while(connectionIterator.hasNext()) {
			gridConnections.add(connectionIterator.next());
		}
		
		this.shipSystemGrid = shipSystemGrid;
	}
	
	protected void fireChange() {
		this.eventEmitter.emit(Events.CHANGE);
	}
	
	@Override
	public void setSystemEnergyAllocated(ShipSystem system, int amount) {
		updateSystemEnergyAllocated(system, Math.min(Artemis.MAX_ENERGY_ALLOCATION_PERCENT, Math.max(0, amount)));
	}
	
	@Override
	public void incrementSystemEnergyAllocated(ShipSystem system, int amount) {
		updateSystemEnergyAllocated(system, Math.min(Artemis.MAX_ENERGY_ALLOCATION_PERCENT, Math.max(0,this.getSystemEnergyAllocated(system) + amount)));
	}
	
	@Override
	public List<VesselNode> getGrid() {
		return this.grid;
	}
	
	@Override
	public List<VesselNodeConnection> getGridConnections() {
		return this.gridConnections;
	}
	
	@Override
	public List<EnhancedDamconStatus> getDamconTeams() {
		List<EnhancedDamconStatus> result = new ArrayList<>();
		for (DamconStatus damconStatus : this.getRawDamconStatus()) {
			VesselNode positionNode = gridIndex.get(damconStatus.getPosition());
			VesselNode goalNode = gridIndex.get(damconStatus.getGoal());
			// Turns out progress = 0 means at GOAL node, while 1 = at position node.
			// So its "percentage distance remaining" I guess. Would have expected opposite.
			float x = (positionNode.getX() - goalNode.getX()) * damconStatus.getProgress() + goalNode.getX();
			float y = (positionNode.getY() - goalNode.getY()) * damconStatus.getProgress() + goalNode.getY();
			float z = (positionNode.getZ() - goalNode.getZ()) * damconStatus.getProgress() + goalNode.getZ();
			
			result.add(new EnhancedDamconStatus(damconStatus, x, y, z));
		}
		return result;
	}
	
	protected abstract List<DamconStatus> getRawDamconStatus();
	
	protected ShipSystemGrid getShipSystemGrid() {
		return shipSystemGrid;
	}
	
	protected abstract void updateSystemEnergyAllocated(ShipSystem system, int amount);
	
	@Override
	public void setSystemCoolantAllocated(ShipSystem system, int amount) {
		updateSystemCoolantAllocated(system, Math.max(0, Math.min(amount, getTotalShipCoolant())));
		
	}
	
	@Override
	public void incrementSystemCoolantAllocated(ShipSystem system, int amount) {
		updateSystemCoolantAllocated(system, Math.max(0, this.getSystemCoolantAllocated(system) + Math.min(amount, getTotalCoolantRemaining())));
	}
	
	protected abstract void updateSystemCoolantAllocated(ShipSystem system, int amount);
	
	@Override
	public void resetEnergy() {
		for (ShipSystem system: ShipSystem.values()) {
			updateSystemEnergyAllocated(system, 100);
		}
	}
	
	@Override
	public void resetCoolant() {
		for (ShipSystem system: ShipSystem.values()) {
			updateSystemCoolantAllocated(system, 0);
		}
	}
	
	@Override
	public void onEvent(Events event, Runnable listener) {
		eventEmitter.on(event, listener);
	}

}
