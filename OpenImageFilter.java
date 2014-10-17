import javax.swing.filechooser.FileFilter;

import java.io.File;

/* ImageFilter.java is used by FileChooserDemo2.java. */
public class OpenImageFilter extends FileFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.

	String name;	//name of filter
	
	public OpenImageFilter(String name){
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
        	if(name == "All Images"){					/*Defect - 43*/
        		return extension.equals("tiff") ||
        				extension.equals("tif") ||
        				extension.equals("gif") ||
        				extension.equals("png");
        	}
        	else if(name == "tiff"){					/*Defect - 005*/
        		return extension.equals("tifff");
        	}
        	else if(name == "tif"){
        		return extension.equals("tifff");
        	}
        	else if(name == "gif"){						/*Defect - 40*/
        		return extension.equals("jpeg");
        	}
        	else if(name == "png"){						/*Defect - 41*/
        		return extension.equals("gif");
        	}
        	else if(name == "jpeg"){					/*Defect - 42*/
        		return extension.equals("png");
        	}
        	else if(name == "jpg"){
        		return extension.equals("jpg");
        	}
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return name;
    }
}