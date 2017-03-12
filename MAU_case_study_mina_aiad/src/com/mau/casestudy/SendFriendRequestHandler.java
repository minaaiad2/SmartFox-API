package com.mau.casestudy;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;

public class SendFriendRequestHandler extends BaseClientRequestHandler{

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
	
		int targetPlayerId=params.getInt("targetplayerid");
		int currentPlayerId=params.getInt("currentplayerid");
		 IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		 //status =0 as it is flag for pending freind request
	     String addRequestQuery = "INSERT INTO `friendship` "
	     		+ "(`player_one_id`,`player_two_id`, `status`, `action_player_id`) VALUES"
	     		+ "( "+currentPlayerId+", "+targetPlayerId+", 0, "+currentPlayerId+");";
	         
	        try
	        {
	        	 ISFSObject response = new SFSObject();
		         SFSObject rtn=new SFSObject();
	             //validate that the current player id is correnct 2
	        	String validateCurrentplayerIDQuery ="select id from players where id="+currentPlayerId;
	        	ISFSArray res =dbManager.executeQuery(validateCurrentplayerIDQuery, new Object[] {});
	        	String validatetarStringplayerIDQuery ="select id from players where id="+targetPlayerId;
	        	ISFSArray res2 =dbManager.executeQuery(validatetarStringplayerIDQuery, new Object[] {});
	        	String validateFriendRequestQuery ="select * from friendship "
	        			+ "where player_one_id="+currentPlayerId+" and player_two_id="+targetPlayerId+";";
	        	ISFSArray res3 =dbManager.executeQuery(validateFriendRequestQuery, new Object[] {});
	        	
	        	///validate that the target player id is corrent 
	        	if(res.size()==0)
	        	{
	        		 rtn.putInt("requestResultCode", 301);
		 	         rtn.putText("requestResultMsg", "current player ID is invalid.");
	        	}
	        	//validate that the target player id is corrent 
	        	else if(res2.size()==0)
	        	{
	        		 rtn.putInt("requestResultCode", 302);
		 	         rtn.putText("requestResultMsg", "target player ID is invalid.");
	        	}
	        	//validate that there is no duplicate request 
	        	else if(res3.size()!=0)
	        	{
	        		rtn.putInt("requestResultCode", 302);
		 	         rtn.putText("requestResultMsg", "friend request is duplicate.");
	        	}
	        	else //every thing is valid just add the request
	        	{
	        		 dbManager.executeQuery(addRequestQuery, new Object[] {});
	 	            //200 flag for success
	 	            rtn.putInt("requestResultCode", 200);
	 	            rtn.putText("requestResultMsg", "request have been sent");
	 	            
	        	}	           
	            GameRequestHandler parentExt=(GameRequestHandler)getParentExtension(); 
	            // Send back to requester
	            parentExt.send("sendfriendrequest", response, sender);
	        }
	        catch (SQLException e)
	        {
	            trace(ExtensionLogLevel.WARN, "SQL Failed: " + e.toString());
	        }
	    }
	}
		
	

