import java.io.File;
import java.io.FilenameFilter;

public final class EndsWithFilter implements FilenameFilter{
	private final String endsWith;
	public EndsWithFilter( String endsWith ){
        this.endsWith = endsWith.toLowerCase();
        }
    public boolean accept( File dir, String name ){
        File f = new File( dir, name );
        if ( f.isDirectory() ){
            return false;
            }
        return name.toLowerCase().endsWith( endsWith );
        }
    }
