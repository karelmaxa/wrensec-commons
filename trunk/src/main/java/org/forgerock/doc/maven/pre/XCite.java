/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * If applicable, add the following below this MPL 2.0 HEADER, replacing
 * the fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *     Portions Copyright [yyyy] [name of copyright owner]
 *
 *     Copyright 2013-2014 ForgeRock AS
 *
 */

package org.forgerock.doc.maven.pre;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.forgerock.doc.maven.AbstractDocbkxMojo;
import org.twdata.maven.mojoexecutor.MojoExecutor;

/**
 * Use the <a href="https://github.com/markcraig/xcite-maven-plugin"
 * >XCite Maven plugin</a> to quote text files.
 *
 * <p>
 *
 * This class generates source including the citations.
 * For example, if your DocBook source file includes
 * the following &lt;programlisting&gt;:
 *
 * <pre>
 * &lt;programlisting language=&quot;java&quot;
 * &gt;[jcp:org.forgerock.doc.jcite.test.Test:--- mainMethod]&lt;/programlisting&gt;
 * </pre>
 *
 * <p>
 *
 * Then this class replaces the citation with the code
 * in between {@code // --- mainMethod} comments,
 * suitable for inclusion in XML,
 * and leaves the new file with the modifiable copy of the sources
 * for further processing.
 */
public class XCite {

    /**
     * The Mojo that holds configuration and related methods.
     */
    private AbstractDocbkxMojo m;

    /**
     * The Executor for the XCite plugin.
     */
    private final Executor exec;

    /**
     * XCite plugin version.
     */
    private final String xCiteVersion;

    /**
     * Plexus utils version.
     */
    private final String plexusUtilsVersion;

    /**
     * Source directory for sources to XCite.
     */
    private final String sourceDir;

    /**
     * Escape XML characters in quotes from other files.
     */
    private final boolean escapeXml = true;

    /**
     * Resolve quotations only in XML files.
     */
    private final String includes = "**/*.xml";

    /**
     * Constructor setting the Mojo that holds the configuration.
     *
     * @param mojo The Mojo that holds the configuration.
     */
    public XCite(final AbstractDocbkxMojo mojo) {
        m = mojo;
        this.exec               = new Executor();
        this.xCiteVersion       = m.getXCiteVersion();
        this.plexusUtilsVersion = m.getPlexusUtilsVersion();
        this.sourceDir          = m.path(m.getDocbkxModifiableSourcesDirectory());
    }

    /**
     * Run XCite on the XML source files.
     *
     * @throws MojoExecutionException   Could not create output directory.
     * @throws MojoFailureException     Failed to perform replacements.
     */
    public void execute() throws MojoExecutionException, MojoFailureException {

        // Run the XCite plugin on the files in place.
        exec.runXCite();
    }

    /**
     * Enclose methods to run plugins.
     */
    class Executor extends MojoExecutor {

        /**
         * Run XCite on the DocBook XML source files.
         *
         * @throws MojoExecutionException   Could not create output directory.
         * @throws MojoFailureException     Failed to perform replacements.
         */
        void runXCite() throws MojoExecutionException, MojoFailureException {

            executeMojo(
                    plugin(
                            groupId("org.forgerock.maven.plugins"),
                            artifactId("xcite-maven-plugin"),
                            version(xCiteVersion),
                            dependencies(
                                    dependency(
                                            groupId("org.codehaus.plexus"),
                                            artifactId("plexus-utils"),
                                            version(plexusUtilsVersion)))),
                    goal("cite"),
                    configuration(
                            element(name("sourceDirectory"), sourceDir),
                            element(name("outputDirectory"), sourceDir),
                            element(name("escapeXml"), Boolean.toString(escapeXml)),
                            element(name("includes"),
                                    element(name("include"), includes))),
                    executionEnvironment(m.getProject(), m.getSession(), m.getPluginManager()));
        }
    }
}
