package android.example.com.houses;

public class Opinion {

    public Opinion(String autor, String content, int stars)
    {
        this.author=autor;
        this.content=content;
        this.checkedStars=stars;
    }
    private String author;
    private String content;
    private int checkedStars;


    public void setAuthor(String author)
    {
        this.author=author;
    }

    public void setContent(String content)
    {
        this.content=content;
    }
    public void setCheckedStars(int stars){this.checkedStars=stars;}
    public int getCheckedStars(){return this.checkedStars;}
    public String getAutor(){return author;}
    public String getContent(){return content;}
}
