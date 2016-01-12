/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ptahi.aet.aetproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.netbeans.api.project.Project;
import org.openide.util.lookup.ServiceProvider;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
/**
 *
 * @author paulorlov
 */
@ServiceProvider(service=ProjectFactory.class)
public class AETProjectFactory implements ProjectFactory {
    
    public static final String PROJECT_FILE = "aetproject.aet";

    
    //Specifies when a project is a project, i.e.,
    //if "customer.txt" is present in a folder:
    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(PROJECT_FILE) != null;
    }

    //Specifies when the project will be opened, i.e., if the project exists:
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new AETProject(dir, state) : null;
    }

    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
        //First check that the project folder is there,
        //otherwise the project cannot be saved:
        FileObject projectRoot = project.getProjectDirectory();
        if (!projectRoot.isFolder()) {
            throw new IOException("Project dir " + projectRoot.getPath()
                    + " deleted,"
                    + " cannot save project");
        }

        //Force the creation of the project folder if it was deleted:
//        ((AETProject) project).getTextFolder(true);

        //Find the Properties file, creating it if necessary:
        String propsPath = projectRoot.getPath() + File.separator + "aetproject.aet";
//        String propsPath = PROJECT_DIR + "/project.properties";
        FileObject propertiesFile = projectRoot.getFileObject("aetproject.aet");
        if (propertiesFile == null) {
            //Recreate the Properties file, if needed:
            propertiesFile = projectRoot.createData(propsPath);
        }

        //Get the properties file from the project's Lookup,
        //where the properties file now has a changed key:
        Properties properties = (Properties) project.getLookup().lookup(Properties.class);

        //Get the file from the FileObject obtained above:
        File f = FileUtil.toFile(propertiesFile);

        //Write the project Lookup's properties list to the file defined above:
        properties.store(new FileOutputStream(f), "Description of the property list");

    }

}
