package edu.eci.arsw.blueprints.filter.impl;

import edu.eci.arsw.blueprints.filter.BluePrinterFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.stereotype.Service;

import java.util.*;

//@Service
public class SubsamplingFilter implements BluePrinterFilter {

    @Override
    public Blueprint filterBlueprint(Blueprint blueprint){
        List<Point> newPointList = new ArrayList<>();
        List<Point> currentPointList = blueprint.getPoints();

        for (int i = 0; i < currentPointList.size() - 1 ; i+=2){
            newPointList.add(currentPointList.get(i));
        }

        Point[] pointsArray = null;
        pointsArray = newPointList.toArray(new Point[newPointList.size()]);

        return new Blueprint(blueprint.getAuthor(), blueprint.getName(), pointsArray);
    }

    @Override
    public Set<Blueprint> filterBlueprints(Set<Blueprint> blueprints){
        Set<Blueprint> newBlueprintSet = new HashSet<>();

        for(Blueprint i: blueprints){
            newBlueprintSet.add(filterBlueprint(i));
        }

        return newBlueprintSet;
    }
}
