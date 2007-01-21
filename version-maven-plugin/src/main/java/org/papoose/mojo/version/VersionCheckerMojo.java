/**
 *  Copyright 2006 Too Lazy Goats Inc., 2265 Alva Avenue, El Cerrito, CA 94530 U.S.A. All rights reserved.
 */
package org.papoose.mojo.version;

import org.apache.commons.lang.SystemUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * Forces the build to fail if the version of Java is not compatible.
 *
 * This class was stolen from Geronimo
 *
 * @goal require-java-version
 * @phase validate
 * @description Java version checking plugin
 * @version $Revision$ $Date: $
 */
public class VersionCheckerMojo extends AbstractMojo
{
    /**
     * Specify the required version of Java (1.1, 1.2, 1.3, 1.4, 1.5).
     * <p/>
     * Can specify a suffix of '+' to allow any version equal to or newer or '*'
     * to allow versions in the same group.
     * <p/>
     * For example, version=1.4+ would be allowed on a JDK 1.5 VM, version=1.5*
     * would allow any JDK 1.5, but not JDK 1.6.
     *
     * @parameter
     * @required
     */
    private String version = null;

    /**
     * Flag to skip the version check.
     *
     * @parameter default-value="false"
     */
    private boolean skip = false;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (skip)
        {
            getLog().warn("Skipping Java version check");
            return;
        }

        version = version.trim();

        if (version.endsWith("*"))
        {
            version = version.substring(0, version.length() - 1).trim();

            getLog().debug("Checking Java version is in the same group as: " + version);

            String tmp = SystemUtils.JAVA_VERSION_TRIMMED;

            getLog().debug("Requested version: " + tmp);
            getLog().debug("JVM version: " + SystemUtils.JAVA_VERSION_FLOAT);

            if (!tmp.startsWith(version))
            {
                throw new MojoFailureException("This build requires Java version " + version +
                                               " or a greater version in the same group, found version: " +
                                               SystemUtils.JAVA_VERSION_FLOAT);
            }
        }
        else if (version.endsWith("+"))
        {
            version = version.substring(0, version.length() - 1).trim();

            getLog().debug("Checking Java version is greater than: " + version);

            float tmp = Float.parseFloat(version);

            getLog().debug("Requested version: " + tmp);
            getLog().debug("JVM version: " + SystemUtils.JAVA_VERSION_FLOAT);

            if (tmp > SystemUtils.JAVA_VERSION_FLOAT)
            {
                throw new MojoFailureException("This build requires Java version " + version +
                                               " or greater, found version: " + SystemUtils.JAVA_VERSION_FLOAT);
            }
        }
        else
        {
            getLog().debug("Checking Java version is equal to: " + version);

            float tmp = Float.parseFloat(version);

            getLog().debug("Requested version: " + tmp);
            getLog().debug("JVM version: " + SystemUtils.JAVA_VERSION_FLOAT);

            if (tmp != SystemUtils.JAVA_VERSION_FLOAT)
            {
                throw new MojoFailureException("This build requires Java version " + version +
                                               ", found version: " + SystemUtils.JAVA_VERSION_FLOAT);
            }
        }
    }
}

