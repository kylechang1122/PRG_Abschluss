package project.data.classes;

import java.util.HashSet;
import java.util.Set;

public class Fraction extends PlenaryObject {
    private String name;
    private Set<Speaker> members = new HashSet<Speaker>();
    public String getName(){
        return name;
    }
    public void addMember(Speaker speaker){
        members.add(speaker);
    }

    public Set<Speaker> getMembers(){
        return members;
    }
}
