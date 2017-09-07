package io.mycat.mycat2.cmds.sqlCmds;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.cmds.DirectPassthrouhCmd;
import io.mycat.mysql.AutoCommit;

public class SqlComCommitCmd extends DirectPassthrouhCmd{
	
	private static final Logger logger = LoggerFactory.getLogger(SqlComCommitCmd.class);

	public static final SqlComCommitCmd INSTANCE = new SqlComCommitCmd();

	@Override
	public boolean procssSQL(MycatSession session) throws IOException {
		super.procssSQL(session);
		/*
		 * 提交事务
		 * TODO 事务兼容性完善.
		 */
		session.autoCommit=AutoCommit.ON;
		return false;
	}
}
