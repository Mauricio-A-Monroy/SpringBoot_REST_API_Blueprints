/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.test.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author hcadavid
 */
public class InMemoryPersistenceTest {
    
    @Test
    public void saveNewAndLoadTest() throws BlueprintPersistenceException, BlueprintNotFoundException{
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();

        Point[] pts0=new Point[]{new Point(40, 40),new Point(15, 15)};
        Blueprint bp0=new Blueprint("mack", "mypaint",pts0);
        
        ibpp.saveBlueprint(bp0);
        
        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);
        
        ibpp.saveBlueprint(bp);
        
        assertNotNull("Loading a previously stored blueprint returned null.",ibpp.getBlueprint(bp.getAuthor(), bp.getName()));
        
        assertEquals("Loading a previously stored blueprint returned a different blueprint.",ibpp.getBlueprint(bp.getAuthor(), bp.getName()), bp);
        
    }


    @Test
    public void saveExistingBpTest() {
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();
        
        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);
        
        try {
            ibpp.saveBlueprint(bp);
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }
        
        Point[] pts2=new Point[]{new Point(10, 10),new Point(20, 20)};
        Blueprint bp2=new Blueprint("john", "thepaint",pts2);

        try{
            ibpp.saveBlueprint(bp2);
            fail("An exception was expected after saving a second blueprint with the same name and autor");
        }
        catch (BlueprintPersistenceException ex){
            
        }

    }

    @Test
    public void getBluePrints(){
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        BlueprintsServices bs = ac.getBean(BlueprintsServices.class);

        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);

        Point[] pts2=new Point[]{new Point(10, 10),new Point(20, 20)};
        Blueprint bp2=new Blueprint("john", "thepaintVol2", pts2);

        try {

            // ibpp.saveBlueprint(bp);
            // ibpp.saveBlueprint(bp2);
            bs.addNewBlueprint(bp);
            bs.addNewBlueprint(bp2);
            Blueprint blueprint = bs.getBlueprint("john", "thepaintVol2");
            assertEquals(blueprint, bp2);

        } catch (BlueprintPersistenceException | BlueprintNotFoundException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }

    }

    @Test
    public void getBluePrintsByAuthor(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        BlueprintsServices bs = ac.getBean(BlueprintsServices.class);

        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);

        Point[] pts2=new Point[]{new Point(10, 10),new Point(20, 20)};
        Blueprint bp2=new Blueprint("john", "thepaintVol2",pts2);

        try {

            bs.addNewBlueprint(bp);
            bs.addNewBlueprint(bp2);
            Set<Blueprint> blueprintSet = bs.getBlueprintsByAuthor("john");
            assertEquals(blueprintSet.size(), 2);

        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        } catch (BlueprintNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void filterBlueprintByRedundancy(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        BlueprintsServices bs = ac.getBean(BlueprintsServices.class);

        Point[] pts=new Point[]{new Point(0, 0), new Point(0, 0), new Point(20, 20), new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);

        try {

            bs.addNewBlueprint(bp);
            Blueprint blueprint = bs.getBlueprint("john", "thepaint");
            for(int i = 0; i < blueprint.getPoints().size() - 1; i++){
                assertFalse(blueprint.getPoints().get(i).equals(blueprint.getPoints().get(i+1)));
            }

        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        } catch (BlueprintNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void filterBlueprintsByRedundancy(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        BlueprintsServices bs = ac.getBean(BlueprintsServices.class);

        Point[] pts=new Point[]{new Point(0, 0), new Point(0, 0), new Point(20, 20), new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);

        Point[] pts2=new Point[]{new Point(10, 10), new Point(0, 0), new Point(20, 20), new Point(10, 10)};
        Blueprint bp2=new Blueprint("john", "thepaintVol2",pts2);

        try {

            bs.addNewBlueprint(bp);
            bs.addNewBlueprint(bp2);
            Set<Blueprint> blueprintSet = bs.getBlueprintsByAuthor("john");
            assertFalse(blueprintSet.contains(bp));
            assertTrue(blueprintSet.contains(bp2));
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        } catch (BlueprintNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void filterBlueprintsBySubsampling(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        BlueprintsServices bs = ac.getBean(BlueprintsServices.class);

        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);

        try {

            bs.addNewBlueprint(bp);
            Blueprint blueprint = bs.getBlueprint("john", "thepaint");
            assertEquals(blueprint.getPoints().size(), bp.getPoints().size() / 2);

        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        } catch (BlueprintNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void filterBlueprintBySubsampling(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        BlueprintsServices bs = ac.getBean(BlueprintsServices.class);

        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);

        Point[] pts2=new Point[]{new Point(10, 10),new Point(10, 10), new Point(20, 20)};
        Blueprint bp2=new Blueprint("john", "thepaintVol2",pts2);

        try {

            bs.addNewBlueprint(bp);
            bs.addNewBlueprint(bp2);
            Set<Blueprint> blueprintSet = bs.getBlueprintsByAuthor("john");
            Blueprint[] blueprints = blueprintSet.toArray(new Blueprint[blueprintSet.size()]);
            for(Blueprint i : blueprints){
                if(i.getName().equals("thepaint")){
                    assertEquals(i.getPoints().size(), bp.getPoints().size() / 2);
                }
                else{
                    assertEquals(i.getPoints().size(), bp2.getPoints().size() / 2);
                }
            }

        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        } catch (BlueprintNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
