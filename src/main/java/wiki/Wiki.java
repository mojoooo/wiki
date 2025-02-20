package wiki;

import java.time.LocalDateTime;

public class Wiki
{
    private static int nextId = 1;
    private int id;
    private String entry;
    private LocalDateTime lastModified;

    public String getEntry()
    {
        return entry;
    }

    public void setEntry(String entry, LocalDateTime newModified)
    {
        this.entry = entry;
        this.lastModified = newModified;
    }
    
    public Wiki(String content)
    {
        this.setId(Wiki.nextId++);
        this.setEntry(content, LocalDateTime.now());
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public LocalDateTime getLastModified()
    {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified)
    {
        this.lastModified = lastModified;
    }
}
