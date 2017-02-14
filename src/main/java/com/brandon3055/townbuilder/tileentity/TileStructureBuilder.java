package com.brandon3055.townbuilder.tileentity;

import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.network.wrappers.SyncableBool;
import com.brandon3055.brandonscore.network.wrappers.SyncableInt;
import com.brandon3055.brandonscore.network.wrappers.SyncableString;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by Brandon on 22/02/2015.
 */
public class TileStructureBuilder extends TileBCBase {

	public SyncableString schematic = new SyncableString("", true, false);
	public SyncableBool showPosition = new SyncableBool(false, true, false);
	public SyncableBool copyAir = new SyncableBool(false, true, false);
	public SyncableInt xOffset = new SyncableInt(0, true, false);
	public SyncableInt yOffset = new SyncableInt(0, true, false);
	public SyncableInt zOffset = new SyncableInt(0, true, false);

	public SyncableInt xSize = new SyncableInt(-1, true, false);
	public SyncableInt ySize = new SyncableInt(-1, true, false);
	public SyncableInt zSize = new SyncableInt(-1, true, false);

	public SyncableInt signRotation = new SyncableInt(0, true, false);

	public SyncableInt keyCode = new SyncableInt(0, true, false);

	public TileStructureBuilder() {
		registerSyncableObject(schematic);
		registerSyncableObject(showPosition);
		registerSyncableObject(copyAir);
		registerSyncableObject(xOffset);
		registerSyncableObject(yOffset);
		registerSyncableObject(zOffset);
		registerSyncableObject(xSize);
		registerSyncableObject(ySize);
		registerSyncableObject(zSize);
		registerSyncableObject(signRotation);
		registerSyncableObject(keyCode);
	}

	@Override
	public void updateBlock() {
		super.updateBlock();
		detectAndSendChanges();
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
