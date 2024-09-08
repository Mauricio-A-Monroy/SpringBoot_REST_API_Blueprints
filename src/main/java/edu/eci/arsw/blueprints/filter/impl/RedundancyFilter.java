package edu.eci.arsw.blueprints.filter.impl;

import edu.eci.arsw.blueprints.filter.BluePrinterFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RedundancyFilter implements BluePrinterFilter {

    @Override
    public Blueprint filterBlueprint(Blueprint blueprint){
        List<Point> newPointList = new ArrayList<>();
        List<Point> currentPointList = blueprint.getPoints();

        // First element
        if(currentPointList.get(0) != currentPointList.get(1)){
            newPointList.add(currentPointList.get(0));
        }

        // Middle elements
        for (int i = 1; i < currentPointList.size() - 1 ; i++ ){
            Point p1 = currentPointList.get(i-1);
            Point p2 = currentPointList.get(i);
            Point p3 = currentPointList.get(i+1);

            if (!p2.equals(p1) && !p2.equals(p3)){
                newPointList.add(p2);
            }
        }

        // Last element
        if(currentPointList.get(currentPointList.size() - 1) != currentPointList.get(currentPointList.size() - 2)){
            newPointList.add(currentPointList.get(currentPointList.size() - 1));
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
