package edu.eci.arsw.blueprints.filter;

import edu.eci.arsw.blueprints.model.Blueprint;

import java.util.Set;

public interface BluePrinterFilter {
    public Blueprint filterBlueprint(Blueprint blueprint);

    public Set<Blueprint> filterBlueprints(Set<Blueprint> blueprints);
}
