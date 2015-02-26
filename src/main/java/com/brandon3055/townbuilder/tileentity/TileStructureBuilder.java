package com.brandon3055.townbuilder.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

/**
 * Created by Brandon on 22/02/2015.
 */
public class TileStructureBuilder extends TileEntity {

	public String schematic = "";
	public boolean showPosition = false;
	public boolean copyAir = false;
	public int xOffset = 0;
	public int yOffset = 0;
	public int zOffset = 0;

	public int xSize = -1;
	public int ySize = -1;
	public int zSize = -1;

	public int signRotation = 0;

	public int keyCode = 0;

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}


	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("Schematic", schematic);
		compound.setBoolean("ShowPosition", showPosition);
		compound.setBoolean("CopyAir", copyAir);
		compound.setInteger("XOffset", xOffset);
		compound.setInteger("YOffset", yOffset);
		compound.setInteger("ZOffset", zOffset);
		compound.setInteger("XSize", xSize);
		compound.setInteger("YSize", ySize);
		compound.setInteger("ZSize", zSize);
		compound.setInteger("KeyCode", keyCode);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		schematic = compound.getString("Schematic");
		xOffset = compound.getInteger("XOffset");
		yOffset = compound.getInteger("YOffset");
		zOffset = compound.getInteger("ZOffset");
		xSize = compound.getInteger("XSize");
		ySize = compound.getInteger("YSize");
		zSize = compound.getInteger("ZSize");
		keyCode = compound.getInteger("KeyCode");
		showPosition = compound.getBoolean("ShowPosition");
		copyAir = compound.getBoolean("CopyAir");
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
