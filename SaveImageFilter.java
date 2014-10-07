import javax.swing.filechooser.FileFilter;
import java.io.File;

/* ImageFilter.java is used by FileChooserDemo2.java. */
public class SaveImageFilter extends FileFilter {

	String name; 
    //Accept all directories and all gif, jpg, tiff, or png files.
	public SaveImageFilter(String name){
		this.name = name;
	}

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            return extension.equals("tiff") ||
                    extension.equals("tif") ||
                    extension.equals("gif") ||
                    extension.equals("jpeg") ||
                    extension.equals("jpg") ||
                    extension.equals("png");
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return name;
    }
}