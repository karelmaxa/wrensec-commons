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
 *     Copyright 2014 ForgeRock AS
 *
 */

package org.forgerock.doc.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.forgerock.doc.maven.release.Css;
import org.forgerock.doc.maven.release.Favicon;
import org.forgerock.doc.maven.release.IndexHtml;
import org.forgerock.doc.maven.release.Layout;
import org.forgerock.doc.maven.release.PdfNames;
import org.forgerock.doc.maven.release.Robots;
import org.forgerock.doc.maven.release.Zip;

/**
 * Call other classes to prepare release layout documents.
 */
@Mojo(name = "release", defaultPhase = LifecyclePhase.SITE)
public class ReleaseMojo extends AbstractDocbkxMojo {

    /**
     * Call other classes to prepare release layout documents.
     *
     * @throws MojoExecutionException Failed to prepare docs successfully.
     */
    @Override
    public void execute() throws MojoExecutionException {

        // When not producing final output, but only preprocessed XML,
        // we can interrupt processing now.
        if (stopAfterPreProcessing()) {
            return;
        }

        new Layout(this).execute();
        new IndexHtml(this).execute();
        new PdfNames(this).execute();
        new Favicon(this).execute();
        new Css(this).execute();
        new Robots(this).execute();
        new Zip(this).execute();
    }
}
