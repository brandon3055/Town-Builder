package com.brandon3055.townbuilder.tileentity;

import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;
import com.brandon3055.brandonscore.lib.datamanager.ManagedString;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by Brandon on 22/02/2015.
 */
public class TileStructureBuilder extends TileBCBase {

    public ManagedString schematic = register("schematic", new ManagedString("")).saveToTile().syncViaTile().finish();
    public ManagedBool showPosition = register("showPosition", new ManagedBool(false)).saveToTile().syncViaTile().finish();
    public ManagedBool copyAir = register("copyAir", new ManagedBool(false)).saveToTile().syncViaTile().finish();
    public ManagedInt xOffset = register("xOffset", new ManagedInt(0)).saveToTile().syncViaTile().finish();
    public ManagedInt yOffset = register("yOffset", new ManagedInt(0)).saveToTile().syncViaTile().finish();
    public ManagedInt zOffset = register("zOffset", new ManagedInt(0)).saveToTile().syncViaTile().finish();

    public ManagedInt xSize = register("xSize", new ManagedInt(-1)).saveToTile().syncViaTile().finish();
    public ManagedInt ySize = register("ySize", new ManagedInt(-1)).saveToTile().syncViaTile().finish();
    public ManagedInt zSize = register("zSize", new ManagedInt(-1)).saveToTile().syncViaTile().finish();

    public ManagedInt signRotation = register("signRotation", new ManagedInt(0)).saveToTile().syncViaTile().finish();

    public ManagedInt keyCode = register("keyCode", new ManagedInt(0)).saveToTile().syncViaTile().finish();

    @Override
    public void updateBlock() {
        super.updateBlock();
        dataManager.detectAndSendChanges();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}
