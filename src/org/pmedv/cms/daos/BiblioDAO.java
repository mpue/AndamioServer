package org.pmedv.cms.daos;

public class BiblioDAO extends AbstractDAO {

	@Override
	protected String getQueryAll() {
		return "from Biblio biblio order by biblio.identifier";
	}

	@Override
	protected String getQueryById() {
		return "from Biblio biblio where biblio.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Biblio biblio where biblio.identifier = ?";
	}

	@Override
	protected String getQueryElementsByName() {
		return "from Biblio biblio where biblio.identifier like ? order by biblio.identifier";
	}

}
