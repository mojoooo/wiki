package wiki;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/wiki")

public class WikiServlet extends HttpServlet
{
    private List<Wiki> entries;
    
    public void addEntry(Wiki entry)
    {
        this.entries.add(entry);
    }

    public List<Wiki> getAllEntries()
    {
        return this.entries;
    }
    
    public Wiki getWikiById(int id)
    {
        for (Wiki entry : this.getAllEntries())
        {
            if (entry.getId() == id)
            {
                return entry;
            }
        }
        
        return null;
    }
    
    public void editEntry(Wiki entryToEdit)
    {
        Wiki tmp = this.getWikiById(entryToEdit.getId());

        if (tmp != null)
        {
            tmp.setEntry(entryToEdit.getEntry(), LocalDateTime.now());
        }
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        this.entries = new ArrayList<Wiki>();
        
        Wiki entry1 = new Wiki("Erster Wiki Eintrag");
        Wiki entry2 = new Wiki("Zweiter Wiki Eintrag");
        this.addEntry(entry1);
        this.addEntry(entry2);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {     
        String content = req.getParameter("content");
        String lastModifiedString = req.getParameter("lastmodified");
        int wikiId = 0;
        try
        {
            wikiId = Integer.parseInt(req.getParameter("id"));
        }
        catch (Exception e)
        {
        }
        
        if (content != null && !content.isEmpty() && wikiId != 0 && lastModifiedString != null)
        {            
            PrintWriter out = resp.getWriter();
            Wiki selectedWiki = this.getWikiById(wikiId);
            
            if (selectedWiki != null && !selectedWiki.getEntry().equals(content))
            {
                LocalDateTime clientLastModified = LocalDateTime.parse(lastModifiedString);
                LocalDateTime serverLastModified = selectedWiki.getLastModified();
                
                if (!clientLastModified.equals(serverLastModified))
                {
                    out.println("<script>alert('"
                            + "Fehler, es existiert ein neuerer Eintrag. Bitte Seite neu laden!"
                            + "Ihr Eingegebener Text lautet:"
                            + content + "');</script>"
                            );
                }
                else
                {
                    selectedWiki.setEntry(content, LocalDateTime.now());
                }
            }
        }
        
        this.showHTML(req, resp);
    }
    
    public void showHTML(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Wiki</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Wiki</h1>");
        
        for (Wiki entry : this.getAllEntries())
        {
            out.println("<div style='border: 2px solid #ddd; padding: 20px; margin: 15px 0; width: 80%; max-width: 600px; border-radius: 12px; background-color: #f4f4f9; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);'>");
            out.println("<form action='' method='post'>");
            out.println("<p>ID: " + entry.getId() + " last modified: " + entry.getLastModified() + "</p>");
            out.println("<input type='hidden' name='lastmodified' value='" + entry.getLastModified() + "'>");
            out.println("<label for='content' style='font-size: 16px; font-weight: bold; color: #333;'>Bearbeite den Text:</label><br>");
            out.println("<textarea id='content' name='content' rows='10' style='width: 100%; padding: 12px; margin: 10px 0; border: 2px solid #ccc; border-radius: 8px; background-color: #fff; font-size: 14px; resize: vertical;'>" + entry.getEntry() + "</textarea><br>");
            out.println("<button type='submit' name='id' value='" + entry.getId() + "' style='background-color: #007bff; color: white; padding: 12px 20px; font-size: 16px; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s;'>Edit</button>");
            out.println("</form>");
            out.println("</div>");
        }

        out.println("</body>");
        out.println("</html>");
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        this.showHTML(req, resp);
    }
}
