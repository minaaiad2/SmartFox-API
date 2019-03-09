package com.mau.casestudy;




import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class GameRequestHandler extends SFSExtension {
	public static final String DATABASE_ID = "dbID";
	
	@Override
	public void init()
	{
		trace("Database MAU case study Extension -- started");
		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);
		addRequestHandler("sendfriendrequest", SendFriendRequestHandler.class);
		addRequestHandler("respondfriendrequest", RespondFriendRequestHandler.class);
	}
	
	@Override
	public void destroy()
	{
	    super.destroy();
	    trace("Database Login Extension -- stopped");
	}
	
}
