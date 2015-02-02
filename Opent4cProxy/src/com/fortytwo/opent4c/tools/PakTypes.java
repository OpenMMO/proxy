package com.fortytwo.opent4c.tools;

import java.nio.ByteBuffer;

public class PakTypes {

	public static String getTypeInfos(boolean isServerToClient, short type, ByteBuffer pak) {
		if(isServerToClient)return getServerTypeInfos(type, pak);
		return getClientTypeInfos(type, pak);
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
		break;
		case 0x000C : sb.append("PAK_CLIENT_DepositObject 0x000C"+System.lineSeparator()+
				"   2   int16 x_coord; // object's X coordinate on map"+System.lineSeparator()+
				"   4   int16 y_coord; // object's Y coordinate on map"+System.lineSeparator()+
				"   6   int32 object_id; // object unit ID"+System.lineSeparator()+
				"   10   int32 item_count; // amount of same objects in this unit, if the objects are stacked");
		break;
		case 0x000D : sb.append("PAK_CLIENT_PutPlayerInGame 0x000D"+System.lineSeparator()+
				"   2   string8 playername;");
		break;
		case 0x000E : sb.append("PAK_CLIENT_Login 0x000E"+System.lineSeparator()+
				"   2   string8 login;"+System.lineSeparator()+
				"   2+[1+S]   string8 password;"+System.lineSeparator()+
				"   2+[1+S]+[1+S]+2   int16 client_version_number;"+System.lineSeparator()+
				"   2+[1+S]+[1+S]+4   int16 unknown;");
		break;
		case 0x000F : sb.append("");
		break;
		case 0x0010 : sb.append("");
		break;
		case 0x0011 : sb.append("");
		break;
		case 0x0012 : sb.append("");
		break;
		case 0x0013 : sb.append("");
		break;
		case 0x0014 : sb.append("");
		break;
		case 0x0015 : sb.append("");
		break;
		case 0x0016 : sb.append("");
		break;
		case 0x0017 : sb.append("");
		break;
		case 0x0018 : sb.append("");
		break;
		case 0x0019 : sb.append("");
		break;
		case 0x001A : sb.append("");
		break;
		case 0x001B : sb.append("");
		break;
		case 0x001C : sb.append("");
		break;
		case 0x001D : sb.append("");
		break;
		case 0x001E : sb.append("");
		break;
		case 0x001F : sb.append("");
		break;
		case 0x0020 : sb.append("");
		break;
		case 0x0021 : sb.append("");
		break;
		case 0x0022 : sb.append("");
		break;
		case 0x0023 : sb.append("");
		break;
		case 0x0024 : sb.append("");
		break;
		case 0x0025 : sb.append("");
		break;
		case 0x0042 : sb.append("PAK_CLIENT_MessageOfTheDay 0x0042");
		break;
	
		}
		sb.append(System.lineSeparator());
		
		return sb.toString();
	}
	
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
		case 0x0002 : sb.append("N/A");
		break;
		case 0x0003 : sb.append("N/A");
		break;
		case 0x0004 : sb.append("N/A");
		break;
		case 0x0005 : sb.append("N/A");
		break;
		case 0x0006 : sb.append("N/A");
		break;
		case 0x0007 : sb.append("N/A");
		break;
		case 0x0008 : sb.append("N/A");
		break;
		case 0x0009 : sb.append("PAK_SERVER_SynchronizePlayerCoordinates 0x0009 // server is making sure client is at the right location"+System.lineSeparator()+
				"   2   int16 x_coord;"+System.lineSeparator()+
				"   4   int16 y_coord;"+System.lineSeparator()+
				"   6   int16 world;");
		break;
		case 0x000A : sb.append("PAK_SERVER_KeepAlive 0x000A // Ping");
		break;
		case 0x000B : sb.append("N/A");
		break;
		case 0x000C : sb.append("N/A");
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
				"   69   int64 base_level_xp;");
		break;
		case 0x000E : sb.append("PAK_SERVER_Login 0x000E"+System.lineSeparator()+
				"   2    int8 status; // 0 = welcome, 1 = can't login, 2 = already logged in"+System.lineSeparator()+
				"   3    string16 message;");
		break;
		case 0x000F : sb.append("");
		break;
		case 0x0010 : sb.append("");
		break;
		case 0x0011 : sb.append("");
		break;
		case 0x0012 : sb.append("");
		break;
		case 0x0013 : sb.append("");
		break;
		case 0x0014 : sb.append("");
		break;
		case 0x0015 : sb.append("");
		break;
		case 0x0016 : sb.append("");
		break;
		case 0x0017 : sb.append("");
		break;
		case 0x0018 : sb.append("");
		break;
		case 0x0019 : sb.append("");
		break;
		case 0x001A : sb.append("");
		break;
		case 0x001B : sb.append("");
		break;
		case 0x001C : sb.append("");
		break;
		case 0x001D : sb.append("");
		break;
		case 0x001E : sb.append("");
		break;
		case 0x001F : sb.append("");
		break;
		case 0x0020 : sb.append("");
		break;
		case 0x0021 : sb.append("");
		break;
		case 0x0022 : sb.append("");
		break;
		case 0x0023 : sb.append("");
		break;
		case 0x0024 : sb.append("");
		break;
		case 0x0025 : sb.append("");
		break;
		case 0x0026 : sb.append("");
		break;
		case 0x0042 : sb.append("PAK_SERVER_MessageOfTheDay 0x0042"+System.lineSeparator()+
				"   2   string16 MOTD size : ");
		pak.rewind();
		pak.getShort();
		short size = pak.getShort();
		sb.append(size);
		sb.append(System.lineSeparator());
		sb.append("MOTD : ");
		byte[] motd = new byte[size];
		pak.get(motd,0,size);
		sb.append(new String(motd));
		sb.append(System.lineSeparator());
		break;
		}
		sb.append(System.lineSeparator());
		
		return sb.toString();
	}
/**

#define 
#define 

#define 

#define 

#define 
#define 

#define 
#define 

#define PAK_CLIENT_DeletePlayer 0x000F
//   2   string8 playername;
#define PAK_SERVER_DeletePlayer 0x000F
//   2    int8 status; // 0 = no error, 1 = error

#define PAK_SERVER_RemoveUnit 0x000B
//   2   int8 unknown; // deprecated, set it to 0
//   3   int32 unit_id; // unit ID

#define PAK_SERVER_SetUnitAppearance 0x000C
//   2   int16 skin_id; // unit's skin ID (as referenced in the T4C Worlds.wda file)
//   4   int32 unit_id; // unit ID

#define PAK_CLIENT_SendPeriphericObjects 0x0010
//   2   int8 player_orientation; // player character's orientation (9: ignore orientation)
//   3   int16 x_coord; // player character's X coordinates on map
//   5   int16 y_coord; // player character's Y coordinates on map

#define PAK_SERVER_SendPeriphericObjects 0x0010
//   2   int16 unit_count; // number of units in the array below
//               for (i = 0; i < unit_count; i++) // for each unit...
//               {
//   2+i*13         int16 x_coord; // unit's X coordinate on map
//   2+i*13+2       int16 y_coord; // unit's Y coordinate on map
//   2+i*13+4       int16 skin_id; // unit skin ID, as referenced in the T4C Worlds WDA file
//   2+i*13+6       int32 unit_id; // unit ID
//   2+i*13+10      int8 light_percentage; // unit radiance from 0 to 100
//   2+i*13+11      int8 unit_type; // 0 = monster, 1 = NPC, 2 = player
//   2+i*13+12      int8 health_percentage; // unit's visual health from 0 (red) to 100 (green)
//               }

#define PAK_CLIENT_Unknown17 0x0011
#define PAK_SERVER_Unknown17 0x0011

#define PAK_CLIENT_BackpackItems 0x0012
//   2   int16 unknown;
#define PAK_SERVER_BackpackItems 0x0012
//   2   int8 popup_backpack; // set to TRUE if the client should pop up the backpack on screen
//   3   int32 player_id; // player ID, as read in the database
//   7   int16 item_count; // amount of items this player has in backpack
//               for (item_index = 0; item_index < item_count; item_index++)
//               {
//   9+i*16         int16 icon_id; // item skin ID, the full list is in the T4C Worlds.wda file
//   9+i*16+2       int32 unit_id; // item instance ID (unique), to be saved in the database
//   9+i*16+6       int16 object_id; // item ID as read in the WDAs
//   9+i*16+8       int32 quantity; // amount of items, if this object is stacked with others
//   9+i*16+12      int32 number_of_charges; // number of charges this object has (for ex, keys)
//               }

#define PAK_CLIENT_EquippedItems 0x0013
#define PAK_SERVER_EquippedItems 0x0013
//   2   int8 Ranged_Attack;
//   3   int32 body_item_unit_id;
//   7   int16 body_icon_id;
//   int16 body_object_id;
//   int16 body_item_count;
//   int32 body_unknown;
//   string16 body_itemname;
//   int32 hands_item_unit_id;
//   int16 hands_icon_id;
//   int16 hands_object_id;
//   int16 hands_item_count;
//   int32 hands_unknown;
//   string16 hands_itemname;
//   int32 head_item_unit_id;
//   int16 head_icon_id;
//   int16 head_object_id;
//   int16 head_item_count;
//   int32 head_unknown;
//   string16 head_itemname;
//   int32 legs_item_unit_id;
//   int16 legs_icon_id;
//   int16 legs_object_id;
//   int16 legs_item_count;
//   int32 legs_unknown;
//   string16 legs_itemname;
//   int32 bracers_item_unit_id;
//   int16 bracers_icon_id;
//   int16 bracers_object_id;
//   int16 bracers_item_count;
//   int32 bracers_unknown;
//   string16 bracers_itemname;
//   int32 neck_item_unit_id;
//   int16 neck_icon_id;
//   int16 neck_object_id;
//   int16 neck_item_count;
//   int32 neck_unknown;
//   string16 neck_itemname;
//   int32 rhand_item_unit_id;
//   int16 rhand_icon_id;
//   int16 rhand_object_id;
//   int16 rhand_item_count;
//   int32 rhand_unknown;
//   string16 rhand_itemname;
//   int32 lhand_item_unit_id;
//   int16 lhand_icon_id;
//   int16 lhand_object_id;
//   int16 lhand_item_count;
//   int32 lhand_unknown;
//   string16 lhand_itemname;
//   int32 rring_item_unit_id;
//   int16 rring_icon_id;
//   int16 rring_object_id;
//   int16 rring_item_count;
//   int32 rring_unknown;
//   string16 rring_itemname;
//   int32 lring_item_unit_id;
//   int16 lring_icon_id;
//   int16 lring_object_id;
//   int16 lring_item_count;
//   int32 lring_unknown;
//   string16 lring_itemname;
//   int32 belt_item_unit_id;
//   int16 belt_icon_id;
//   int16 belt_object_id;
//   int16 belt_item_count;
//   int32 belt_unknown;
//   string16 belt_itemname;
//   int32 back_item_unit_id;
//   int16 back_icon_id;
//   int16 back_object_id;
//   int16 back_item_count;
//   int32 back_unknown;
//   string16 back_itemname;
//   int32 feet_item_unit_id;
//   int16 feet_icon_id;
//   int16 feet_object_id;
//   int16 feet_item_count;
//   int32 feet_unknown;
//   string16 feet_itemname;



#define PAK_CLIENT_ExitGame 0x0014
#define PAK_SERVER_ServerShutdown 0x0014

#define PAK_CLIENT_EquipObject 0x0015
//   2   int32 item_id;
#define PAK_SERVER_Unknown21 0x0015

#define PAK_CLIENT_UnequipObject 0x0016
//   2   int8 equipment_position;
#define PAK_SERVER_Unknown22 0x0016

#define PAK_CLIENT_UseObject 0x0017
//   2   int16 x_coord;
//   4   int16 y_coord;
//   6   int32 item_unit_id;
#define PAK_SERVER_Unknown23 0x0017

#define PAK_CLIENT_Attack 0x0018
//   2   int16 x_coord;
//   4   int16 y_coord;
//   6   int32 unit_id;
#define PAK_SERVER_Unknown24 0x0018

#define PAK_CLIENT_CreatePlayer 0x0019
//   2   int8 dexterity;
//   3   int8 endurance;
//   4   int8 intelligence;
//   5   int8 strength;
//   6   int8 wisdom;
//   7   int8 unknown; // probably gender
//   8   string8 playername;
#define PAK_SERVER_RollDices 0x0019
//   2   int8 unknown;
//   3   int8 dexterity;
//   4   int8 endurance;
//   5   int8 intelligence;
//   6   int8 luck; // unused
//   7   int8 strength;
//   8   int8 willpower; // unused
//   9   int8 wisdom;
//   10   int32 health;
//   14   int32 max_health;
//   18   int16 mana;
//   20   int16 max_mana;

#define PAK_CLIENT_PersonalPCList 0x001A
#define PAK_SERVER_PersonalPCList 0x001A
//   int8 character_count;
//   for (character_index = 0; character_index < character_count; character_index++)
//   {
//      string8 playername;
//      int16 skin_id;
//      int16 level;
//   }

#define PAK_CLIENT_IndirectTalk 0x001B // local talk
//   2   int32 unit_id; // player ID in database
//   6   int8 unit_orientation; // usually 0 (means don't change current orientation)
//   7   int32 text_color; // seems unused, usually 00 00 BE BE
//   11   string16 message;
#define PAK_SERVER_IndirectTalk 0x001B
//   2   int32 sender_id;
//   6   int8 sender_orientation;
//   7   int32 text_color; // unused
//   11   int8 sender_type; // 0 = player, 1 = NPC
//   12   string16 message;
//   12+[2+S]   string16 sender_playername;
//   12+[2+S]+[2+S] Int32 unknown; // 1.65: seems to always be 0xFF

#define PAK_CLIENT_Shout 0x001C // main channel talk
//   2   string16 playername; // ignored (corrected by server if wrong)
//   2+[2+S]   int32 text_color; // unused
//   2+[2+S]+4   string16 message;
#define PAK_SERVER_Unknown28 0x001C

#define PAK_CLIENT_Page 0x001D // whisp
//   2   string16 target_playername;
//   2+[2+S]   string16 message;
#define PAK_SERVER_Page 0x001D
//   2   string16 sender_playername;
//   2+[2+S]   string16 message;

#define PAK_CLIENT_DirectedTalk 0x001E
//   2   int16 target_x_coord;
//   4   int16 target_y_coord;
//   6   int32 target_unit_id;
//   10   int8 orientation;
//   11   int32 text_color; // unused
//   15   string16 message;
#define PAK_SERVER_Unknown30 0x001E

#define PAK_CLIENT_Reroll 0x001F
#define PAK_SERVER_Reroll 0x001F
//   2   int8 dexterity;
//   3   int8 endurance;
//   4   int8 intelligence;
//   5   int8 luck; // unused
//   6   int8 strength;
//   7   int8 willpower; // unused
//   8   int8 wisdom;
//   9   int32 health;
//   13  int32 max_health;
//   17  int16 mana;
//   19  int16 max_mana;

#define PAK_CLIENT_CastSpell 0x0020
//   2   int16 spell_id;
//   4   int16 target_x_coord;
//   6   int16 target_y_coord;
//   8   int32 target_unit_id;
#define PAK_SERVER_Unknown32 0x0020



#define PAK_CLIENT_UpdateLife 0x0021
#define PAK_SERVER_UpdateLife 0x0021
//   2   int32 health;
//   6   int32 max_health;

#define PAK_CLIENT_UnitName 0x0023
//   2   int32 unit_id;
//   6   int16 x_coord;
//   8   int16 y_coord;
#define PAK_SERVER_UnitName 0x0023
//   2   int32 unit_id;
//   6   string16 unitname;
//   6+[2+S] Int32 color; // caught on 1.65 server
//   10+[2+S] string16 name_2; // the text that appears under the player's name. caught on 1.65


#define PAK_CLIENT_BroadcastTextChange 0x0022
//   2   int32 npc_unit_id;
//   6   int16 start_line_number;
#define PAK_SERVER_BroadcastTextChange 0x0022
//   2   int32 npc_unit_id;
//   6   int16 start_line_number;

#define PAK_CLIENT_BreakConversation 0x0024
//   2   int32 npc_unit_id;
//   6   int16 npc_x_position;
//   8   int16 npc_y_position;
#define PAK_SERVER_BreakConversation 0x0024

#define PAK_CLIENT_Unknown37 0x0025
#define PAK_SERVER_UpdateLevel 0x0025
//   2   int16 level;
//   4   int64 xp_left_before_next_level;
//   12   int32 health;
//   16   int32 max_health;
//   20   int16 mana;
//   22   int16 max_mana;

#define PAK_CLIENT_ReturnToMenu 0x0026
#define PAK_SERVER_ReturnToMenu 0x0026
//   2   int8 status; // 0 = OK, 1 = error

#define PAK_CLIENT_PersonalSkills 0x0027 // request to get the skill list in the stats page
#define PAK_SERVER_PersonalSkills 0x0027
//   2   int16 skill_count;
//   for (skill_index = 0; skill_index < skill_count; skill_index++)
//   {
//      int16 skill_id;
//      int8 use; // 0 = permanent, 1 = on trigger, 3 = specify target
//      int16 value;
//      int16 boosted_value;
//      string16 skillname;
//      string16 description;
//   }

#define PAK_CLIENT_TrainList 0x0028
//   2   int16 target_x_coord;
//   4   int16 target_y_coord;
//   6   int32 target_unit_id;
//   10   int16 skill_count;
//              for (i = 0; i < skill_count; i++)
//              {
//   12+i*4        int16 skill_id;
//   12+i*4+2      int16 points_count;
//              }
#define PAK_SERVER_TrainList 0x0028
//   int16 available_points;
//   int16 skill_count;
//   for (skill_index = 0; skill_index < skill_count; skill_index++)
//   {
//      int8 status; // 0 = can't train this skill, 1 = can train this skill
//      int16 skill_id;
//      int32 current_skill_value;
//      int16 max_skill_value;
//      int16 price;
//      string16 skillname;
//   }

#define PAK_CLIENT_BuyList 0x0029
//   2   int16 x_coord; // target
//   4   int16 y_coord; // target
//   6   int32 unit_id; // target
//   10  int16 itemtype_count;
//              for (i = 0; i < itemtype_count; i++)
//              {
//   12+i*4        int16 object_id;
//   12+i*4+2      int16 item_count;
//              }
#define PAK_SERVER_BuyList 0x0029
//   int32 player_gold;
//   int16 item_count;
//   for (item_index = 0; item_index < item_count; item_index++)
//   {
//      int16 object_id;
//      int16 icon_id;
//      int32 price;
//      int8 status; // 0 = can't buy this item, 1 = can buy this item
//      string16 itemname;
//      string16 description;
//   }

#define PAK_CLIENT_UseSkill 0x002A // request to use a particular skill on a target
//   2   int16 skill_id; // skill ID
//   4   int16 target_x_coord; // target's X coordinate
//   6   int16 target_y_coord; // target's Y coordinate
//   8   int32 target_unit_id; // target unit ID (if player, player ID in database)
#define PAK_SERVER_Unknown42 0x002A

#define PAK_CLIENT_PersonalStats 0x002B // request to get the stats list (usually to display the stats page)
#define PAK_SERVER_PersonalStats 0x002B
//   2   int32 health;
//   6   int32 max_health;
//   10   int16 mana;
//   12   int16 max_mana;
//   14   int64 xp;
//   22   int16 boosted_armor;
//   24   int16 armor;
//   26   int16 boosted_strength;
//   28   int16 boosted_endurance;
//   30   int16 boosted_dexterity;
//   32   int16 boosted_willpower; // unused
//   34   int16 boosted_wisdom;
//   36   int16 boosted_intelligence;
//   38   int16 boosted_luck; // unused
//   40   int16 stat_points;
//   42   int16 strength;
//   44   int16 endurance;
//   46   int16 dexterity;
//   48   int16 willpower; // unused
//   50   int16 wisdom;
//   52   int16 intelligence;
//   54   int16 luck; // unused
//   56   int16 level;
//   58   int16 skill_points;
//   60   int16 luggage;
//   62   int16 max_luggage;
//   64   int16 karma;
//   66   int16 base_health;

#define PAK_CLIENT_UpdateXP 0x002C
#define PAK_SERVER_UpdateXP 0x002C
//   2   int64 xp;

#define PAK_CLIENT_UpdateTime 0x002D
#define PAK_SERVER_UpdateTime 0x002D
//   2   int8 seconds;
//   3   int8 minutes;
//   4   int8 hours;
//   5   int8 week;
//   6   int8 day;
//   7   int8 month;
//   8   int16 year;

#define PAK_CLIENT_FromPreInGameToInGame 0x002E
#define PAK_SERVER_FromPreInGameToInGame 0x002E
//   int8 ²; // 0 = data following, 1 = no data following
//   if (!no_data)
//   {
//      int16 unknown; // seems useless, always 0x0000
//      int16 unit_count;
//      for (unit_index = 0; unit_index < unit_count; unit_index++)
//      {
//         int16 x_coord;
//         int16 y_coord;
//         int16 skin_id;
//         int32 unit_id;
//         int8 light_percentage;
//         int8 unit_type; // 0 = monster, 1 = NPC, 2 = player
//         int8 health_percentage;
//      }
//   }

#define PAK_CLIENT_Unknown47 0x002F // unused (probably obsolete)
#define PAK_SERVER_Unknown47 0x002F // unused (probably obsolete)

#define PAK_CLIENT_EnterChannel 0x0030
//   2   string16 channelname;
//   2+[2+S]   string16 password;
#define PAK_SERVER_Unknown48 0x0030

#define PAK_CLIENT_ChannelMessage 0x0031
//   2   string16 channelname;
//   2+[2+S]   string16 message;
#define PAK_SERVER_ChannelMessage 0x0031
//   string16 channelname;
//   string16 playername;
//   string16 message;

#define PAK_CLIENT_ChannelUsers 0x0032
//   2   string16 channelname;
#define PAK_SERVER_ChannelUsers 0x0032
//   string16 channelname;
//   int16 player_count;
//   for (player_index = 0; player_index < player_count; player_index++)
//   {
//      string16 playername;
//      string16 playertitle;
//      int8 status; // 0 = has closed channel, 1 = has opened channel
//   }

#define PAK_CLIENT_PlayerList 0x0033
#define PAK_SERVER_PlayerList 0x0033

#define PAK_CLIENT_GetSkillStatPoints 0x0034
#define PAK_SERVER_Unknown52 0x0034

#define PAK_CLIENT_UpdateGold 0x0035
#define PAK_SERVER_UpdateGold 0x0035
//   2   int32 gold;

#define PAK_CLIENT_ViewGroundItemIndentContent 0x0036
#define PAK_SERVER_Unknown54 0x0036

#define PAK_CLIENT_TeachList 0x0037
//   2   int16 target_x_coord;
//   4   int16 target_y_coord;
//   6   int32 target_unit_id;
//   for (;;)
//   {
//      int16 skill_id;
//   }
#define PAK_SERVER_TeachList 0x0037
//   int16 player_skillpoints;
//   int16 skill_count;
//   for (skill_index = 0; skill_index < skill_count; skill_index++)
//   {
//      int8 status; // 0 = can't be taught to player, 1 = can be taught to player
//      int16 skill_id;
//      int32 price;
//      string16 skillname;
//      string16 description;
//      int32 cost_in_points;
//      int32 skill_icon_id;
//   }

#define PAK_CLIENT_SellList 0x0038
//   2   int16 x_coord;
//   4   int16 y_coord;
//   6   int32 buyer_npc_id;
//   for (;;)
//   {
//      10   int32 item_unit_id;
//      14   int32 quantity;
//   }
#define PAK_SERVER_SellList 0x0038
//   int32 player_gold;
//   int16 item_count;
//   for (item_index = 0; item_index < item_count; item_index++)
//   {
//      int32 item_unit_id;
//      int16 item_type_id;
//      int32 price;
//      int32 quantity;
//      string16 itemname;
//   }

#define PAK_CLIENT_Unknown57 0x0039
#define PAK_SERVER_TeleportPlayer 0x0039
//   2   int16 x_coord;
//   4   int16 y_coord;
//   6   int16 world;

#define PAK_CLIENT_SendStatTrain 0x003A
//   2   int8 strength;
//   3   int8 endurance;
//   4   int8 dexterity;
//   5   int8 willpower; // unused
//   6   int8 wisdom;
//   7   int8 intelligence;
//   8   int8 luck; // unused
#define PAK_SERVER_Unknown58 0x003A

#define PAK_CLIENT_ItemName 0x003B
//   2   int32 item_id;
#define PAK_SERVER_ItemName 0x003B
//   2   int32 object_id;
//   6   string16 itemname;

#define PAK_CLIENT_GetNearItems 0x003C
#define PAK_SERVER_NoUnitInView 0x003C

#define PAK_CLIENT_Unknown61 0x003D // unused (probably obsolete)
#define PAK_SERVER_Unknown61 0x003D // unused (probably obsolete)

#define PAK_CLIENT_PersonalSpellList 0x003E
//   2   int8 unknown; // 0x01 if god
#define PAK_SERVER_PersonalSpellList 0x003E
//   int8 unknown; // unused (probably obsolete), always 0x00
//   int16 mana;
//   int16 max_mana;
//   int16 spell_count;
//   for (spell_index = 0; spell_index < spell_count; spell_index++)
//   {
//      int16 spell_id;
//      int8 target_type;
//      int16 mana_cost;
//      int16 unknown1;
//      int16 unknown2;
//      int16 unknown3;
//      int16 unknown4;
//      int16 unknown5;
//      int16 unknown6;
//      int16 spell_icon;
//      string16 description;
//      string16 spellname;
//   }

#define PAK_CLIENT_Unknown63 0x003F
#define PAK_SERVER_ServerMessage 0x003F
//   2   int32 unused; // was color, but client sets it to 0x00FF6400 (blue). Can be nulled.
//   6   string16 message;

#define PAK_CLIENT_Unknown64 0x0040
#define PAK_SERVER_CastingSpell 0x0040
//   2   int16 effect_id;
//   4   int32 source_id;
//   8   int32 target_id;
//   12   int16 target_x_coord;
//   14   int16 target_y_coord;
//   16   int16 source_x_coord;
//   18   int16 source_y_coord;
//   20   int32 created_unit_id;
//   24   int32 unknown;

#define PAK_CLIENT_ServerInformation 0x0041
#define PAK_SERVER_ServerInformation 0x0041
//   int32 version;
//   int16 patchserver_count;
//   for (patchserver_index = 0; patchserver_index < patchserver_count; patchserver_index++)
//   {
//      int16 port_number;
//      string16 address;
//   }

#define 
#define 

#define PAK_CLIENT_Unknown67 0x0043
#define PAK_SERVER_UpdateMana 0x0043
//   2   int16 mana;

#define PAK_CLIENT_PuppetInformation 0x0044
//   2   int32 unit_id;
//   6   int16 x_coord;
//   8   int16 y_coord;
#define PAK_SERVER_PuppetInformation 0x0044
//   2   int32 unit_id;
//   6   int16 body_type_id;
//   8   int16 feet_type_id;
//   10   int16 hands_type_id;
//   12   int16 head_type_id;
//   14   int16 legs_type_id;
//   16   int16 weapon_type_id;
//   18   int16 shield_type_id;
//   20   int16 back_type_id;

#define PAK_CLIENT_Unknown69 0x0045
#define PAK_SERVER_UnitAppearance 0x0045
//   2   int16 skin_id;
//   4   int32 unit_id;
//   8   int8 light_percentage;
//   9   int8 unit_type; // 0 = monster, 1 = NPC, 2 = player
//   10   int8 health_percentage;

#define PAK_CLIENT_Unknown70 0x0046
#define PAK_SERVER_UnitNotFound 0x0046
//   2   int32 unit_id;
//   6   int16 in_reply_to_pak_type;

#define PAK_CLIENT_QueryUnitExistence 0x0047
//   2   int32 unit_id;
//   6   int16 x_coord;
//   8   int16 y_coord;
#define PAK_SERVER_Unknown71 0x0047

#define PAK_CLIENT_UseItemByAppearance 0x0048
//   2   int16 object_id;
#define PAK_SERVER_Unknown72 0x0048

#define PAK_CLIENT_Unknown73 0x0049
#define PAK_SERVER_NoMoreItem 0x0049
//   2   int16 item_id;

#define PAK_CLIENT_RemoveFromChatterChannel 0x004A
//   2   string16 channelname;
#define PAK_SERVER_Unknown74 0x004A

#define PAK_CLIENT_ChannelList 0x004B
#define PAK_SERVER_ChannelList 0x004B
//   int16 channel_count;
//   for (channel_index = 0; channel_index < channel_count; channel_index++)
//   {
//      string16 channelname;
//      int8 status; // 0 = closed, 1 = open
//   }

#define PAK_CLIENT_Unknown76 0x004C
#define PAK_SERVER_CreateGroup 0x004C
//   int8 unknown; // unused (probably obsolete), always 0x01
//   int16 player_count;
//   for (player_index = 0; player_index < player_count; player_index++)
//   {
//      int32 player_unit_id;
//      int16 player_level;
//      int16 health_percentage;
//      int8 is_creator; // set to TRUE if this player is the leader of this group
//      string16 playername;
//   }

#define PAK_CLIENT_Unknown77 0x004D
#define PAK_SERVER_Unknown77 0x004D

#define PAK_CLIENT_GroupInvite 0x004E
//   2   int32 target_player_unit_id;
//   6   int16 x_coord;
//   8   int16 y_coord;
#define PAK_SERVER_GroupInvite 0x004E
//   2   int32 player_unit_id;
//   6   string16 message;

#define PAK_CLIENT_GroupJoin 0x004F
#define PAK_SERVER_Unknown79 0x004F

#define PAK_CLIENT_GroupLeave 0x0050
#define PAK_SERVER_GroupLeave 0x0050

#define PAK_CLIENT_GroupKick 0x0051
//   2   int32 player_unit_id;
#define PAK_SERVER_Unknown81 0x0051

#define PAK_CLIENT_Unknown82 0x0052
#define PAK_SERVER_DeleteGroup 0x0052

#define PAK_CLIENT_Unknown83 0x0053
#define PAK_SERVER_ActivateUserSpell 0x0053
//   2   int32 spell_id;
//   6   int32 duration1;
//   10   int32 duration2;
//   14   int32 icon_id;
//   18   string16 description;

#define PAK_CLIENT_Unknown84 0x0054
#define PAK_SERVER_SpellOff 0x0054
//   2   int32 spell_id;

#define PAK_CLIENT_JunkItems 0x0055
//   2   int32 item_id;
//   6   int32 item_count;
#define PAK_SERVER_Unknown85 0x0055

#define PAK_CLIENT_ToggleChatterListening 0x0056
//   2   string16 channelname;
//   [2+S]  int8 is_enabled; // 0 = channel listening disabled, 1 = channel listening enabled
#define PAK_SERVER_Unknown86 0x0056

#define PAK_CLIENT_Unknown87 0x0057
#define PAK_SERVER_GroupMemberHealth 0x0057
//   2   int32 player_id;
//   6   int16 health_percentage;

#define PAK_CLIENT_ToggleGroupAutoSplit 0x0058
//   2   int8 unknown; // usually 0
#define PAK_SERVER_ToggleGroupAutoSplit 0x0058
//   2   int8 status; // 0 = disabled, 1 = enabled

#define PAK_CLIENT_TogglePages 0x0059
//   2   int8 status; // 0 = pages disabled, 1 = pages enabled
#define PAK_SERVER_Unknown89 0x0059

#define PAK_CLIENT_PlayerExists 0x005A
//   2   string16 playername;
#define PAK_SERVER_PlayerExists 0x005A
//   2   int8 status // 0 = unexistent, 1 = existent

#define PAK_CLIENT_PatchServerInformation 0x005B
#define PAK_SERVER_PatchServerInformation 0x005B
//   int32 server_version;
//   string16 url;
//   string16 filename;
//   string16 login;
//   string16 password;
//   int16 unknown1;

#define PAK_CLIENT_Unknown92 0x005C
#define PAK_SERVER_CarriableWeight 0x005C // actualize current luggage and max carriable weight
//   2   int32 current_weight;
//   6   int32 max_weight;

#define PAK_CLIENT_Rob 0x005D // request to rob a certain item in the victim's inventory
//   2   int32 item_id; // ID of item to rob in the victim's inventory
#define PAK_SERVER_Rob 0x005D // show the robber the inventory of his victim
//   2   int8 can_rob; // set to TRUE if player can rob his victim
//   3   int32 player_id; // victim player ID in database
//   7   string16 playername; // victim name
//   int16 item_count; // number of items in inventory
//   for (item_index = 0; item_index < item_count; item_index++)
//   {
//      int16 item_skin_id; // item skin ID as defined in WDA
//      int32 item_unit_id; // item ID in database
//      int16 item_object_id; // item object ID as defined in WDA
//      int32 quantity; // item quantity
//      string16 itemname; // item name
//   }

#define PAK_CLIENT_DispelRob 0x005E
#define PAK_SERVER_DispelRob 0x005E
//   int8 unknown; // ??? 01 hex
//   int32 player_id; // victim player ID in database
//   string16 playername; // victim name
//   int16 item_count; // number of items in inventory
//   for (item_index = 0; item_index < item_count; item_index++)
//   {
//      int16 item_skin_id; // item skin ID as defined in WDA
//      int32 item_unit_id; // item ID in database
//      int16 item_object_id; // item object ID as defined in WDA
//      int32 quantity; // item quantity
//      string16 itemname; // item name
//   }

#define PAK_CLIENT_Unknown95 0x005F
#define PAK_SERVER_ArrowMissed 0x005F
//   2   int32 unit_id;
//   6   int16 x_coord;
//   8   int16 y_coord;
//   10   int8 health_percentage;

#define PAK_CLIENT_Unknown96 0x0060
#define PAK_SERVER_ArrowSuccess 0x0060
//   2   int32 unit_id;
//   6   int32 target_unit_id;
//   10   int8 health_percentage;

#define PAK_CLIENT_Unknown97 0x0061
#define PAK_SERVER_ServerFlags 0x0061
//   2   int8 scripts_enabled; // 1 = scripts enabled, 2 = ???
//   3   int8 help_enabled; // 0 = disabled, 1 = enabled

#define PAK_CLIENT_Unknown98 0x0062
#define PAK_SERVER_SeraphArrival 0x0062
//   2   int16 pak1_id; // 10004 (PAK_SERVER_PopupUnit)
//   4   int16 x_coord;
//   6   int16 y_coord;
//   8   int16 skin_id;
//   10   int32 unit_id;
//   14   int8 light_percentage;
//   15   int8 unit_type; // 0 = monster, 1 = NPC, 2 = player
//   16   int8 health_percentage;
//   17   int16 pak2_id; // 68 (PAK_SERVER_PuppetInformation)
//   19   int32 unit_id;
//   23   int16 body_type_id;
//   25   int16 feet_type_id;
//   27   int16 hands_type_id;
//   29   int16 head_type_id;
//   31   int16 legs_type_id;
//   33   int16 weapon_type_id;
//   35   int16 shield_type_id;
//   37   int16 back_type_id;

#define PAK_CLIENT_AuthenticateVersion 0x0063
//   2   int32 version_number;
#define PAK_SERVER_AuthenticateVersion 0x0063
//   2   int32 status; // (assumed) 0 = client version unusable, 1 = client version usable

#define PAK_CLIENT_Unknown100 0x0064 // unregistered
#define PAK_SERVER_ResetRegistryInventory 0x0064

#define PAK_CLIENT_Unknown101 0x0065
#define PAK_SERVER_InfoMessage 0x0065
//   2   int32 type; // message type (arbitrary)
//   6   int32 color; // alpha blue green red format
//   10  string16 message;

#define PAK_CLIENT_Unknown102 0x0066
#define PAK_SERVER_InfoMessage2 0x0066
//   2   int32 type; // message type (arbitrary)
//   6   int32 color; // alpha blue green red format
//   10  string16 message;

#define PAK_CLIENT_Unknown103 0x0067
#define PAK_SERVER_MaxCharactersPerAccount 0x0067
//   2   int8 max_characters; // max amount of characters allowed per account (usually 3)

#define PAK_CLIENT_Unknown104 0x0068 // unregistered
#define PAK_SERVER_ToggleWeather 0x0068
//   2   int16 enabled; // set to 0x0001 to enable rain, 0x0000 to disable

#define PAK_CLIENT_Unknown105 0x0069
#define PAK_SERVER_OpenURL 0x0069
//   2   string16 url_to_open; // goes straight into a ShellExecute, prefixed with "http://"

#define PAK_CLIENT_Unknown106 0x006A // unregistered
#define PAK_SERVER_ChestItems 0x006A
//   int16 item_count; // number of items in chest
//   for (item_index = 0; item_index < item_count; item_index++)
//   {
//      int16 item_skin_id; // item skin ID as defined in WDA
//      int32 item_unit_id; // item ID in database
//      int16 item_object_id; // item object ID as defined in WDA
//      int32 quantity; // item quantity
//      int32 number_of_charges; // charges left in item (for ex. keys)
//   }

#define PAK_CLIENT_DepositChest 0x006B
//   2   int32 item_id;
//   6   int32 item_count;
#define PAK_SERVER_Unknown107 0x006B // unregistered

#define PAK_CLIENT_WithdrawChest 0x006C
//   2   int32 item_id;
//   6   int32 item_count;
#define PAK_SERVER_Unknown108 0x006C // unregistered

#define PAK_CLIENT_Unknown109 0x006D // unregistered
#define PAK_SERVER_OpenChest 0x006D

#define PAK_CLIENT_Unknown110 0x006E // unregistered
#define PAK_SERVER_CloseChest 0x006E

#define PAK_CLIENT_RequestExchange 0x0074
//   2   int32 target_unit_id;
//   6   int16 x_coord;
//   8   int16 y_coord;
#define PAK_SERVER_RequestExchange 0x0074 // only in 1.50 and newer
//   int16 target_unit_id; // player ID of the target with whom to instantiate an exchange
//   int16 item_count; // number of items in chest
//   for (item_index = 0; item_index < item_count; item_index++)
//   {
//      int16 item_skin_id; // item skin ID as defined in WDA
//      int32 item_unit_id; // item ID in database
//      int16 item_object_id; // item object ID as defined in WDA
//      int32 quantity; // item quantity
//      int32 number_of_charges; // charges left in item (for ex. keys)
//   }

#define PAK_CLIENT_ItemDescription 0x007A // only in 1.50 and newer
//   2   int32 unit_id;
#define PAK_SERVER_ItemDescription 0x007A // only in 1.50 and newer
//   2   int8 unit_type; // 0 = item unit; 1 = non-unit; 2 = non-item unit
//   if (unit_type == 0)
//   {
//      string16 item_name;
//      int16 item_skin_id;
//      int8 light_percentage;
//      int16 item_armor_class;
//      int16 dodge_malus;
//      int16 required_endurance;
//      int32 minimum_damage;
//      int32 maximum_damage;
//      int16 required_attack;
//      int16 required_strength;
//      int16 required_agility;
//      int16 required_wisdom;
//      int16 required_intellect;
//      int16 boost_count;
//      for (boost_index = 0; boost_index < boost_count; boost_index++)
//      {
//          if (boosted_stat == 10001)
//             int8 boosted_stat; // fixed, 27
//          else if (boosted_stat == 10002)
//             int8 boosted_stat; // fixed, 28
//          else if (boosted_stat == 10004)
//             int8 boosted_stat; // fixed, 29
//          else if (boosted_stat == 10008)
//             int8 boosted_stat; // fixed, 30
//          else if (boosted_stat == 10009)
//             int8 boosted_stat; // fixed, 31
//          else if (boosted_stat == 10011)
//             int8 boosted_stat; // fixed, 32
//          else if (boosted_stat == 10012)
//             int8 boosted_stat; // fixed, 33
//          else if (boosted_stat == 10014)
//             int8 boosted_stat; // fixed, 34
//          else if (boosted_stat == 10015)
//             int8 boosted_stat; // fixed, 35
//          else if (boosted_stat == 10016)
//             int8 boosted_stat; // fixed, 36
//          else if (boosted_stat == 10017)
//             int8 boosted_stat; // fixed, 37
//          else if (boosted_stat == 10026)
//             int8 boosted_stat; // fixed, 38
//          else if (boosted_stat == 10027)
//             int8 boosted_stat; // fixed, 39
//          else if (boosted_stat == 10028)
//             int8 boosted_stat; // fixed, 40
//          else if (boosted_stat == 10029)
//             int8 boosted_stat; // fixed, 41
//          else if (boosted_stat == 10035)
//             int8 boosted_stat; // fixed, 42
//          else if (boosted_stat == 10036)
//             int8 boosted_stat; // fixed, 43
//          else
//          {
//             int8 boosted_stat;
//             int32 minimum_boost; // only if player's wis & int >= item's wis & int, else 0
//             int32 maximum_boost; // only if player's wis & int >= item's wis & int, else 0
//          }
//      }
//   }

#define PAK_CLIENT_Unknown10001 0x2711 // unregistered
#define PAK_SERVER_AttackMissed 0x2711
//   2   int32 unit_id;
//   6   int32 target_unit_id; // BUG: always 0
//   10   int8 light_percentage;
//   11   int8 unit_type; // 0 = monster, 1 = NPC, 2 = player
//   12   int8 health_percentage;
//   13   int16 x_coord;
//   15   int16 y_coord;
//   17   int16 target_x_coord;
//   19   int16 target_y_coord;

#define PAK_CLIENT_Unknown10002 0x2712 // unregistered
#define PAK_SERVER_AttackSuccess 0x2712
//   2   int32 unit_id;
//   6   int32 target_unit_id; // BUG: always 0
//   10   int16 x_coord;
//   12   int16 y_coord;
//   14   int16 target_x_coord;
//   16   int16 target_y_coord;

#define PAK_CLIENT_Unknown10003 0x2713 // unregistered
#define PAK_SERVER_SkillSuccess 0x2713
//   2   int16 skill_id;
//   4   int16 value; // unused ?

#define PAK_CLIENT_Unknown10004 0x2714 // unregistered
#define PAK_SERVER_PopupUnit 0x2714
//   2   int16 x_coord;
//   4   int16 y_coord;
//   6   int16 skin_id;
//   8   int32 unit_id;
//   12   int8 light_percentage;
//   13   int8 unit_type; // 0 = monster, 1 = NPC, 2 = player
//   14   int8 health_percentage;

#endif // PAKTYPES_H

Pak pour les clients NMS uniquement

#define PAK_CLIENT_DeathStatus 0x00C8 // Affichage de la mort en jeu
#define PAK_SERVER_DeathStatus 0x00C8
//   2   int8 death_flag; (1 = Death; 0 = Not death)

#define PAK_CLIENT_DeathProgress 0x00C9 // Affichage de la mort en jeu
#define PAK_SERVER_DeathProgress 0x00C9
//   2   int16 res_countdown;
//   3   int16 res_countdown_max;
//   5   int8 resurrection_button_flag; (1 = visible; 0 = invisible)

#define	PAK_CLIENT_DeathResurection 0x00CA // Demande de la résurrection du personnage
#define	PAK_SERVER_DeathResurection 0x00CA

 */


}
