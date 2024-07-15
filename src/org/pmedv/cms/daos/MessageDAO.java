package org.pmedv.cms.daos;


public class MessageDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Message message order by message.sender";
	}

	@Override
	protected String getQueryById() {
		return "from Message message where message.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Message message where message.sender = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Message message where message.sender like ? order by message.received";
	}

}
