package io.mycat.mycat2.cmds;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mycat2.MySQLCommand;
import io.mycat.mycat2.MySQLSession;
import io.mycat.mycat2.MycatSession;

public class ComChangeUserCmd implements MySQLCommand{
	
	private static final Logger logger = LoggerFactory.getLogger(ComChangeUserCmd.class);

	public static final ComChangeUserCmd INSTANCE = new ComChangeUserCmd();

	@Override
	public boolean procssSQL(MycatSession session) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackendResponse(MySQLSession session) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackendClosed(MySQLSession session, boolean normal) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFrontWriteFinished(MycatSession session) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackendWriteFinished(MySQLSession session) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clearFrontResouces(MycatSession session, boolean sessionCLosed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearBackendResouces(MySQLSession session, boolean sessionCLosed) {
		// TODO Auto-generated method stub
		
	}
}
