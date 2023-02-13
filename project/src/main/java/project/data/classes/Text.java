package project.data.classes;

public class Text extends PlenaryObject{
    private String text;

    public Text(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
