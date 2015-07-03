package com.fortytwo.opent4c.tools;

import java.nio.ByteBuffer;

public class PakTypes {

	public static final short PAK_CLIENT_PlayerMove_N = 0x0001;
	public static final short PAK_CLIENT_PlayerMove_NE = 0x0002;
	public static final short PAK_CLIENT_PlayerMove_E = 0x0003;
	public static final short PAK_CLIENT_PlayerMove_SE = 0x0004;
	public static final short PAK_CLIENT_PlayerMove_S = 0x0005;
	public static final short PAK_CLIENT_PlayerMove_SW = 0x0006;
	public static final short PAK_CLIENT_PlayerMove_W = 0x0007;
	public static final short PAK_CLIENT_PlayerMove_NW = 0x0008;
	public static final short PAK_CLIENT_PlayerMove_Stop = 0x0009;
	public static final short PAK_CLIENT_KeepAlive = 0x000A;
	public static final short PAK_CLIENT_GetObject = 0x000B;
	public static final short PAK_CLIENT_DepositObject = 0x000C;
	public static final short PAK_CLIENT_PutPlayerInGame = 0x000D;
	public static final short PAK_CLIENT_Login = 0x000E;
	public static final short PAK_CLIENT_DeletePlayer = 0x000F;
	public static final short PAK_CLIENT_SendPeriphericObjects = 0x0010;
	public static final short PAK_CLIENT_Unknown17 = 0x0011;
	public static final short PAK_CLIENT_BackpackItems = 0x0012;
	public static final short PAK_CLIENT_EquippedItems = 0x0013;
	public static final short PAK_CLIENT_ExitGame = 0x0014;
	public static final short PAK_CLIENT_EquipObject = 0x0015;
	public static final short PAK_CLIENT_UnequipObject = 0x0016;
	public static final short PAK_CLIENT_UseObject = 0x0017;
	public static final short PAK_CLIENT_Attack = 0x0018;
	public static final short PAK_CLIENT_CreatePlayer = 0x0019;
	public static final short PAK_CLIENT_PersonalPCList = 0x001A;
	public static final short PAK_CLIENT_IndirectTalk = 0x001B;
	public static final short PAK_CLIENT_Shout = 0x001C;
	public static final short PAK_CLIENT_Page = 0x001D;
	public static final short PAK_CLIENT_DirectedTalk = 0x001E;
	public static final short PAK_CLIENT_Reroll = 0x001F;
	public static final short PAK_CLIENT_CastSpell = 0x0020;
	public static final short PAK_CLIENT_UpdateLife = 0x0021;
	public static final short PAK_CLIENT_BroadcastTextChange = 0x0022;
	public static final short PAK_CLIENT_UnitName = 0x0023;
	public static final short PAK_CLIENT_BreakConversation = 0x0024;
	public static final short PAK_CLIENT_Unknown37 = 0x0025;
	public static final short PAK_CLIENT_ReturnToMenu = 0x0026;
	public static final short PAK_CLIENT_PersonalSkills = 0x0027;
	public static final short PAK_CLIENT_TrainList = 0x0028;
	public static final short PAK_CLIENT_BuyList = 0x0029;
	public static final short PAK_CLIENT_UseSkill = 0x002A;
	public static final short PAK_CLIENT_PersonalStats = 0x002B;
	public static final short PAK_CLIENT_UpdateXP = 0x002C;
	public static final short PAK_CLIENT_UpdateTime = 0x002D;
	public static final short PAK_CLIENT_FromPreInGameToInGame = 0x002E;
	public static final short PAK_CLIENT_Unknown47 = 0x002F;
	public static final short PAK_CLIENT_EnterChannel = 0x0030;
	public static final short PAK_CLIENT_ChannelMessage = 0x0031;
	public static final short PAK_CLIENT_ChannelUsers = 0x0032;
	public static final short PAK_CLIENT_PlayerList = 0x0033;
	public static final short PAK_CLIENT_GetSkillStatPoints = 0x0034;
	public static final short PAK_CLIENT_UpdateGold = 0x0035;
	public static final short PAK_CLIENT_ViewGroundItemIndentContent = 0x0036;
	public static final short PAK_CLIENT_TeachList = 0x0037;
	public static final short PAK_CLIENT_SellList = 0x0038;
	public static final short PAK_CLIENT_Unknown57 = 0x0039;
	public static final short PAK_CLIENT_SendStatTrain = 0x003A;
	public static final short PAK_CLIENT_ItemName = 0x003B;
	public static final short PAK_CLIENT_GetNearItems = 0x003C;
	public static final short PAK_CLIENT_Unknown61 = 0x003D;
	public static final short PAK_CLIENT_PersonalSpellList = 0x003E;
	public static final short PAK_CLIENT_Unknown63 = 0x003F;
	public static final short PAK_CLIENT_Unknown64 = 0x0040;
	public static final short PAK_CLIENT_ServerInformation = 0x0041;
	public static final short PAK_CLIENT_MessageOfTheDay = 0x0042;
	public static final short PAK_CLIENT_Unknown67 = 0x0043;
	public static final short PAK_CLIENT_PuppetInformation = 0x0044;
	public static final short PAK_CLIENT_Unknown69 = 0x0045;
	public static final short PAK_CLIENT_Unknown70 = 0x0046;
	public static final short PAK_CLIENT_QueryUnitExistence = 0x0047;
	public static final short PAK_CLIENT_UseItemByAppearance = 0x0048;
	public static final short PAK_CLIENT_Unknown73 = 0x0049;
	public static final short PAK_CLIENT_RemoveFromChatterChannel = 0x004A;
	public static final short PAK_CLIENT_ChannelList = 0x004B;
	public static final short PAK_CLIENT_Unknown76 = 0x004C;
	public static final short PAK_CLIENT_Unknown77 = 0x004D;
	public static final short PAK_CLIENT_GroupInvite = 0x004E;
	public static final short PAK_CLIENT_GroupJoin = 0x004F;
	public static final short PAK_CLIENT_GroupLeave = 0x0050;
	public static final short PAK_CLIENT_GroupKick = 0x0051;
	public static final short PAK_CLIENT_Unknown82 = 0x0052;
	public static final short PAK_CLIENT_Unknown83 = 0x0053;
	public static final short PAK_CLIENT_Unknown84 = 0x0054;
	public static final short PAK_CLIENT_JunkItems = 0x0055;
	public static final short PAK_CLIENT_ToggleChatterListening = 0x0056;
	public static final short PAK_CLIENT_Unknown87 = 0x0057;
	public static final short PAK_CLIENT_ToggleGroupAutoSplit = 0x0058;
	public static final short PAK_CLIENT_TogglePages = 0x0059;
	public static final short PAK_CLIENT_PlayerExists = 0x005A;
	public static final short PAK_CLIENT_PatchServerInformation = 0x005B;
	public static final short PAK_CLIENT_Unknown92 = 0x005C;
	public static final short PAK_CLIENT_Rob = 0x005D;
	public static final short PAK_CLIENT_DispelRob = 0x005E;
	public static final short PAK_CLIENT_Unknown95 = 0x005F;
	public static final short PAK_CLIENT_Unknown96 = 0x0060;
	public static final short PAK_CLIENT_Unknown97 = 0x0061;
	public static final short PAK_CLIENT_Unknown98 = 0x0062;
	public static final short PAK_CLIENT_AuthenticateVersion = 0x0063;
	public static final short PAK_CLIENT_Unknown100 = 0x0064;
	public static final short PAK_CLIENT_Unknown101 = 0x0065;
	public static final short PAK_CLIENT_Unknown102 = 0x0066;
	public static final short PAK_CLIENT_Unknown103 = 0x0067;
	public static final short PAK_CLIENT_Unknown104 = 0x0068;
	public static final short PAK_CLIENT_Unknown105 = 0x0069;
	public static final short PAK_CLIENT_Unknown106 = 0x006A;
	public static final short PAK_CLIENT_DepositChest = 0x006B;
	public static final short PAK_CLIENT_WithdrawChest = 0x006C;
	public static final short PAK_CLIENT_Unknown109 = 0x006D;
	public static final short PAK_CLIENT_Unknown110 = 0x006E;
	public static final short PAK_CLIENT_Undefined01 = 0x006F;
	public static final short PAK_CLIENT_Undefined02 = 0x0070;
	public static final short PAK_CLIENT_Undefined03 = 0x0071;
	public static final short PAK_CLIENT_Undefined04 = 0x0072;
	public static final short PAK_CLIENT_Undefined05 = 0x0073;
	public static final short PAK_CLIENT_RequestExchange = 0x0074;
	public static final short PAK_CLIENT_Undefined06 = 0x0075;
	public static final short PAK_CLIENT_Undefined07 = 0x0076;
	public static final short PAK_CLIENT_Undefined08 = 0x0077;
	public static final short PAK_CLIENT_Undefined09 = 0x0078;
	public static final short PAK_CLIENT_Undefined10= 0x0079;
	public static final short PAK_CLIENT_ItemDescription = 0x007A;
	public static final short PAK_CLIENT_Undefined11 = 0x007B;
	public static final short PAK_CLIENT_Undefined12 = 0x00;
	public static final short PAK_CLIENT_Undefined13 = 0x00;
	public static final short PAK_CLIENT_Undefined14 = 0x00;
	public static final short PAK_CLIENT_Undefined15 = 0x00;
	public static final short PAK_CLIENT_Unknown10001 = 0x2711;
	public static final short PAK_CLIENT_Unknown10002 = 0x2712;
	public static final short PAK_CLIENT_Unknown10003 = 0x2713;
	public static final short PAK_CLIENT_Unknown10004 = 0x2714;
	public static final short PAK_CLIENT_NMS_DeathStatus = 0x00C8;
	public static final short PAK_CLIENT_NMS_DeathProgress = 0x00C9;
	public static final short PAK_CLIENT_NMS_DeathResurection = 0x00CA;	
	
	/**
	 * to be called only on decrypted paks
	 * @param isServerToClient
	 * @param pak
	 * @return
	 */
	public static String getTypeInfos(short type, boolean isServerToClient, ByteBuffer pak) {
		pak.rewind();
		if(isServerToClient)return getServerTypeInfos(type, pak);
		return getClientTypeInfos(type, pak);
	}
	
	private static short getInt8(ByteBuffer pak, StringBuilder sb, String name){
		short num = (short) (pak.get() & 0xFF);
		sb.append(name+" : "+num);
		sb.append(System.lineSeparator());
		return num;
	}
	
	private static int getInt16(ByteBuffer pak, StringBuilder sb, String name){
		int num = (int) pak.getShort() & 0xFFFF;
		sb.append(name+" : "+num);
		sb.append(System.lineSeparator());
		return num;
	}

	private static long getInt32(ByteBuffer pak, StringBuilder sb, String name){
		long num = (long) pak.getInt() & 0xFFFFFFFF;
		sb.append(name+" : "+num);
		sb.append(System.lineSeparator());
		return num;
	}
	
	private static long getInt64(ByteBuffer pak, StringBuilder sb, String name){
		long num = (long) pak.getLong() & 0xFFFFFFFFFFFFFFFFl;
		sb.append(name+" : "+num);
		sb.append(System.lineSeparator());
		return num;
	}
	
	private static void getString8(ByteBuffer pak, StringBuilder sb, String name){
		byte size = pak.get();
		sb.append(name + "("+size+") : ");
		byte[] text = new byte[size];
		pak.get(text,0,size);
		sb.append(new String(text));
		sb.append(System.lineSeparator());
	}
	
	private static void getString16(ByteBuffer pak, StringBuilder sb, String name){
		short size = pak.getShort();
		sb.append(name + "("+size+") : ");
		byte[] text = new byte[size];
		pak.get(text,0,size);
		sb.append(new String(text));
		sb.append(System.lineSeparator());
	}
	
	public static String getClientTypeInfos(short type, ByteBuffer pak) {
		StringBuilder sb = new StringBuilder();
		
		switch(type){
		case 0x0001 : sb.append("PAK_CLIENT_PlayerMove_N 0x0001 // player character wants to move north");
		break;
		case 0x0002 : sb.append("PAK_CLIENT_PlayerMove_NE 0x0002");
		break;
		case 0x0003 : sb.append("PAK_CLIENT_PlayerMove_E 0x0003 // player character wants to move east");
		break;
		case 0x0004 : sb.append("PAK_CLIENT_PlayerMove_SE 0x0004");
		break;
		case 0x0005 : sb.append("PAK_CLIENT_PlayerMove_S 0x0005 // player character wants to move south");
		break;
		case 0x0006 : sb.append("PAK_CLIENT_PlayerMove_SW 0x0006");
		break;
		case 0x0007 : sb.append("PAK_CLIENT_PlayerMove_W 0x0007 // player character wants to move west");
		break;
		case 0x0008 : sb.append("PAK_CLIENT_PlayerMove_NW 0x0008");
		break;
		case 0x0009 : sb.append("PAK_CLIENT_PlayerMove_Stop 0x0009 // player character stopped moving");
		break;
		case 0x000A : sb.append("PAK_CLIENT_KeepAlive 0x000A // Pong");
		break;
		case 0x000B : sb.append("PAK_CLIENT_GetObject 0x000B"+System.lineSeparator()+
				"   2   int16 x_coord; // object's X coordinate on map"+System.lineSeparator()+
				"   4   int16 y_coord; // object's Y coordinate on map"+System.lineSeparator()+
				"   6   int32 object_id; // object unit ID");
				getInt16(pak, sb, "x_coord");
				getInt16(pak, sb, "y_coord");
				getInt32(pak, sb, "object_ID");
		break;
		case 0x000C : sb.append("PAK_CLIENT_DepositObject 0x000C"+System.lineSeparator()+
				"   2   int16 x_coord; // object's X coordinate on map"+System.lineSeparator()+
				"   4   int16 y_coord; // object's Y coordinate on map"+System.lineSeparator()+
				"   6   int32 object_id; // object unit ID"+System.lineSeparator()+
				"   10   int32 item_count; // amount of same objects in this unit, if the objects are stacked");
				getInt16(pak, sb, "x_coord");
				getInt16(pak, sb, "y_coord");
				getInt32(pak, sb, "object_ID");
				getInt32(pak, sb, "item_count");
		break;
		case 0x000D : sb.append("PAK_CLIENT_PutPlayerInGame 0x000D"+System.lineSeparator()+
				"   2   string8 playername;"+System.lineSeparator());
				getString8(pak, sb, "Player Name");
		break;
		case 0x000E : sb.append("PAK_CLIENT_Login 0x000E"+System.lineSeparator()+
				"   2   string8 login;"+System.lineSeparator()+
				"   2+[1+S]   string8 password;"+System.lineSeparator()+
				"   2+[1+S]+[1+S]+2   int16 client_version_number;"+System.lineSeparator()+
				"   2+[1+S]+[1+S]+4   int16 unknown;"+System.lineSeparator());
				getString8(pak, sb, "Login");
				getString8(pak, sb, "Password");
				getInt16(pak, sb, "Client Version Number");
				getInt16(pak, sb, "Unknown");
		break;
		case 0x000F : sb.append("PAK_CLIENT_DeletePlayer 0x000F"+System.lineSeparator()+
				"   2   string8 playername;");
		break;
		case 0x0010 : sb.append("PAK_CLIENT_SendPeriphericObjects 0x0010"+System.lineSeparator()+
				"   2   int8 player_orientation; // player character's orientation (9: ignore orientation)"+System.lineSeparator()+
				"   3   int16 x_coord; // player character's X coordinates on map"+System.lineSeparator()+
				"   5   int16 y_coord; // player character's Y coordinates on map");
		break;
		case 0x0011 : sb.append("PAK_CLIENT_Unknown17 0x0011");
		break;
		case 0x0012 : sb.append("PAK_CLIENT_BackpackItems 0x0012"+System.lineSeparator()+
				"	2   int16 unknown;");
		break;
		case 0x0013 : sb.append("PAK_CLIENT_EquippedItems 0x0013");
		break;
		case 0x0014 : sb.append("PAK_CLIENT_ExitGame 0x0014");
		break;
		case 0x0015 : sb.append("PAK_CLIENT_EquipObject 0x0015"+System.lineSeparator()+
				"   2   int32 item_id;");
		break;
		case 0x0016 : sb.append("PAK_CLIENT_UnequipObject 0x0016"+System.lineSeparator()+
				"   2   int8 equipment_position;");
		break;
		case 0x0017 : sb.append("PAK_CLIENT_UseObject 0x0017"+System.lineSeparator()+
				"   2   int16 x_coord;"+System.lineSeparator()+
				"   4   int16 y_coord;"+System.lineSeparator()+
				"   6   int32 item_unit_id;");
		break;
		case 0x0018 : sb.append("PAK_CLIENT_Attack 0x0018"+System.lineSeparator()+
				"   2   int16 x_coord;"+System.lineSeparator()+
				"   4   int16 y_coord;"+System.lineSeparator()+
				"   6   int32 unit_id;");
		break;
		case 0x0019 : sb.append("PAK_CLIENT_CreatePlayer 0x0019"+System.lineSeparator()+
				"   2   int8 dexterity;"+System.lineSeparator()+
				"   3   int8 endurance;"+System.lineSeparator()+
				"   4   int8 intelligence;"+System.lineSeparator()+
				"   5   int8 strength;"+System.lineSeparator()+
				"   6   int8 wisdom;"+System.lineSeparator()+
				"   7   int8 unknown; // probably gender"+System.lineSeparator()+
				"   8   string8 playername;");
		break;
		case 0x001A : sb.append("PAK_CLIENT_PersonalPCList 0x001A");
		break;
		case 0x001B : sb.append("PAK_CLIENT_IndirectTalk 0x001B // local talk"+System.lineSeparator()+
				"   2   int32 unit_id; // player ID in database"+System.lineSeparator()+
				"   6   int8 unit_orientation; // usually 0 (means don't change current orientation)"+System.lineSeparator()+
				"   7   int32 text_color; // seems unused, usually 00 00 BE BE"+System.lineSeparator()+
				"   11   string16 message;"+System.lineSeparator());
				getInt32(pak, sb, "Unit ID");
				getInt8(pak, sb, "Unit Orientation");
				getInt32(pak, sb, "Text Color");
				getString16(pak, sb, "Message");
		break;
		case 0x001C : sb.append("PAK_CLIENT_Shout 0x001C // main channel talk"+System.lineSeparator()+
				"   2   string16 playername; // ignored (corrected by server if wrong)"+System.lineSeparator()+
				"   2+[2+S]   int32 text_color; // unused"+System.lineSeparator()+
				"   2+[2+S]+4   string16 message;");
				getString16(pak, sb, "Player Name");
				getInt32(pak, sb, "Text Color");
				getString16(pak, sb, "Message");
		break;
		case 0x001D : sb.append("PAK_CLIENT_Page 0x001D // whisp"+System.lineSeparator()+
				"   2   string16 target_playername;"+System.lineSeparator()+
				"   2+[2+S]   string16 message;");
		break;
		case 0x001E : sb.append("PAK_CLIENT_DirectedTalk 0x001E"+System.lineSeparator()+
				"   2   int16 target_x_coord;"+System.lineSeparator()+
				"   4   int16 target_y_coord;"+System.lineSeparator()+
				"   6   int32 target_unit_id;"+System.lineSeparator()+
				"   10   int8 orientation;"+System.lineSeparator()+
				"   11   int32 text_color; // unused"+System.lineSeparator()+
				"   15   string16 message;");
		break;
		case 0x001F : sb.append("PAK_CLIENT_Reroll 0x001F");
		break;
		case 0x0020 : sb.append("PAK_CLIENT_CastSpell 0x0020"+System.lineSeparator()+
				"   2   int16 spell_id;"+System.lineSeparator()+
				"   4   int16 target_x_coord;"+System.lineSeparator()+
				"   6   int16 target_y_coord;"+System.lineSeparator()+
				"   8   int32 target_unit_id;");
		break;
		case 0x0021 : sb.append("PAK_CLIENT_UpdateLife 0x0021");
		break;
		case 0x0022 : sb.append("PAK_CLIENT_BroadcastTextChange 0x0022"+System.lineSeparator()+
				"   2   int32 npc_unit_id;"+System.lineSeparator()+
				"   6   int16 start_line_number;");
		break;
		case 0x0023 : sb.append("PAK_CLIENT_UnitName 0x0023"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   int16 x_coord;"+System.lineSeparator()+
				"   8   int16 y_coord;");
		break;
		case 0x0024 : sb.append("PAK_CLIENT_BreakConversation 0x0024"+System.lineSeparator()+
				"   2   int32 npc_unit_id;"+System.lineSeparator()+
				"   6   int16 npc_x_position;"+System.lineSeparator()+
				"   8   int16 npc_y_position;");
		break;
		case 0x0025 : sb.append("PAK_CLIENT_Unknown37 0x0025");
		break;
		case 0x0026 : sb.append("PAK_CLIENT_ReturnToMenu 0x0026");
		break;
		case 0x0027 : sb.append("PAK_CLIENT_PersonalSkills 0x0027 // request to get the skill list in the stats page");
		break;
		case 0x0028 : sb.append("PAK_CLIENT_TrainList 0x0028"+System.lineSeparator()+
				"   2   int16 target_x_coord;"+System.lineSeparator()+
				"   4   int16 target_y_coord;"+System.lineSeparator()+
				"   6   int32 target_unit_id;"+System.lineSeparator()+
				"   10   int16 skill_count;"+System.lineSeparator()+
				"              for (i = 0; i < skill_count; i++)"+System.lineSeparator()+
				"              {"+System.lineSeparator()+
				"				12+i*4        int16 skill_id;"+System.lineSeparator()+
				"       		12+i*4+2      int16 points_count;"+System.lineSeparator()+
				"              }");
		break;
		case 0x0029 : sb.append("PAK_CLIENT_BuyList 0x0029"+System.lineSeparator()+
				"   2   int16 x_coord; // target"+System.lineSeparator()+
				"   4   int16 y_coord; // target"+System.lineSeparator()+
				"   6   int32 unit_id; // target"+System.lineSeparator()+
				"   10  int16 itemtype_count;"+System.lineSeparator()+
				"              for (i = 0; i < itemtype_count; i++)"+System.lineSeparator()+
				"              {"+System.lineSeparator()+
				"			   12+i*4        int16 object_id;"+System.lineSeparator()+
				"			   12+i*4+2      int16 item_count;"+System.lineSeparator()+
				"              }");
		break;
		case 0x002A : sb.append("PAK_CLIENT_UseSkill 0x002A // request to use a particular skill on a target"+System.lineSeparator()+
				"   2   int16 skill_id; // skill ID"+System.lineSeparator()+
				"   4   int16 target_x_coord; // target's X coordinate"+System.lineSeparator()+
				"   6   int16 target_y_coord; // target's Y coordinate"+System.lineSeparator()+
				"   8   int32 target_unit_id; // target unit ID (if player, player ID in database)");
		break;
		case 0x002B : sb.append("PAK_CLIENT_PersonalStats 0x002B // request to get the stats list (usually to display the stats page)");
		break;
		case 0x002C : sb.append("PAK_CLIENT_UpdateXP 0x002C");
		break;
		case 0x002D : sb.append("PAK_CLIENT_UpdateTime 0x002D");
		break;
		case 0x002E : sb.append("PAK_CLIENT_FromPreInGameToInGame 0x002E");
		break;
		case 0x002F : sb.append("PAK_CLIENT_Unknown47 0x002F // unused (probably obsolete)");
		break;
		case 0x0030 : sb.append("PAK_CLIENT_EnterChannel 0x0030"+System.lineSeparator()+
				"   2   string16 channelname;"+System.lineSeparator()+
				"   2+[2+S]   string16 password;"+System.lineSeparator());
				getString16(pak, sb, "Channel Name");
				getString16(pak, sb, "Password");
		break;
		case 0x0031 : sb.append("PAK_CLIENT_ChannelMessage 0x0031"+System.lineSeparator()+
				"   2   string16 channelname;"+System.lineSeparator()+
				"   2+[2+S]   string16 message;");
		break;
		case 0x0032 : sb.append("PAK_CLIENT_ChannelUsers 0x0032"+System.lineSeparator()+
				"   2   string16 channelname;");
		break;
		case 0x0033 : sb.append("PAK_CLIENT_PlayerList 0x0033");
		break;
		case 0x0034 : sb.append("PAK_CLIENT_GetSkillStatPoints 0x0034");
		break;
		case 0x0035 : sb.append("PAK_CLIENT_UpdateGold 0x0035");
		break;
		case 0x0036 : sb.append("PAK_CLIENT_ViewGroundItemIndentContent 0x0036");
		break;
		case 0x0037 : sb.append("PAK_CLIENT_TeachList 0x0037"+System.lineSeparator()+
				"   2   int16 target_x_coord;"+System.lineSeparator()+
				"   4   int16 target_y_coord;"+System.lineSeparator()+
				"   6   int32 target_unit_id;"+System.lineSeparator()+
				"   for (;;)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 skill_id;"+System.lineSeparator()+
				"   }");
		break;
		case 0x0038 : sb.append("PAK_CLIENT_SellList 0x0038"+System.lineSeparator()+
				"   2   int16 x_coord;"+System.lineSeparator()+
				"   4   int16 y_coord;"+System.lineSeparator()+
				"   6   int32 buyer_npc_id;"+System.lineSeparator()+
				"   for (;;)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      10   int32 item_unit_id;"+System.lineSeparator()+
				"      14   int32 quantity;"+System.lineSeparator()+
				"   }");
		break;
		case 0x0039 : sb.append("PAK_CLIENT_Unknown57 0x0039");
		break;
		case 0x003A : sb.append("PAK_CLIENT_SendStatTrain 0x003A"+System.lineSeparator()+
				"   2   int8 strength;"+System.lineSeparator()+
				"   3   int8 endurance;"+System.lineSeparator()+
				"   4   int8 dexterity;"+System.lineSeparator()+
				"   5   int8 willpower; // unused"+System.lineSeparator()+
				"   6   int8 wisdom;"+System.lineSeparator()+
				"   7   int8 intelligence;"+System.lineSeparator()+
				"   8   int8 luck; // unused");
		break;
		case 0x003B : sb.append("PAK_CLIENT_ItemName 0x003B"+System.lineSeparator()+
				"   2   int32 item_id;"+System.lineSeparator());
				getInt32(pak, sb, "Item ID");
		break;
		case 0x003C : sb.append("PAK_CLIENT_GetNearItems 0x003C");
		break;
		case 0x003D : sb.append("PAK_CLIENT_Unknown61 0x003D // unused (probably obsolete)");
		break;
		case 0x003E : sb.append("PAK_CLIENT_PersonalSpellList 0x003E"+System.lineSeparator()+
				"   2   int8 unknown; // 0x01 if god"+System.lineSeparator());
				getInt8(pak, sb, "Unknown (god?)");
		break;
		case 0x003F : sb.append("PAK_CLIENT_Unknown63 0x003F");
		break;
		case 0x0040 : sb.append("PAK_CLIENT_Unknown64 0x0040");
		break;
		case 0x0041 : sb.append("PAK_CLIENT_ServerInformation 0x0041");
		break;
		case 0x0042 : sb.append("PAK_CLIENT_MessageOfTheDay 0x0042");
		break;
		case 0x0043 : sb.append("PAK_CLIENT_Unknown67 0x0043");
		break;
		case 0x0044 : sb.append("PAK_CLIENT_PuppetInformation 0x0044"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   int16 x_coord;"+System.lineSeparator()+
				"   8   int16 y_coord;"+System.lineSeparator());
				getInt32(pak, sb, "Unit ID");
				getInt16(pak, sb, "X Coord");
				getInt16(pak, sb, "Y Coord");
		break;
		case 0x0045 : sb.append("PAK_CLIENT_Unknown69 0x0045");
		break;
		case 0x0046 : sb.append("PAK_CLIENT_Unknown70 0x0046");
		break;
		case 0x0047 : sb.append("PAK_CLIENT_QueryUnitExistence 0x0047"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   int16 x_coord;"+System.lineSeparator()+
				"   8   int16 y_coord;");
		break;
		case 0x0048 : sb.append("PAK_CLIENT_UseItemByAppearance 0x0048"+System.lineSeparator()+
				"   2   int16 object_id;");
		break;
		case 0x0049 : sb.append("PAK_CLIENT_Unknown73 0x0049");
		break;
		case 0x004A : sb.append("PAK_CLIENT_RemoveFromChatterChannel 0x004A"+System.lineSeparator()+
				"   2   string16 channelname;");
		break;
		case 0x004B : sb.append("PAK_CLIENT_ChannelList 0x004B");
		break;
		case 0x004C : sb.append("PAK_CLIENT_Unknown76 0x004C");
		break;
		case 0x004D : sb.append("PAK_CLIENT_Unknown77 0x004D");
		break;
		case 0x004E : sb.append("PAK_CLIENT_GroupInvite 0x004E"+System.lineSeparator()+
				"   2   int32 target_player_unit_id;"+System.lineSeparator()+
				"   6   int16 x_coord;"+System.lineSeparator()+
				"   8   int16 y_coord;");
		break;
		case 0x004F : sb.append("PAK_CLIENT_GroupJoin 0x004F");
		break;
		case 0x0050 : sb.append("PAK_CLIENT_GroupLeave 0x0050");
		break;
		case 0x0051 : sb.append("PAK_CLIENT_GroupKick 0x0051"+System.lineSeparator()+
				"   2   int32 player_unit_id;");
		break;
		case 0x0052 : sb.append("PAK_CLIENT_Unknown82 0x0052");
		break;
		case 0x0053 : sb.append("PAK_CLIENT_Unknown83 0x0053");
		break;
		case 0x0054 : sb.append("PAK_CLIENT_Unknown84 0x0054");
		break;
		case 0x0055 : sb.append("PAK_CLIENT_JunkItems 0x0055"+System.lineSeparator()+
				"   2   int32 item_id;"+System.lineSeparator()+
				"   6   int32 item_count;");
		break;
		case 0x0056 : sb.append("PAK_CLIENT_ToggleChatterListening 0x0056"+System.lineSeparator()+
				"   2   string16 channelname;"+System.lineSeparator()+
				"   [2+S]  int8 is_enabled; // 0 = channel listening disabled, 1 = channel listening enabled");
		break;
		case 0x0057 : sb.append("PAK_CLIENT_Unknown87 0x0057");
		break;
		case 0x0058 : sb.append("PAK_CLIENT_ToggleGroupAutoSplit 0x0058"+System.lineSeparator()+
				"   2   int8 unknown; // usually 0");
		break;
		case 0x0059 : sb.append("PAK_CLIENT_TogglePages 0x0059"+System.lineSeparator()+
				"   2   int8 status; // 0 = pages disabled, 1 = pages enabled"+System.lineSeparator());
				getInt8(pak, sb, "Status");
		break;
		case 0x005A : sb.append("PAK_CLIENT_PlayerExists 0x005A"+System.lineSeparator()+
				"   2   string16 playername;");
		break;
		case 0x005B : sb.append("PAK_CLIENT_PatchServerInformation 0x005B");
		break;
		case 0x005C : sb.append("PAK_CLIENT_Unknown92 0x005C");
		break;
		case 0x005D : sb.append("PAK_CLIENT_Rob 0x005D // request to rob a certain item in the victim's inventory"+System.lineSeparator()+
				"   2   int32 item_id; // ID of item to rob in the victim's inventory");
		break;
		case 0x005E : sb.append("PAK_CLIENT_DispelRob 0x005E");
		break;
		case 0x005F : sb.append("PAK_CLIENT_Unknown95 0x005F");
		break;
		case 0x0060 : sb.append("PAK_CLIENT_Unknown96 0x0060");
		break;
		case 0x0061 : sb.append("PAK_CLIENT_Unknown97 0x0061");
		break;
		case 0x0062 : sb.append("PAK_CLIENT_Unknown98 0x0062");
		break;
		case 0x0063 : sb.append("PAK_CLIENT_AuthenticateVersion 0x0063"+System.lineSeparator()+
				"   2   int32 version_number;"+System.lineSeparator());
				getInt32(pak, sb, "Version Number");
		break;
		case 0x0064 : sb.append("PAK_CLIENT_Unknown100 0x0064 // unregistered");
		break;
		case 0x0065 : sb.append("PAK_CLIENT_Unknown101 0x0065");
		break;
		case 0x0066 : sb.append("PAK_CLIENT_Unknown102 0x0066");
		break;
		case 0x0067 : sb.append("PAK_CLIENT_Unknown103 0x0067");
		break;
		case 0x0068 : sb.append("PAK_CLIENT_Unknown104 0x0068 // unregistered");
		break;
		case 0x0069 : sb.append("PAK_CLIENT_Unknown105 0x0069");
		break;
		case 0x006A : sb.append("PAK_CLIENT_Unknown106 0x006A // unregistered");
		break;
		case 0x006B : sb.append("PAK_CLIENT_DepositChest 0x006B"+System.lineSeparator()+
				"   2   int32 item_id;"+System.lineSeparator()+
				"   6   int32 item_count;");
		break;
		case 0x006C : sb.append("PAK_CLIENT_WithdrawChest 0x006C"+System.lineSeparator()+
				"   2   int32 item_id;"+System.lineSeparator()+
				"   6   int32 item_count;");
		break;
		case 0x006D : sb.append("PAK_CLIENT_Unknown109 0x006D // unregistered");
		break;
		case 0x006E : sb.append("PAK_CLIENT_Unknown110 0x006E // unregistered");
		break;
		case 0x006F : sb.append("PAK_CLIENT_Undefined 0x006F");
		break;
		case 0x0074 : sb.append("PAK_CLIENT_RequestExchange 0x0074"+System.lineSeparator()+
				"   2   int32 target_unit_id;"+System.lineSeparator()+
				"   6   int16 x_coord;"+System.lineSeparator()+
				"   8   int16 y_coord;");
		break;
		case 0x007A : sb.append("PAK_CLIENT_ItemDescription 0x007A // only in 1.50 and newer"+System.lineSeparator()+
				"   2   int32 unit_id;");
		break;
		case 0x2711 : sb.append("PAK_CLIENT_Unknown10001 0x2711 // unregistered");
		break;
		case 0x2712 : sb.append("PAK_CLIENT_Unknown10002 0x2712 // unregistered");
		break;
		case 0x2713 : sb.append("PAK_CLIENT_Unknown10003 0x2713 // unregistered");
		break;
		case 0x2714 : sb.append("PAK_CLIENT_Unknown10004 0x2714 // unregistered");
		break;
		//Pak pour les clients NMS uniquement

		case 0x00C8 : sb.append("PAK_CLIENT_DeathStatus 0x00C8 // Affichage de la mort en jeu");
		break;
		case 0x00C9 : sb.append("PAK_CLIENT_DeathProgress 0x00C9 // Affichage de la mort en jeu");
		break;
		case 0x00CA : sb.append("PAK_CLIENT_DeathResurection 0x00CA // Demande de la résurrection du personnage");
		break;
		}
		sb.append(System.lineSeparator());
		
		return sb.toString();
	}
	
	public static final short PAK_SERVER_UpdateCoordinates = 0x0001;
	public static final short PAK_SERVER_Undefined01 = 0x0002;
	public static final short PAK_SERVER_Undefined02 = 0x0003;
	public static final short PAK_SERVER_Undefined03 = 0x0004;
	public static final short PAK_SERVER_Undefined04 = 0x0005;
	public static final short PAK_SERVER_Undefined05 = 0x0006;
	public static final short PAK_SERVER_Undefined06 = 0x0007;
	public static final short PAK_SERVER_Undefined07 = 0x0008;
	public static final short PAK_SERVER_SynchronizePlayerCoordinates = 0x0009;
	public static final short PAK_SERVER_KeepAlive = 0x000A;
	public static final short PAK_SERVER_RemoveUnit = 0x000B;
	public static final short PAK_SERVER_SetUnitAppearance = 0x000C;
	public static final short PAK_SERVER_PutPlayerInGame = 0x000D;
	public static final short PAK_SERVER_Login = 0x000E;
	public static final short PAK_SERVER_DeletePlayer = 0x000F;
	public static final short PAK_SERVER_SendPeriphericObjects = 0x0010;
	public static final short PAK_SERVER_Unknown17 = 0x0011;
	public static final short PAK_SERVER_BackpackItems = 0x0012;
	public static final short PAK_SERVER_EquippedItems = 0x0013;
	public static final short PAK_SERVER_ServerShutdown = 0x0014;
	public static final short PAK_SERVER_Unknown21 = 0x0015;
	public static final short PAK_SERVER_Unknown22 = 0x0016;
	public static final short PAK_SERVER_Unknown23 = 0x0017;
	public static final short PAK_SERVER_Unknown24 = 0x0018;
	public static final short PAK_SERVER_RollDices = 0x0019;
	public static final short PAK_SERVER_PersonalPCList = 0x001A;
	public static final short PAK_SERVER_IndirectTalk = 0x001B;
	public static final short PAK_SERVER_Unknown28 = 0x001C;
	public static final short PAK_SERVER_Page = 0x001D;
	public static final short PAK_SERVER_Unknown30 = 0x001E;
	public static final short PAK_SERVER_Reroll = 0x001F;
	public static final short PAK_SERVER_Unknown32 = 0x0020;
	public static final short PAK_SERVER_UpdateLife = 0x0021;
	public static final short PAK_SERVER_BroadcastTextChange = 0x0022;
	public static final short PAK_SERVER_UnitName = 0x0023;
	public static final short PAK_SERVER_BreakConversation = 0x0024;
	public static final short PAK_SERVER_UpdateLevel = 0x0025;
	public static final short PAK_SERVER_ReturnToMenu = 0x0026;
	public static final short PAK_SERVER_PersonalSkills = 0x0027;
	public static final short PAK_SERVER_TrainList = 0x0028;
	public static final short PAK_SERVER_BuyList = 0x0029;
	public static final short PAK_SERVER_Unknown42 = 0x002A;
	public static final short PAK_SERVER_PersonalStats = 0x002B;
	public static final short PAK_SERVER_UpdateXP = 0x002C;
	public static final short PAK_SERVER_UpdateTime = 0x002D;
	public static final short PAK_SERVER_FromPreInGameToInGame = 0x002E;
	public static final short PAK_SERVER_Unknown47 = 0x002F;
	public static final short PAK_SERVER_Unknown48 = 0x0030;
	public static final short PAK_SERVER_ChannelMessage = 0x0031;
	public static final short PAK_SERVER_ChannelUsers = 0x0032;
	public static final short PAK_SERVER_PlayerList = 0x0033;
	public static final short PAK_SERVER_Unknown52 = 0x0034;
	public static final short PAK_SERVER_UpdateGold = 0x0035;
	public static final short PAK_SERVER_Unknown54 = 0x0036;
	public static final short PAK_SERVER_TeachList = 0x0037;
	public static final short PAK_SERVER_SellList = 0x0038;
	public static final short PAK_SERVER_TeleportPlayer = 0x0039;
	public static final short PAK_SERVER_Unknown58 = 0x003A;
	public static final short PAK_SERVER_ItemName = 0x003B;
	public static final short PAK_SERVER_NoUnitInView = 0x003C;
	public static final short PAK_SERVER_Unknown61 = 0x003D;
	public static final short PAK_SERVER_PersonalSpellList = 0x003E;
	public static final short PAK_SERVER_ServerMessage = 0x003F;
	public static final short PAK_SERVER_CastingSpell = 0x0040;
	public static final short PAK_SERVER_ServerInformation = 0x0041;
	public static final short PAK_SERVER_MessageOfTheDay = 0x0042;
	public static final short PAK_SERVER_UpdateMana = 0x0043;
	public static final short PAK_SERVER_PuppetInformation = 0x0044;
	public static final short PAK_SERVER_UnitAppearance = 0x0045;
	public static final short PAK_SERVER_UnitNotFound = 0x0046;
	public static final short PAK_SERVER_Unknown71 = 0x0047;
	public static final short PAK_SERVER_Unknown72 = 0x0048;
	public static final short PAK_SERVER_NoMoreItem = 0x0049;
	public static final short PAK_SERVER_Unknown74 = 0x004A;
	public static final short PAK_SERVER_ChannelList = 0x004B;
	public static final short PAK_SERVER_CreateGroup = 0x004C;
	public static final short PAK_SERVER_Unknown77 = 0x004D;
	public static final short PAK_SERVER_GroupInvite = 0x004E;
	public static final short PAK_SERVER_Unknown79 = 0x004F;
	public static final short PAK_SERVER_GroupLeave = 0x0050;
	public static final short PAK_SERVER_Unknown81 = 0x0051;
	public static final short PAK_SERVER_DeleteGroup = 0x0052;
	public static final short PAK_SERVER_ActivateUserSpell = 0x0053;
	public static final short PAK_SERVER_SpellOff = 0x0054;
	public static final short PAK_SERVER_Unknown85 = 0x0055;
	public static final short PAK_SERVER_Unknown86 = 0x0056;
	public static final short PAK_SERVER_GroupMemberHealth = 0x0057;
	public static final short PAK_SERVER_ToggleGroupAutoSplit = 0x0058;
	public static final short PAK_SERVER_Unknown89 = 0x0059;
	public static final short PAK_SERVER_PlayerExists = 0x005A;
	public static final short PAK_SERVER_PatchServerInformation = 0x005B;
	public static final short PAK_SERVER_CarriableWeight = 0x005C;
	public static final short PAK_SERVER_Rob = 0x005D;
	public static final short PAK_SERVER_DispelRob = 0x005E;
	public static final short PAK_SERVER_ArrowMissed = 0x005F;
	public static final short PAK_SERVER_ArrowSuccess = 0x0060;
	public static final short PAK_SERVER_ServerFlags = 0x0061;
	public static final short PAK_SERVER_SeraphArrival = 0x0062;
	public static final short PAK_SERVER_AuthenticateVersion = 0x0063;
	public static final short PAK_SERVER_ResetRegistryInventory = 0x0064;
	public static final short PAK_SERVER_InfoMessage = 0x0065;
	public static final short PAK_SERVER_InfoMessage2 = 0x0066;
	public static final short PAK_SERVER_MaxCharactersPerAccount = 0x0067;
	public static final short PAK_SERVER_ToggleWeather = 0x0068;
	public static final short PAK_SERVER_OpenURL = 0x0069;
	public static final short PAK_SERVER_ChestItems = 0x006A;
	public static final short PAK_SERVER_Unknown107 = 0x006B;
	public static final short PAK_SERVER_Unknown108 = 0x006C;
	public static final short PAK_SERVER_OpenChest = 0x006D;
	public static final short PAK_SERVER_CloseChest = 0x006E;
	public static final short PAK_SERVER_Undefined08 = 0x006F;
	public static final short PAK_SERVER_Undefined09 = 0x0070;
	public static final short PAK_SERVER_Undefined10 = 0x0071;
	public static final short PAK_SERVER_Undefined11 = 0x0072;
	public static final short PAK_SERVER_Undefined12 = 0x0073;
	public static final short PAK_SERVER_RequestExchange = 0x0074;
	public static final short PAK_SERVER_Undefined13 = 0x0075;
	public static final short PAK_SERVER_Undefined14 = 0x0076;
	public static final short PAK_SERVER_Undefined15 = 0x0077;
	public static final short PAK_SERVER_Undefined16 = 0x0078;
	public static final short PAK_SERVER_Undefined17= 0x0079;
	public static final short PAK_SERVER_ItemDescription = 0x007A;
	public static final short PAK_SERVER_Undefined18 = 0x007B;
	public static final short PAK_SERVER_Undefined19 = 0x00;
	public static final short PAK_SERVER_Undefined20 = 0x00;
	public static final short PAK_SERVER_Undefined21 = 0x00;
	public static final short PAK_SERVER_Undefined22 = 0x00;
	public static final short PAK_SERVER_AttackMissed = 0x2711;
	public static final short PAK_SERVER_AttackSuccess = 0x2712;
	public static final short PAK_SERVER_SkillSuccess = 0x2713;
	public static final short PAK_SERVER_PopupUnit = 0x2714;
	public static final short PAK_SERVER_NMS_DeathStatus = 0x00C8;
	public static final short PAK_SERVER_NMS_DeathProgress = 0x00C9;
	public static final short PAK_SERVER_NMS_DeathResurection = 0x00CA;	
	
	public static String getServerTypeInfos(short type, ByteBuffer pak) {
		StringBuilder sb = new StringBuilder();
		
		switch(type){
		case 0x0001 : sb.append("PAK_SERVER_UpdateCoordinates 0x0001 // a unit is moving on screen"+System.lineSeparator()+
				"	2   int16 x_coord; // new X coordinate on map"+System.lineSeparator()+
				"	4   int16 y_coord; // new Y coordinate on map"+System.lineSeparator()+
				"	6   int16 skin_id; // unit skin ID"+System.lineSeparator()+
				"	8   int32 unit_id; // unit ID"+System.lineSeparator()+
				"	12   int8 light_percentage; // radiance from 0 to 100"+System.lineSeparator()+
				"	13   int8 unit_type; // 0 = monster, 1 = NPC, 2 = player"+System.lineSeparator()+
				"	14   int8 health_percentage; // visual health from red (0) to green (100)");
		break;
		case 0x0002 : sb.append("PAK_SERVER_Undefined 0x0002");
		break;
		case 0x0003 : sb.append("PAK_SERVER_Undefined 0x0003");
		break;
		case 0x0004 : sb.append("PAK_SERVER_Undefined 0x0004");
		break;
		case 0x0005 : sb.append("PAK_SERVER_Undefined 0x0005");
		break;
		case 0x0006 : sb.append("PAK_SERVER_Undefined 0x0006");
		break;
		case 0x0007 : sb.append("PAK_SERVER_Undefined 0x0007");
		break;
		case 0x0008 : sb.append("PAK_SERVER_Undefined 0x0008");
		break;
		case 0x0009 : sb.append("PAK_SERVER_SynchronizePlayerCoordinates 0x0009 // server is making sure client is at the right location"+System.lineSeparator()+
				"   2   int16 x_coord;"+System.lineSeparator()+
				"   4   int16 y_coord;"+System.lineSeparator()+
				"   6   int16 world;");
		break;
		case 0x000A : sb.append("PAK_SERVER_KeepAlive 0x000A // Ping");
		break;
		case 0x000B : sb.append("PAK_SERVER_RemoveUnit 0x000B"+System.lineSeparator()+
				"   2   int8 unknown; // deprecated, set it to 0"+System.lineSeparator()+
				"   3   int32 unit_id; // unit ID");
		break;
		case 0x000C : sb.append("PAK_SERVER_SetUnitAppearance 0x000C"+System.lineSeparator()+
				"   2   int16 skin_id; // unit's skin ID (as referenced in the T4C Worlds.wda file)"+System.lineSeparator()+
				"   4   int32 unit_id; // unit ID");
		break;
		case 0x000D : sb.append("PAK_SERVER_PutPlayerInGame 0x000D"+System.lineSeparator()+
				"   2   int8 unknown1;"+System.lineSeparator()+
				"   3   int32 player_id;"+System.lineSeparator()+
				"   7   int16 x_coord;"+System.lineSeparator()+
				"   9   int16 y_coord;"+System.lineSeparator()+
				"   11   int16 world;"+System.lineSeparator()+
				"   13   int32 health;"+System.lineSeparator()+
				"   17   int32 max_health;"+System.lineSeparator()+
				"   21   int16 mana;"+System.lineSeparator()+
				"   23   int16 max_mana;"+System.lineSeparator()+
				"   25   int64 xp;"+System.lineSeparator()+
				"   33   int64 next_level_xp;"+System.lineSeparator()+
				"   41   int16 strength;"+System.lineSeparator()+
				"   43   int16 endurance;"+System.lineSeparator()+
				"   45   int16 dexterity;"+System.lineSeparator()+
				"   47   int16 willpower; // unused"+System.lineSeparator()+
				"   49   int16 wisdom;"+System.lineSeparator()+
				"   51   int16 intelligence;"+System.lineSeparator()+
				"   53   int16 luck; // unused"+System.lineSeparator()+
				"   55   int8 seconds;"+System.lineSeparator()+
				"   56   int8 minutes;"+System.lineSeparator()+
				"   57   int8 hour;"+System.lineSeparator()+
				"   58   int8 week;"+System.lineSeparator()+
				"   59   int8 day;"+System.lineSeparator()+
				"   60   int8 month;"+System.lineSeparator()+
				"   61   int16 year;"+System.lineSeparator()+
				"   63   int32 gold;"+System.lineSeparator()+
				"   67   int16 level;"+System.lineSeparator()+
				"   69   int64 base_level_xp;"+System.lineSeparator());
				getInt8(pak, sb, "Unknown1");
				getInt32(pak, sb, "Player ID");
				getInt16(pak, sb, "X Coord");
				getInt16(pak, sb, "Y coord");
				getInt16(pak, sb, "World");
				getInt32(pak, sb, "Health");
				getInt32(pak, sb, "Max Health");
				getInt16(pak, sb, "Mana");
				getInt16(pak, sb, "Max Mana");
				getInt64(pak, sb, "XP");
				getInt64(pak, sb, "Next Level XP");
				getInt16(pak, sb, "Strength");
				getInt16(pak, sb, "Endurance");
				getInt16(pak, sb, "Dexterity");
				getInt16(pak, sb, "Willpower");
				getInt16(pak, sb, "Wisdom");
				getInt16(pak, sb, "Intelligence");
				getInt16(pak, sb, "Luck");
				getInt8(pak, sb, "Seconds");
				getInt8(pak, sb, "Minutes");
				getInt8(pak, sb, "Hour");
				getInt8(pak, sb, "Day");
				getInt8(pak, sb, "Year");
				getInt32(pak, sb, "Gold");
				getInt16(pak, sb, "Level");
				getInt64(pak, sb, "Base Level XP");
				
		break;
		case 0x000E : sb.append("PAK_SERVER_Login 0x000E"+System.lineSeparator()+
				"   2    int8 status; // 0 = welcome, 1 = can't login, 2 = already logged in"+System.lineSeparator()+
				"   3    string16 message;"+System.lineSeparator());
				getInt8(pak, sb, "Status");
				getString16(pak, sb, "Message");
		break;
		case 0x000F : sb.append("PAK_SERVER_DeletePlayer 0x000F"+System.lineSeparator()+
				"   2    int8 status; // 0 = no error, 1 = error");
		break;
		case 0x0010 : sb.append("PAK_SERVER_SendPeriphericObjects 0x0010"+System.lineSeparator()+
				"   2   int16 unit_count; // number of units in the array below"+System.lineSeparator()+
				"   for (i = 0; i < unit_count; i++) // for each unit..."+System.lineSeparator()+
				"   	{"+System.lineSeparator()+
				"   	2+i*13         int16 x_coord; // unit's X coordinate on map"+System.lineSeparator()+
				"   	2+i*13+2       int16 y_coord; // unit's Y coordinate on map"+System.lineSeparator()+
				"   	2+i*13+4       int16 skin_id; // unit skin ID, as referenced in the T4C Worlds WDA file"+System.lineSeparator()+
				"   	2+i*13+6       int32 unit_id; // unit ID"+System.lineSeparator()+
				"   	2+i*13+10      int8 light_percentage; // unit radiance from 0 to 100"+System.lineSeparator()+
				"   	2+i*13+11      int8 unit_type; // 0 = monster, 1 = NPC, 2 = player"+System.lineSeparator()+
				"   	2+i*13+12      int8 health_percentage; // unit's visual health from 0 (red) to 100 (green)"+System.lineSeparator()+
				"	}"+System.lineSeparator());
				int unit_count = getInt16(pak, sb, "Unit Count");
				for (int i = 0 ; i < unit_count ; i++){
					getInt16(pak, sb, "X Coord");
					getInt16(pak, sb, "Y Xoord");
					getInt16(pak, sb, "Skin ID");
					getInt32(pak, sb, "Unit ID");
					getInt8(pak, sb, "Light Percentage");
					getInt8(pak, sb, "Unit Type");
					getInt8(pak, sb, "Health Percentage");
				}
		break;
		case 0x0011 : sb.append("PAK_SERVER_Unknown17 0x0011");
		break;
		case 0x0012 : sb.append("PAK_SERVER_BackpackItems 0x0012"+System.lineSeparator()+
				"   2   int8 popup_backpack; // set to TRUE if the client should pop up the backpack on screen"+System.lineSeparator()+
				"   3   int32 player_id; // player ID, as read in the database"+System.lineSeparator()+
				"   7   int16 item_count; // amount of items this player has in backpack"+System.lineSeparator()+
				"   for (item_index = 0; item_index < item_count; item_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"   	9+i*16         int16 icon_id; // item skin ID, the full list is in the T4C Worlds.wda file"+System.lineSeparator()+
				"   	9+i*16+2       int32 unit_id; // item instance ID (unique), to be saved in the database"+System.lineSeparator()+
				"   	9+i*16+6       int16 object_id; // item ID as read in the WDAs"+System.lineSeparator()+
				"   	9+i*16+8       int32 quantity; // amount of items, if this object is stacked with others"+System.lineSeparator()+
				"   	9+i*16+12      int32 number_of_charges; // number of charges this object has (for ex, keys)"+System.lineSeparator()+
				"   }"+System.lineSeparator());
				getInt8(pak, sb, "Popup BackPack");
				getInt32(pak, sb, "Player ID");
				int item_count = getInt16(pak, sb, "Item Count");
				for(int i = 0 ; i < item_count ; i++){
					getInt16(pak, sb, "Icon ID");
					getInt32(pak, sb, "Unit ID");
					getInt16(pak, sb, "Object ID");
					getInt32(pak, sb, "Quantity");
					getInt32(pak, sb, "Number Of Charges");
				}
		break;
		case 0x0013 : sb.append("PAK_SERVER_EquippedItems 0x0013"+System.lineSeparator()+
				"   2   int8 Ranged_Attack;"+System.lineSeparator()+
				"   3   int32 body_item_unit_id;"+System.lineSeparator()+
				"   7   int16 body_icon_id;"+System.lineSeparator()+
				"   int16 body_object_id;"+System.lineSeparator()+
				"   int16 body_item_count;"+System.lineSeparator()+
				"   int32 body_unknown;"+System.lineSeparator()+
				"   string16 body_itemname;"+System.lineSeparator()+
				"   int32 hands_item_unit_id;"+System.lineSeparator()+
				"   int16 hands_icon_id;"+System.lineSeparator()+
				"   int16 hands_object_id;"+System.lineSeparator()+
				"   int16 hands_item_count;"+System.lineSeparator()+
				"   int32 hands_unknown;"+System.lineSeparator()+
				"   string16 hands_itemname;"+System.lineSeparator()+
				"   int32 head_item_unit_id;"+System.lineSeparator()+
				"   int16 head_icon_id;"+System.lineSeparator()+
				"   int16 head_object_id;"+System.lineSeparator()+
				"   int16 head_item_count;"+System.lineSeparator()+
				"   int32 head_unknown;"+System.lineSeparator()+
				"   string16 head_itemname;"+System.lineSeparator()+
				"   int32 legs_item_unit_id;"+System.lineSeparator()+
				"   int16 legs_icon_id;"+System.lineSeparator()+
				"   int16 legs_object_id;"+System.lineSeparator()+
				"   int16 legs_item_count;"+System.lineSeparator()+
				"   int32 legs_unknown;"+System.lineSeparator()+
				"   string16 legs_itemname;"+System.lineSeparator()+
				"   int32 bracers_item_unit_id;"+System.lineSeparator()+
				"   int16 bracers_icon_id;"+System.lineSeparator()+
				"   int16 bracers_object_id;"+System.lineSeparator()+
				"   int16 bracers_item_count;"+System.lineSeparator()+
				"   int32 bracers_unknown;"+System.lineSeparator()+
				"   string16 bracers_itemname;"+System.lineSeparator()+
				"   int32 neck_item_unit_id;"+System.lineSeparator()+
				"   int16 neck_icon_id;"+System.lineSeparator()+
				"   int16 neck_object_id;"+System.lineSeparator()+
				"   int16 neck_item_count;"+System.lineSeparator()+
				"   int32 neck_unknown;"+System.lineSeparator()+
				"   string16 neck_itemname;"+System.lineSeparator()+
				"   int32 rhand_item_unit_id;"+System.lineSeparator()+
				"   int16 rhand_icon_id;"+System.lineSeparator()+
				"   int16 rhand_object_id;"+System.lineSeparator()+
				"   int16 rhand_item_count;"+System.lineSeparator()+
				"   int32 rhand_unknown;"+System.lineSeparator()+
				"   string16 rhand_itemname;"+System.lineSeparator()+
				"   int32 lhand_item_unit_id;"+System.lineSeparator()+
				"   int16 lhand_icon_id;"+System.lineSeparator()+
				"   int16 lhand_object_id;"+System.lineSeparator()+
				"   int16 lhand_item_count;"+System.lineSeparator()+
				"   int32 lhand_unknown;"+System.lineSeparator()+
				"   string16 lhand_itemname;"+System.lineSeparator()+
				"   int32 rring_item_unit_id;"+System.lineSeparator()+
				"   int16 rring_icon_id;"+System.lineSeparator()+
				"   int16 rring_object_id;"+System.lineSeparator()+
				"   int16 rring_item_count;"+System.lineSeparator()+
				"   int32 rring_unknown;"+System.lineSeparator()+
				"   string16 rring_itemname;"+System.lineSeparator()+
				"   int32 lring_item_unit_id;"+System.lineSeparator()+
				"   int16 lring_icon_id;"+System.lineSeparator()+
				"   int16 lring_object_id;"+System.lineSeparator()+
				"   int16 lring_item_count;"+System.lineSeparator()+
				"   int32 lring_unknown;"+System.lineSeparator()+
				"   string16 lring_itemname;"+System.lineSeparator()+
				"   int32 belt_item_unit_id;"+System.lineSeparator()+
				"   int16 belt_icon_id;"+System.lineSeparator()+
				"   int16 belt_object_id;"+System.lineSeparator()+
				"   int16 belt_item_count;"+System.lineSeparator()+
				"   int32 belt_unknown;"+System.lineSeparator()+
				"   string16 belt_itemname;"+System.lineSeparator()+
				"   int32 back_item_unit_id;"+System.lineSeparator()+
				"   int16 back_icon_id;"+System.lineSeparator()+
				"   int16 back_object_id;"+System.lineSeparator()+
				"   int16 back_item_count;"+System.lineSeparator()+
				"   int32 back_unknown;"+System.lineSeparator()+
				"   string16 back_itemname;"+System.lineSeparator()+
				"   int32 feet_item_unit_id;"+System.lineSeparator()+
				"   int16 feet_icon_id;"+System.lineSeparator()+
				"   int16 feet_object_id;"+System.lineSeparator()+
				"   int16 feet_item_count;"+System.lineSeparator()+
				"   int32 feet_unknown;"+System.lineSeparator()+
				"   string16 feet_itemname;"+System.lineSeparator());
				getInt8(pak, sb, "Ranged Attack");
				
				getInt32(pak, sb, "Body Item Unit ID");
				getInt16(pak, sb, "Body Icon ID");
				getInt16(pak, sb, "Body Object ID");
				getInt16(pak, sb, "Body item Count");
				getInt32(pak, sb, "Body Unknown");
				getString16(pak, sb, "Body Item name");
				
				getInt32(pak, sb, "Hands Item Unit ID");
				getInt16(pak, sb, "Hands Icon ID");
				getInt16(pak, sb, "Hands Object ID");
				getInt16(pak, sb, "Hands item Count");
				getInt32(pak, sb, "Hands Unknown");
				getString16(pak, sb, "Hands Item name");
				
				getInt32(pak, sb, "Head Item Unit ID");
				getInt16(pak, sb, "Head Icon ID");
				getInt16(pak, sb, "Head Object ID");
				getInt16(pak, sb, "Head item Count");
				getInt32(pak, sb, "Head Unknown");
				getString16(pak, sb, "Head Item name");
				
				getInt32(pak, sb, "Legs Item Unit ID");
				getInt16(pak, sb, "Legs Icon ID");
				getInt16(pak, sb, "Legs Object ID");
				getInt16(pak, sb, "Legs item Count");
				getInt32(pak, sb, "Legs Unknown");
				getString16(pak, sb, "Legs Item name");
				
				getInt32(pak, sb, "Bracers Item Unit ID");
				getInt16(pak, sb, "Bracers Icon ID");
				getInt16(pak, sb, "Bracers Object ID");
				getInt16(pak, sb, "Bracers item Count");
				getInt32(pak, sb, "Bracers Unknown");
				getString16(pak, sb, "Bracers Item name");
				
				getInt32(pak, sb, "Neck Item Unit ID");
				getInt16(pak, sb, "Neck Icon ID");
				getInt16(pak, sb, "Neck Object ID");
				getInt16(pak, sb, "Neck item Count");
				getInt32(pak, sb, "Neck Unknown");
				getString16(pak, sb, "Neck Item name");
				
				getInt32(pak, sb, "Right Hand Item Unit ID");
				getInt16(pak, sb, "Right Hand Icon ID");
				getInt16(pak, sb, "Right Hand Object ID");
				getInt16(pak, sb, "Right Hand item Count");
				getInt32(pak, sb, "Right Hand Unknown");
				getString16(pak, sb, "Right Hand Item name");
				
				getInt32(pak, sb, "Left Hand Item Unit ID");
				getInt16(pak, sb, "Left Hand Icon ID");
				getInt16(pak, sb, "Left Hand Object ID");
				getInt16(pak, sb, "Left Hand item Count");
				getInt32(pak, sb, "Left Hand Unknown");
				getString16(pak, sb, "Left Hand Item name");
				
				getInt32(pak, sb, "Right Ring Item Unit ID");
				getInt16(pak, sb, "Right Ring Icon ID");
				getInt16(pak, sb, "Right Ring Object ID");
				getInt16(pak, sb, "Right Ring item Count");
				getInt32(pak, sb, "Right Ring Unknown");
				getString16(pak, sb, "Right Ring Item name");
				
				getInt32(pak, sb, "Left Ring Item Unit ID");
				getInt16(pak, sb, "Left Ring Icon ID");
				getInt16(pak, sb, "Left Ring Object ID");
				getInt16(pak, sb, "Left Ring item Count");
				getInt32(pak, sb, "Left Ring Unknown");
				getString16(pak, sb, "Left Ring Item name");
				
				getInt32(pak, sb, "Belt Item Unit ID");
				getInt16(pak, sb, "Belt Icon ID");
				getInt16(pak, sb, "Belt Object ID");
				getInt16(pak, sb, "Belt item Count");
				getInt32(pak, sb, "Belt Unknown");
				getString16(pak, sb, "Belt Item name");
				
				getInt32(pak, sb, "Back Item Unit ID");
				getInt16(pak, sb, "Back Icon ID");
				getInt16(pak, sb, "Back Object ID");
				getInt16(pak, sb, "Back item Count");
				getInt32(pak, sb, "Back Unknown");
				getString16(pak, sb, "Back Item name");
				
				getInt32(pak, sb, "Feet Item Unit ID");
				getInt16(pak, sb, "Feet Icon ID");
				getInt16(pak, sb, "Feet Object ID");
				getInt16(pak, sb, "Feet item Count");
				getInt32(pak, sb, "Feet Unknown");
				getString16(pak, sb, "Feet Item name");
		break;
		case 0x0014 : sb.append("PAK_SERVER_ServerShutdown 0x0014");
		break;
		case 0x0015 : sb.append("PAK_SERVER_Unknown21 0x0015");
		break;
		case 0x0016 : sb.append("PAK_SERVER_Unknown22 0x0016");
		break;
		case 0x0017 : sb.append("PAK_SERVER_Unknown23 0x0017");
		break;
		case 0x0018 : sb.append("PAK_SERVER_Unknown24 0x0018");
		break;
		case 0x0019 : sb.append("PAK_SERVER_RollDices 0x0019"+System.lineSeparator()+
				"   2   int8 unknown;"+System.lineSeparator()+
				"   3   int8 dexterity;"+System.lineSeparator()+
				"   4   int8 endurance;"+System.lineSeparator()+
				"   5   int8 intelligence;"+System.lineSeparator()+
				"   6   int8 luck; // unused"+System.lineSeparator()+
				"   7   int8 strength;"+System.lineSeparator()+
				"   8   int8 willpower; // unused"+System.lineSeparator()+
				"   9   int8 wisdom;"+System.lineSeparator()+
				"   10   int32 health;"+System.lineSeparator()+
				"   14   int32 max_health;"+System.lineSeparator()+
				"   18   int16 mana;"+System.lineSeparator()+
				"   20   int16 max_mana;");
		break;
		case 0x001A : sb.append("PAK_SERVER_PersonalPCList 0x001A"+System.lineSeparator()+
				"   int8 character_count;"+System.lineSeparator()+
				"   for (character_index = 0; character_index < character_count; character_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      string8 playername;"+System.lineSeparator()+
				"      int16 skin_id;"+System.lineSeparator()+
				"      int16 level;"+System.lineSeparator()+
				"   }"+System.lineSeparator());
				int count = getInt8(pak, sb, "Character Count");
				for (int i = 0 ; i < count ; i++){
					getString8(pak, sb, "Player Name");
					getInt16(pak, sb, "Skin ID");
					getInt16(pak, sb, "Level");
				}
		break;
		case 0x001B : sb.append("PAK_SERVER_IndirectTalk 0x001B"+System.lineSeparator()+
				"   2   int32 sender_id;"+System.lineSeparator()+
				"   6   int8 sender_orientation;"+System.lineSeparator()+
				"   7   int32 text_color; // unused"+System.lineSeparator()+
				"   11   int8 sender_type; // 0 = player, 1 = NPC"+System.lineSeparator()+
				"   12   string16 message;"+System.lineSeparator()+
				"   12+[2+S]   string16 sender_playername;"+System.lineSeparator()+
				"   12+[2+S]+[2+S] Int32 unknown; // 1.65: seems to always be 0xFF");
		break;
		case 0x001C : sb.append("PAK_SERVER_Unknown28 0x001C");
		break;
		case 0x001D : sb.append("PAK_SERVER_Page 0x001D // whisp"+System.lineSeparator()+
				"   2   string16 sender_playername;"+System.lineSeparator()+
				"   2+[2+S]   string16 message;");
		break;
		case 0x001E : sb.append("PAK_SERVER_Unknown30 0x001E");
		break;
		case 0x001F : sb.append("PAK_SERVER_Reroll 0x001F"+System.lineSeparator()+
				"   2   int8 dexterity;"+System.lineSeparator()+
				"   3   int8 endurance;"+System.lineSeparator()+
				"   4   int8 intelligence;"+System.lineSeparator()+
				"   5   int8 luck; // unused"+System.lineSeparator()+
				"   6   int8 strength;"+System.lineSeparator()+
				"   7   int8 willpower; // unused"+System.lineSeparator()+
				"   8   int8 wisdom;"+System.lineSeparator()+
				"   9   int32 health;"+System.lineSeparator()+
				"   13  int32 max_health;"+System.lineSeparator()+
				"   17  int16 mana;"+System.lineSeparator()+
				"   19  int16 max_mana;");
		break;
		case 0x0020 : sb.append("PAK_SERVER_Unknown32 0x0020");
		break;
		case 0x0021 : sb.append("PAK_SERVER_UpdateLife 0x0021"+System.lineSeparator()+
				"   2   int32 health;"+System.lineSeparator()+
				"   6   int32 max_health;"+System.lineSeparator());
				getInt32(pak, sb, "Health");
				getInt32(pak, sb, "Max Health");
		break;
		case 0x0022 : sb.append("PAK_SERVER_BroadcastTextChange 0x0022"+System.lineSeparator()+
				"   2   int32 npc_unit_id;"+System.lineSeparator()+
				"   6   int16 start_line_number;");
		break;
		case 0x0023 : sb.append("PAK_SERVER_UnitName 0x0023"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   string16 unitname;"+System.lineSeparator()+
				"   6+[2+S] Int32 color; // caught on 1.65 server"+System.lineSeparator()+
				"   10+[2+S] string16 name_2; // the text that appears under the player's name. caught on 1.65");
		break;
		case 0x0024 : sb.append("PAK_SERVER_BreakConversation 0x0024");
		break;
		case 0x0025 : sb.append("PAK_SERVER_UpdateLevel 0x0025"+System.lineSeparator()+
				"   2   int16 level;"+System.lineSeparator()+
				"   4   int64 xp_left_before_next_level;"+System.lineSeparator()+
				"   12   int32 health;"+System.lineSeparator()+
				"   16   int32 max_health;"+System.lineSeparator()+
				"   20   int16 mana;"+System.lineSeparator()+
				"   22   int16 max_mana;"+System.lineSeparator());
				getInt16(pak, sb, "Level");
				getInt64(pak, sb, "XP Before Next Level");
				getInt32(pak, sb, "Health");
				getInt32(pak, sb, "Max Health");
				getInt16(pak, sb, "Mana");
				getInt16(pak, sb, "Max Mana");
		break;
		case 0x0026 : sb.append("PAK_SERVER_ReturnToMenu 0x0026"+System.lineSeparator()+
				"   2   int8 status; // 0 = OK, 1 = error");
		break;
		case 0x0027 : sb.append("PAK_SERVER_PersonalSkills 0x0027"+System.lineSeparator()+
				"   2   int16 skill_count;"+System.lineSeparator()+
				"   for (skill_index = 0; skill_index < skill_count; skill_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 skill_id;"+System.lineSeparator()+
				"      int8 use; // 0 = permanent, 1 = on trigger, 3 = specify target"+System.lineSeparator()+
				"      int16 value;"+System.lineSeparator()+
				"      int16 boosted_value;"+System.lineSeparator()+
				"      string16 skillname;"+System.lineSeparator()+
				"      string16 description;"+System.lineSeparator()+
				"   }");
		break;
		case 0x0028 : sb.append("PAK_SERVER_TrainList 0x0028"+System.lineSeparator()+
				"   int16 available_points;"+System.lineSeparator()+
				"   int16 skill_count;"+System.lineSeparator()+
				"   for (skill_index = 0; skill_index < skill_count; skill_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int8 status; // 0 = can't train this skill, 1 = can train this skill"+System.lineSeparator()+
				"      int16 skill_id;"+System.lineSeparator()+
				"      int32 current_skill_value;"+System.lineSeparator()+
				"      int16 max_skill_value;"+System.lineSeparator()+
				"      int16 price;"+System.lineSeparator()+
				"      string16 skillname;"+System.lineSeparator()+
				"   }");
		break;
		case 0x0029 : sb.append("PAK_SERVER_BuyList 0x0029"+System.lineSeparator()+
				"   int32 player_gold;"+System.lineSeparator()+
				"   int16 item_count;"+System.lineSeparator()+
				"   for (item_index = 0; item_index < item_count; item_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 object_id;"+System.lineSeparator()+
				"      int16 icon_id;"+System.lineSeparator()+
				"      int32 price;"+System.lineSeparator()+
				"      int8 status; // 0 = can't buy this item, 1 = can buy this item"+System.lineSeparator()+
				"      string16 itemname;"+System.lineSeparator()+
				"      string16 description;"+System.lineSeparator()+
				"   }");
		break;
		case 0x002A : sb.append("PAK_SERVER_Unknown42 0x002A");
		break;
		case 0x002B : sb.append("PAK_SERVER_PersonalStats 0x002B"+System.lineSeparator()+
				"   2   int32 health;"+System.lineSeparator()+
				"   6   int32 max_health;"+System.lineSeparator()+
				"   10   int16 mana;"+System.lineSeparator()+
				"   12   int16 max_mana;"+System.lineSeparator()+
				"   14   int64 xp;"+System.lineSeparator()+
				"   22   int16 boosted_armor;"+System.lineSeparator()+
				"   24   int16 armor;"+System.lineSeparator()+
				"   26   int16 boosted_strength;"+System.lineSeparator()+
				"   28   int16 boosted_endurance;"+System.lineSeparator()+
				"   30   int16 boosted_dexterity;"+System.lineSeparator()+
				"   32   int16 boosted_willpower; // unused"+System.lineSeparator()+
				"   34   int16 boosted_wisdom;"+System.lineSeparator()+
				"   36   int16 boosted_intelligence;"+System.lineSeparator()+
				"   38   int16 boosted_luck; // unused"+System.lineSeparator()+
				"   40   int16 stat_points;"+System.lineSeparator()+
				"   42   int16 strength;"+System.lineSeparator()+
				"   44   int16 endurance;"+System.lineSeparator()+
				"   46   int16 dexterity;"+System.lineSeparator()+
				"   48   int16 willpower; // unused"+System.lineSeparator()+
				"   50   int16 wisdom;"+System.lineSeparator()+
				"   52   int16 intelligence;"+System.lineSeparator()+
				"   54   int16 luck; // unused"+System.lineSeparator()+
				"   56   int16 level;"+System.lineSeparator()+
				"   58   int16 skill_points;"+System.lineSeparator()+
				"   60   int16 luggage;"+System.lineSeparator()+
				"   62   int16 max_luggage;"+System.lineSeparator()+
				"   64   int16 karma;"+System.lineSeparator()+
				"   66   int16 base_health;");
		break;
		case 0x002C : sb.append("PAK_SERVER_UpdateXP 0x002C"+System.lineSeparator()+
				"   2   int64 xp;");
		break;
		case 0x002D : sb.append("PAK_SERVER_UpdateTime 0x002D"+System.lineSeparator()+
				"   2   int8 seconds;"+System.lineSeparator()+
				"   3   int8 minutes;"+System.lineSeparator()+
				"   4   int8 hours;"+System.lineSeparator()+
				"   5   int8 week;"+System.lineSeparator()+
				"   6   int8 day;"+System.lineSeparator()+
				"   7   int8 month;"+System.lineSeparator()+
				"   8   int16 year;");
		break;
		case 0x002E : sb.append("PAK_SERVER_FromPreInGameToInGame 0x002E"+System.lineSeparator()+
				"   int8 ²; // 0 = data following, 1 = no data following"+System.lineSeparator()+
				"   if (!no_data)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 unknown; // seems useless, always 0x0000"+System.lineSeparator()+
				"      int16 unit_count;"+System.lineSeparator()+
				"      for (unit_index = 0; unit_index < unit_count; unit_index++)"+System.lineSeparator()+
				"      {"+System.lineSeparator()+
				"         int16 x_coord;"+System.lineSeparator()+
				"         int16 y_coord;"+System.lineSeparator()+
				"         int16 skin_id;"+System.lineSeparator()+
				"         int32 unit_id;"+System.lineSeparator()+
				"         int8 light_percentage;"+System.lineSeparator()+
				"         int8 unit_type; // 0 = monster, 1 = NPC, 2 = player"+System.lineSeparator()+
				"         int8 health_percentage;"+System.lineSeparator()+
				"      }"+System.lineSeparator()+
				"   }");
		break;
		case 0x002F : sb.append("PAK_SERVER_Unknown47 0x002F // unused (probably obsolete)");
		break;
		case 0x0030 : sb.append("PAK_SERVER_Unknown48 0x0030");
		break;
		case 0x0031 : sb.append("PAK_SERVER_ChannelMessage 0x0031"+System.lineSeparator()+
				"   string16 channelname;"+System.lineSeparator()+
				"   string16 playername;"+System.lineSeparator()+
				"   string16 message;");
		break;
		case 0x0032 : sb.append("PAK_SERVER_ChannelUsers 0x0032"+System.lineSeparator()+
				"   string16 channelname;"+System.lineSeparator()+
				"   int16 player_count;"+System.lineSeparator()+
				"   for (player_index = 0; player_index < player_count; player_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      string16 playername;"+System.lineSeparator()+
				"      string16 playertitle;"+System.lineSeparator()+
				"      int8 status; // 0 = has closed channel, 1 = has opened channel"+System.lineSeparator()+
				"   }");
		break;
		case 0x0033 : sb.append("PAK_SERVER_PlayerList 0x0033");
		break;
		case 0x0034 : sb.append("PAK_SERVER_Unknown52 0x0034");
		break;
		case 0x0035 : sb.append("PAK_SERVER_UpdateGold 0x0035"+System.lineSeparator()+
				"   2   int32 gold;"+System.lineSeparator());
				getInt32(pak, sb, "Gold");
		break;
		case 0x0036 : sb.append("PAK_SERVER_Unknown54 0x0036");
		break;
		case 0x0037 : sb.append("PAK_SERVER_TeachList 0x0037"+System.lineSeparator()+
				"   int16 player_skillpoints;"+System.lineSeparator()+
				"   int16 skill_count;"+System.lineSeparator()+
				"   for (skill_index = 0; skill_index < skill_count; skill_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int8 status; // 0 = can't be taught to player, 1 = can be taught to player"+System.lineSeparator()+
				"      int16 skill_id;"+System.lineSeparator()+
				"      int32 price;"+System.lineSeparator()+
				"      string16 skillname;"+System.lineSeparator()+
				"      string16 description;"+System.lineSeparator()+
				"      int32 cost_in_points;"+System.lineSeparator()+
				"      int32 skill_icon_id;"+System.lineSeparator()+
				"   }");
		break;
		case 0x0038 : sb.append("PAK_SERVER_SellList 0x0038"+System.lineSeparator()+
				"   int32 player_gold;"+System.lineSeparator()+
				"   int16 item_count;"+System.lineSeparator()+
				"   for (item_index = 0; item_index < item_count; item_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int32 item_unit_id;"+System.lineSeparator()+
				"      int16 item_type_id;"+System.lineSeparator()+
				"      int32 price;"+System.lineSeparator()+
				"      int32 quantity;"+System.lineSeparator()+
				"      string16 itemname;"+System.lineSeparator()+
				"   }");
		break;
		case 0x0039 : sb.append("PAK_SERVER_TeleportPlayer 0x0039"+System.lineSeparator()+
				"   2   int16 x_coord;"+System.lineSeparator()+
				"   4   int16 y_coord;"+System.lineSeparator()+
				"   6   int16 world;");
		break;
		case 0x003A : sb.append("PAK_SERVER_Unknown58 0x003A");
		break;
		case 0x003B : sb.append("PAK_SERVER_ItemName 0x003B"+System.lineSeparator()+
				"   2   int32 object_id;"+System.lineSeparator()+
				"   6   string16 itemname;");
		break;
		case 0x003C : sb.append("PAK_SERVER_NoUnitInView 0x003C");
		break;
		case 0x003D : sb.append("PAK_SERVER_Unknown61 0x003D // unused (probably obsolete)");
		break;
		case 0x003E : sb.append("PAK_SERVER_PersonalSpellList 0x003E"+System.lineSeparator()+
				"   int8 unknown; // unused (probably obsolete), always 0x00"+System.lineSeparator()+
				"   int16 mana;"+System.lineSeparator()+
				"   int16 max_mana;"+System.lineSeparator()+
				"   int16 spell_count;"+System.lineSeparator()+
				"   for (spell_index = 0; spell_index < spell_count; spell_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 spell_id;"+System.lineSeparator()+
				"      int8 target_type;"+System.lineSeparator()+
				"      int16 mana_cost;"+System.lineSeparator()+
				"      int16 unknown1;"+System.lineSeparator()+
				"      int16 unknown2;"+System.lineSeparator()+
				"      int16 unknown3;"+System.lineSeparator()+
				"      int16 unknown4;"+System.lineSeparator()+
				"      int16 unknown5;"+System.lineSeparator()+
				"      int16 unknown6;"+System.lineSeparator()+
				"      int16 spell_icon;"+System.lineSeparator()+
				"      string16 description;"+System.lineSeparator()+
				"      string16 spellname;"+System.lineSeparator()+
				"   }");
		break;
		case 0x003F : sb.append("PAK_SERVER_ServerMessage 0x003F"+System.lineSeparator()+
				"   2   int32 unused; // was color, but client sets it to 0x00FF6400 (blue). Can be nulled."+System.lineSeparator()+
				"   6   string16 message;");
		break;
		case 0x0040 : sb.append("PAK_SERVER_CastingSpell 0x0040"+System.lineSeparator()+
				"   2   int16 effect_id;"+System.lineSeparator()+
				"   4   int32 source_id;"+System.lineSeparator()+
				"   8   int32 target_id;"+System.lineSeparator()+
				"   12   int16 target_x_coord;"+System.lineSeparator()+
				"   14   int16 target_y_coord;"+System.lineSeparator()+
				"   16   int16 source_x_coord;"+System.lineSeparator()+
				"   18   int16 source_y_coord;"+System.lineSeparator()+
				"   20   int32 created_unit_id;"+System.lineSeparator()+
				"   24   int32 unknown;");
		break;
		case 0x0041 : sb.append("PAK_SERVER_ServerInformation 0x0041"+System.lineSeparator()+
				"   int32 version;"+System.lineSeparator()+
				"   int16 patchserver_count;"+System.lineSeparator()+
				"   for (patchserver_index = 0; patchserver_index < patchserver_count; patchserver_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 port_number;"+System.lineSeparator()+
				"      string16 address;"+System.lineSeparator()+
				"   }");
		break;
		case 0x0042 : sb.append("PAK_SERVER_MessageOfTheDay 0x0042"+System.lineSeparator()+
				"   2   string16 MOTD"+System.lineSeparator());
				getString16(pak, sb, "MOTD");
		break;
		case 0x0043 : sb.append("PAK_SERVER_UpdateMana 0x0043"+System.lineSeparator()+
				"   2   int16 mana;"+System.lineSeparator());
				getInt16(pak, sb, "Mana");
		break;
		case 0x0044 : sb.append("PAK_SERVER_PuppetInformation 0x0044"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   int16 body_type_id;"+System.lineSeparator()+
				"   8   int16 feet_type_id;"+System.lineSeparator()+
				"   10   int16 hands_type_id;"+System.lineSeparator()+
				"   12   int16 head_type_id;"+System.lineSeparator()+
				"   14   int16 legs_type_id;"+System.lineSeparator()+
				"   16   int16 weapon_type_id;"+System.lineSeparator()+
				"   18   int16 shield_type_id;"+System.lineSeparator()+
				"   20   int16 back_type_id;"+System.lineSeparator());
				getInt32(pak, sb, "Unit ID");
				getInt16(pak, sb, "Body Type ID");
				getInt16(pak, sb, "Feet Type ID");
				getInt16(pak, sb, "Hands Type ID");
				getInt16(pak, sb, "Head Type ID");
				getInt16(pak, sb, "Legs Type ID");
				getInt16(pak, sb, "Weapon Type ID");
				getInt16(pak, sb, "Shield Type ID");
				getInt16(pak, sb, "Back Type ID");
				
				
		break;
		case 0x0045 : sb.append("PAK_SERVER_UnitAppearance 0x0045"+System.lineSeparator()+
				"   2   int16 skin_id;"+System.lineSeparator()+
				"   4   int32 unit_id;"+System.lineSeparator()+
				"   8   int8 light_percentage;"+System.lineSeparator()+
				"   9   int8 unit_type; // 0 = monster, 1 = NPC, 2 = player"+System.lineSeparator()+
				"   10   int8 health_percentage;");
		break;
		case 0x0046 : sb.append("PAK_SERVER_UnitNotFound 0x0046"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   int16 in_reply_to_pak_type;");
		break;
		case 0x0047 : sb.append("PAK_SERVER_Unknown71 0x0047");
		break;
		case 0x0048 : sb.append("PAK_SERVER_Unknown72 0x0048");
		break;
		case 0x0049 : sb.append("PAK_SERVER_NoMoreItem 0x0049"+System.lineSeparator()+
				"   2   int16 item_id;");
		break;
		case 0x004A : sb.append("PAK_SERVER_Unknown74 0x004A");
		break;
		case 0x004B : sb.append("PAK_SERVER_ChannelList 0x004B"+System.lineSeparator()+
				"   int16 channel_count;"+System.lineSeparator()+
				"   for (channel_index = 0; channel_index < channel_count; channel_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      string16 channelname;"+System.lineSeparator()+
				"      int8 status; // 0 = closed, 1 = open"+System.lineSeparator()+
				"   }"+System.lineSeparator());
				int channel_count = getInt16(pak, sb, "Channel count");
				for(int i = 0 ; i< channel_count ; i++){
					getString16(pak, sb, "Channel Name");
					getInt8(pak, sb, "Status");
				}
		break;
		case 0x004C : sb.append("PAK_SERVER_CreateGroup 0x004C"+System.lineSeparator()+
				"   int8 unknown; // unused (probably obsolete), always 0x01"+System.lineSeparator()+
				"   int16 player_count;"+System.lineSeparator()+
				"   for (player_index = 0; player_index < player_count; player_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int32 player_unit_id;"+System.lineSeparator()+
				"      int16 player_level;"+System.lineSeparator()+
				"      int16 health_percentage;"+System.lineSeparator()+
				"      int8 is_creator; // set to TRUE if this player is the leader of this group"+System.lineSeparator()+
				"      string16 playername;"+System.lineSeparator()+
				"   }");
		break;
		case 0x004D : sb.append("PAK_SERVER_Unknown77 0x004D");
		break;
		case 0x004E : sb.append("PAK_SERVER_GroupInvite 0x004E"+System.lineSeparator()+
				"   2   int32 player_unit_id;"+System.lineSeparator()+
				"   6   string16 message;");
		break;
		case 0x004F : sb.append("PAK_SERVER_Unknown79 0x004F");
		break;
		case 0x0050 : sb.append("PAK_SERVER_GroupLeave 0x0050");
		break;
		case 0x0051 : sb.append("PAK_SERVER_Unknown81 0x0051");
		break;
		case 0x0052 : sb.append("PAK_SERVER_DeleteGroup 0x0052");
		break;
		case 0x0053 : sb.append("PAK_SERVER_ActivateUserSpell 0x0053"+System.lineSeparator()+
				"   2   int32 spell_id;"+System.lineSeparator()+
				"   6   int32 duration1;"+System.lineSeparator()+
				"   10   int32 duration2;"+System.lineSeparator()+
				"   14   int32 icon_id;"+System.lineSeparator()+
				"   18   string16 description;");
		break;
		case 0x0054 : sb.append("PAK_SERVER_SpellOff 0x0054"+System.lineSeparator()+
				"   2   int32 spell_id;");
		break;
		case 0x0055 : sb.append("PAK_SERVER_Unknown85 0x0055");
		break;
		case 0x0056 : sb.append("PAK_SERVER_Unknown86 0x0056");
		break;
		case 0x0057 : sb.append("PAK_SERVER_GroupMemberHealth 0x0057"+System.lineSeparator()+
				"   2   int32 player_id;"+System.lineSeparator()+
				"   6   int16 health_percentage;");
		break;
		case 0x0058 : sb.append("PAK_SERVER_ToggleGroupAutoSplit 0x0058"+System.lineSeparator()+
				"   2   int8 status; // 0 = disabled, 1 = enabled");
		break;
		case 0x0059 : sb.append("PAK_SERVER_Unknown89 0x0059");
		break;
		case 0x005A : sb.append("PAK_SERVER_PlayerExists 0x005A"+System.lineSeparator()+
				"   2   int8 status // 0 = unexistent, 1 = existent");
		break;
		case 0x005B : sb.append("PAK_SERVER_PatchServerInformation 0x005B"+System.lineSeparator()+
				"   int32 server_version;"+System.lineSeparator()+
				"   string16 url;"+System.lineSeparator()+
				"   string16 filename;"+System.lineSeparator()+
				"   string16 login;"+System.lineSeparator()+
				"   string16 password;"+System.lineSeparator()+
				"   int16 unknown1;"+System.lineSeparator());
				getInt32(pak, sb, "Server Version");
				getString16(pak, sb, "URL");
				getString16(pak, sb, "Filename");
				getString16(pak, sb, "Login");
				getString16(pak, sb, "Password");
				getInt16(pak, sb, "Unknown1");
		break;
		case 0x005C : sb.append("PAK_SERVER_CarriableWeight 0x005C // actualize current luggage and max carriable weight"+System.lineSeparator()+
				"   2   int32 current_weight;"+System.lineSeparator()+
				"   6   int32 max_weight;");
		break;
		case 0x005D : sb.append("PAK_SERVER_Rob 0x005D // show the robber the inventory of his victim"+System.lineSeparator()+
				"   2   int8 can_rob; // set to TRUE if player can rob his victim"+System.lineSeparator()+
				"   3   int32 player_id; // victim player ID in database"+System.lineSeparator()+
				"   7   string16 playername; // victim name"+System.lineSeparator()+
				"   int16 item_count; // number of items in inventory"+System.lineSeparator()+
				"   for (item_index = 0; item_index < item_count; item_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 item_skin_id; // item skin ID as defined in WDA"+System.lineSeparator()+
				"      int32 item_unit_id; // item ID in database"+System.lineSeparator()+
				"      int16 item_object_id; // item object ID as defined in WDA"+System.lineSeparator()+
				"      int32 quantity; // item quantity"+System.lineSeparator()+
				"      string16 itemname; // item name"+System.lineSeparator()+
				"   }");
		break;
		case 0x005E : sb.append("PAK_SERVER_DispelRob 0x005E"+System.lineSeparator()+
				"   int8 unknown; // ??? 01 hex"+System.lineSeparator()+
				"   int32 player_id; // victim player ID in database"+System.lineSeparator()+
				"   string16 playername; // victim name"+System.lineSeparator()+
				"   int16 item_count; // number of items in inventory"+System.lineSeparator()+
				"   for (item_index = 0; item_index < item_count; item_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 item_skin_id; // item skin ID as defined in WDA"+System.lineSeparator()+
				"      int32 item_unit_id; // item ID in database"+System.lineSeparator()+
				"      int16 item_object_id; // item object ID as defined in WDA"+System.lineSeparator()+
				"      int32 quantity; // item quantity"+System.lineSeparator()+
				"      string16 itemname; // item name"+System.lineSeparator()+
				"   }");
		break;
		case 0x005F : sb.append("PAK_SERVER_ArrowMissed 0x005F"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   int16 x_coord;"+System.lineSeparator()+
				"   8   int16 y_coord;"+System.lineSeparator()+
				"   10   int8 health_percentage;");
		break;
		case 0x0060 : sb.append("PAK_SERVER_ArrowSuccess 0x0060"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   int32 target_unit_id;"+System.lineSeparator()+
				"   10   int8 health_percentage;");
		break;
		case 0x0061 : sb.append("PAK_SERVER_ServerFlags 0x0061"+System.lineSeparator()+
				"   2   int8 scripts_enabled; // 1 = scripts enabled, 2 = ???"+System.lineSeparator()+
				"   3   int8 help_enabled; // 0 = disabled, 1 = enabled"+System.lineSeparator());
				getInt8(pak, sb, "Scripts Enabled");
				getInt8(pak, sb, "Help Enabled");
		break;
		case 0x0062 : sb.append("PAK_SERVER_SeraphArrival 0x0062"+System.lineSeparator()+
				"   2   int16 pak1_id; // 10004 (PAK_SERVER_PopupUnit)"+System.lineSeparator()+
				"   4   int16 x_coord;"+System.lineSeparator()+
				"   6   int16 y_coord;"+System.lineSeparator()+
				"   8   int16 skin_id;"+System.lineSeparator()+
				"   10   int32 unit_id;"+System.lineSeparator()+
				"   14   int8 light_percentage;"+System.lineSeparator()+
				"   15   int8 unit_type; // 0 = monster, 1 = NPC, 2 = player"+System.lineSeparator()+
				"   16   int8 health_percentage;"+System.lineSeparator()+
				"   17   int16 pak2_id; // 68 (PAK_SERVER_PuppetInformation)"+System.lineSeparator()+
				"   19   int32 unit_id;"+System.lineSeparator()+
				"   23   int16 body_type_id;"+System.lineSeparator()+
				"   25   int16 feet_type_id;"+System.lineSeparator()+
				"   27   int16 hands_type_id;"+System.lineSeparator()+
				"   29   int16 head_type_id;"+System.lineSeparator()+
				"   31   int16 legs_type_id;"+System.lineSeparator()+
				"   33   int16 weapon_type_id;"+System.lineSeparator()+
				"   35   int16 shield_type_id;"+System.lineSeparator()+
				"   37   int16 back_type_id;");
		break;
		case 0x0063 : sb.append("PAK_SERVER_AuthenticateVersion 0x0063"+System.lineSeparator()+
				"   2   int32 status; // (assumed) 0 = client version unusable, 1 = client version usable"+System.lineSeparator());
				getInt32(pak, sb, "Status");
		break;
		case 0x0064 : sb.append("PAK_SERVER_ResetRegistryInventory 0x0064");
		break;
		case 0x0065 : sb.append("PAK_SERVER_InfoMessage 0x0065"+System.lineSeparator()+
				"   2   int32 type; // message type (arbitrary)"+System.lineSeparator()+
				"   6   int32 color; // alpha blue green red format"+System.lineSeparator()+
				"   10  string16 message;");
		break;
		case 0x0066 : sb.append("PAK_SERVER_InfoMessage2 0x0066"+System.lineSeparator()+
				"   2   int32 type; // message type (arbitrary)"+System.lineSeparator()+
				"   6   int32 color; // alpha blue green red format"+System.lineSeparator()+
				"   10  string16 message;");
		break;
		case 0x0067 : sb.append("PAK_SERVER_MaxCharactersPerAccount 0x0067"+System.lineSeparator()+
				"   2   int8 max_characters; // max amount of characters allowed per account (usually 3)"+System.lineSeparator());
				getInt8(pak, sb, "Max Characters Per Account");
		break;
		case 0x0068 : sb.append("PAK_SERVER_ToggleWeather 0x0068"+System.lineSeparator()+
				"   2   int16 enabled; // set to 0x0001 to enable rain, 0x0000 to disable");
		break;
		case 0x0069 : sb.append("PAK_SERVER_OpenURL 0x0069"+System.lineSeparator()+
				"   2   string16 url_to_open; // goes straight into a ShellExecute, prefixed with \"http://\"");
		break;
		case 0x006A : sb.append("PAK_SERVER_ChestItems 0x006A"+System.lineSeparator()+
				"   int16 item_count; // number of items in chest"+System.lineSeparator()+
				"   for (item_index = 0; item_index < item_count; item_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 item_skin_id; // item skin ID as defined in WDA"+System.lineSeparator()+
				"      int32 item_unit_id; // item ID in database"+System.lineSeparator()+
				"      int16 item_object_id; // item object ID as defined in WDA"+System.lineSeparator()+
				"      int32 quantity; // item quantity"+System.lineSeparator()+
				"      int32 number_of_charges; // charges left in item (for ex. keys)"+System.lineSeparator()+
				"   }");
		break;
		case 0x006B : sb.append("PAK_SERVER_Unknown107 0x006B // unregistered");
		break;
		case 0x006C : sb.append("PAK_SERVER_Unknown108 0x006C // unregistered");
		break;
		case 0x006D : sb.append("PAK_SERVER_OpenChest 0x006D");
		break;
		case 0x006E : sb.append("PAK_SERVER_CloseChest 0x006E");
		break;
		case 0x006F : sb.append("PAK_SERVER_Undefined 0x006F");
		break;
		case 0x0074 : sb.append("PAK_SERVER_RequestExchange 0x0074 // only in 1.50 and newer"+System.lineSeparator()+
				"   int16 target_unit_id; // player ID of the target with whom to instantiate an exchange"+System.lineSeparator()+
				"   int16 item_count; // number of items in chest"+System.lineSeparator()+
				"   for (item_index = 0; item_index < item_count; item_index++)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      int16 item_skin_id; // item skin ID as defined in WDA"+System.lineSeparator()+
				"      int32 item_unit_id; // item ID in database"+System.lineSeparator()+
				"      int16 item_object_id; // item object ID as defined in WDA"+System.lineSeparator()+
				"      int32 quantity; // item quantity"+System.lineSeparator()+
				"      int32 number_of_charges; // charges left in item (for ex. keys)"+System.lineSeparator()+
				"   }");
		break;
		case 0x007A : sb.append("PAK_SERVER_ItemDescription 0x007A // only in 1.50 and newer"+System.lineSeparator()+
				"   2   int8 unit_type; // 0 = item unit; 1 = non-unit; 2 = non-item unit"+System.lineSeparator()+
				"   if (unit_type == 0)"+System.lineSeparator()+
				"   {"+System.lineSeparator()+
				"      string16 item_name;"+System.lineSeparator()+
				"      int16 item_skin_id;"+System.lineSeparator()+
				"      int8 light_percentage;"+System.lineSeparator()+
				"      int16 item_armor_class;"+System.lineSeparator()+
				"      int16 dodge_malus;"+System.lineSeparator()+
				"      int16 required_endurance;"+System.lineSeparator()+
				"      int32 minimum_damage;"+System.lineSeparator()+
				"      int32 maximum_damage;"+System.lineSeparator()+
				"      int16 required_attack;"+System.lineSeparator()+
				"      int16 required_strength;"+System.lineSeparator()+
				"      int16 required_agility;"+System.lineSeparator()+
				"      int16 required_wisdom;"+System.lineSeparator()+
				"      int16 required_intellect;"+System.lineSeparator()+
				"      int16 boost_count;"+System.lineSeparator()+
				"      for (boost_index = 0; boost_index < boost_count; boost_index++)"+System.lineSeparator()+
				"      {"+System.lineSeparator()+
				"          if (boosted_stat == 10001)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 27"+System.lineSeparator()+
				"          else if (boosted_stat == 10002)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 28"+System.lineSeparator()+
				"          else if (boosted_stat == 10004)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 29"+System.lineSeparator()+
				"          else if (boosted_stat == 10008)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 30"+System.lineSeparator()+
				"          else if (boosted_stat == 10009)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 31"+System.lineSeparator()+
				"          else if (boosted_stat == 10011)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 32"+System.lineSeparator()+
				"          else if (boosted_stat == 10012)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 33"+System.lineSeparator()+
				"          else if (boosted_stat == 10014)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 34"+System.lineSeparator()+
				"          else if (boosted_stat == 10015)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 35"+System.lineSeparator()+
				"          else if (boosted_stat == 10016)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 36"+System.lineSeparator()+
				"          else if (boosted_stat == 10017)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 37"+System.lineSeparator()+
				"          else if (boosted_stat == 10026)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 38"+System.lineSeparator()+
				"          else if (boosted_stat == 10027)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 39"+System.lineSeparator()+
				"          else if (boosted_stat == 10028)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 40"+System.lineSeparator()+
				"          else if (boosted_stat == 10029)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 41"+System.lineSeparator()+
				"          else if (boosted_stat == 10035)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 42"+System.lineSeparator()+
				"          else if (boosted_stat == 10036)"+System.lineSeparator()+
				"             int8 boosted_stat; // fixed, 43"+System.lineSeparator()+
				"          else"+System.lineSeparator()+
				"          {"+System.lineSeparator()+
				"             int8 boosted_stat;"+System.lineSeparator()+
				"             int32 minimum_boost; // only if player's wis & int >= item's wis & int, else 0"+System.lineSeparator()+
				"             int32 maximum_boost; // only if player's wis & int >= item's wis & int, else 0"+System.lineSeparator()+
				"          }"+System.lineSeparator()+
				"      }"+System.lineSeparator()+
				"   }");
		break;
		case 0x2711 : sb.append("PAK_SERVER_AttackMissed 0x2711"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   int32 target_unit_id; // BUG: always 0"+System.lineSeparator()+
				"   10   int8 light_percentage;"+System.lineSeparator()+
				"   11   int8 unit_type; // 0 = monster, 1 = NPC, 2 = player"+System.lineSeparator()+
				"   12   int8 health_percentage;"+System.lineSeparator()+
				"   13   int16 x_coord;"+System.lineSeparator()+
				"   15   int16 y_coord;"+System.lineSeparator()+
				"   17   int16 target_x_coord;"+System.lineSeparator()+
				"   19   int16 target_y_coord;");
		break;
		case 0x2712 : sb.append("PAK_SERVER_AttackSuccess 0x2712"+System.lineSeparator()+
				"   2   int32 unit_id;"+System.lineSeparator()+
				"   6   int32 target_unit_id; // BUG: always 0"+System.lineSeparator()+
				"   10   int16 x_coord;"+System.lineSeparator()+
				"   12   int16 y_coord;"+System.lineSeparator()+
				"   14   int16 target_x_coord;"+System.lineSeparator()+
				"   16   int16 target_y_coord;");
		break;
		case 0x2713 : sb.append("PAK_SERVER_SkillSuccess 0x2713"+System.lineSeparator()+
				"   2   int16 skill_id;"+System.lineSeparator()+
				"   4   int16 value; // unused ?");
		break;
		case 0x2714 : sb.append("PAK_SERVER_PopupUnit 0x2714"+System.lineSeparator()+
				"   2   int16 x_coord;"+System.lineSeparator()+
				"   4   int16 y_coord;"+System.lineSeparator()+
				"   6   int16 skin_id;"+System.lineSeparator()+
				"   8   int32 unit_id;"+System.lineSeparator()+
				"   12   int8 light_percentage;"+System.lineSeparator()+
				"   13   int8 unit_type; // 0 = monster, 1 = NPC, 2 = player"+System.lineSeparator()+
				"   14   int8 health_percentage;");
		break;
		//Pak pour les clients NMS uniquement

		case 0x00C8 : sb.append("PAK_SERVER_DeathStatus 0x00C8"+System.lineSeparator()+
				"   2   int8 death_flag; (1 = Death; 0 = Not death)");
		break;
		case 0x00C9 : sb.append("PAK_SERVER_DeathProgress 0x00C9"+System.lineSeparator()+
				"   2   int16 res_countdown;"+System.lineSeparator()+
				"   3   int16 res_countdown_max;"+System.lineSeparator()+
				"   5   int8 resurrection_button_flag; (1 = visible; 0 = invisible)");
		break;
		case 0x00CA : sb.append("PAK_SERVER_DeathResurection 0x00CA");
		break;
		}
		sb.append(System.lineSeparator());
		
		return sb.toString();
	}

}
