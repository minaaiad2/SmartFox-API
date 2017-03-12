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
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class RespondFriendRequestHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User sender, ISFSObject params) {

		int fromplayerID=params.getInt("fromplayerid");
		//flag to represent the response 
		//1 accept
		//2 reject
		int status=params.getInt("status");
		 IDBManager dbManager = getParentExtension().getParentZone().getDBManager();
		 //status =0 as it is flag for pending freind request
	     String respondRequestQuery = "update friendship set status="+status+" where action_player_id="+fromplayerID+";";
	     		
	         
	        try
	        {
	        	 ISFSObject response = new SFSObject();
		         SFSObject rtn=new SFSObject();
	             //validate that the current player id is correnct 2
	        	String validateFromplayerIDQuery ="select id from players where id="+fromplayerID;
	        	ISFSArray res =dbManager.executeQuery(validateFromplayerIDQuery, new Object[] {});
	        	String checkFriendRequestQuery ="select * from friendship "
	        			+ "where action_player_id="+fromplayerID+";";
	        	ISFSArray res2 =dbManager.executeQuery(checkFriendRequestQuery, new Object[] {});
	        	
	        	///validate that the from player id is corrent 
	        	if(res.size()==0)
	        	{
	        		 rtn.putInt("requestResultCode", 301);
		 	         rtn.putText("requestResultMsg", "from player ID is invalid.");
	        	}
	        	//validate that the request exists 
	        	else if(res2.size()==0)
	        	{
	        		 rtn.putInt("requestResultCode", 302);
		 	         rtn.putText("requestResultMsg", "friend request doesn't exists");
	        	}
	        	else if( status!=1 && status !=2)
	        	{
	        		rtn.putInt("requestResultCode", 303);
		 	         rtn.putText("requestResultMsg", "status value is incorrect");
	        	}
	        	else //every thing is valid just add the request
	        	{
	        		 dbManager.executeQuery(respondRequestQuery, new Object[] {});
	 	            //200 flag for success
	 	            rtn.putInt("requestResultCode", 200);
	 	            rtn.putText("requestResultMsg", "request have been updated");
	 	            
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
		
	

