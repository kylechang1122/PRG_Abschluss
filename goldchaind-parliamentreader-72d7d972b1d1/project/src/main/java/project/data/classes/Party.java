package project.data.classes;

import java.util.HashSet;
import java.util.Set;

public class Party extends PlenaryObject{

    private String name;
    private Set<Speaker> members = new HashSet<>();
    public Party(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Set<Speaker> getMembers(){
        return members;
    }

    public void addMember(Speaker speaker){
        members.add(speaker);
    }

}
