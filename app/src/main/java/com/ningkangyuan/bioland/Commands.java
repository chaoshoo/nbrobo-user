package com.ningkangyuan.bioland;

import android.text.format.Time;

public class Commands {

	private Time time = null;

	/**
	 *  " + getResources().getString(R.string.Commands_java_1)
	 */
	public final static byte CMD_HEAD = 0x5A;
	/**
	 *  " + getResources().getString(R.string.Commands_java_2)
	 */
	public final static byte CMD_CATEGORY_ONE = 0x01;
	public final static byte CMD_CATEGORY_TWO = 0x02;
	public final static byte CMD_CATEGORY_THReE = 0x03;
	public final static byte CMD_CATEGORY_FOUR = 0x04;
	public final static byte CMD_CATEGORY_FIVE = 0x05;
	public final static byte CMD_CATEGORY_SIX = 0x06;
	/**
	 *  " + getResources().getString(R.string.Commands_java_3)
	 */
	public final static byte CMD_LENGTH_TEN = 0x0A;
	public final static byte CMD_LENGTH_ELEVEN = 0x0B;
	public final static byte CMD_LENGTH_TWELVE = 0x0C;
	public final static byte CMD_LENGTH_THIRTEEN = 0x0D;
	public final static byte CMD_LENGTHY_FOURTEEN = 0x0E;
	public final static byte CMD_LENGTH_FIFTEEN = 0x0F;
	public final static byte CMD_SIXTEEN_THIRTEEN = 0x10;

	/**
	 *  " + getResources().getString(R.string.DeviceSelectAcitivity_java_6)
	 */

	/**
	 *  " + getResources().getString(R.string.DeviceSelectAcitivity_java_3)
	 */
	byte[] cmdData = new byte[] {};

	public byte[] getSystemdate(byte cmdStart, byte cmdLength, byte cmdSort) {
		byte cmdCheck = 0;

		time = new Time();
		time.setToNow();
		cmdData = new byte[cmdLength];
		cmdData[0] = (byte) cmdStart;
		cmdData[1] = (byte) cmdLength;
		cmdData[2] = (byte) cmdSort;
		cmdData[3] = (byte) (time.year - 2000);
		cmdData[4] = (byte) ((byte) time.month + 1);
		cmdData[5] = (byte) time.monthDay;
		cmdData[6] = (byte) time.hour;
		cmdData[7] = (byte) time.minute;
		for (int i = 0; i < cmdData.length; i++) {
			cmdCheck += cmdData[i];
		}
		cmdData[8] = cmdCheck;
		cmdData[9] = 0;

		return cmdData;
	}
}